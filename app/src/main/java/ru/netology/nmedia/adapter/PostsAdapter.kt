package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostDiffCallback
import kotlin.math.floor

interface PostEventListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onVideo(post: Post)
}

class PostsAdapter(
    private val listener: PostEventListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = binding,
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: PostEventListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            video.setImageResource(R.mipmap.video_example)
            video.isVisible = !post.video.isNullOrBlank()
            if (video.isVisible) {
                video.setOnClickListener {
                    listener.onVideo(post)
                }
            }

            like.text = validateText(post.likes)
            like.isChecked = post.likedByMe
            like.setOnClickListener {
                listener.onLike(post)
            }

            share.text = validateText(post.shared)
            share.setOnClickListener {
                listener.onShare(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                return@setOnMenuItemClickListener true
                            }
                            R.id.edit -> {
                                listener.onEdit(post)
                                return@setOnMenuItemClickListener true
                            }
                            else -> return@setOnMenuItemClickListener false
                        }
                    }
                }.show()
            }
        }
    }
}

fun validateText(number: Int): String {
    return when (number) {
        in 0..999 -> "$number"
        in 1_000..10_000 -> adjustNumberUnder10K(number) + "K"
        in 10_001..999_999 -> adjustNumberUnder1M(number) + "K"
        else -> adjustNumberOver1M(number) + "M"
    }
}

fun adjustNumberUnder10K(number: Int): String {
    val f = floor((number / 1_000.0) * 10) / 10
    return if ((f * 10).toInt() % 10 == 0) {
        floor(f).toInt().toString()
    } else f.toString()
}

fun adjustNumberUnder1M(number: Int): String {
    return floor((number / 1_000.0)).toInt().toString()
}

fun adjustNumberOver1M(number: Int): String {
    val f = floor((number / 1_000_000.0) * 10) / 10
    return if ((f * 10).toInt() % 10 == 0) {
        floor(f).toInt().toString()
    } else f.toString()
}