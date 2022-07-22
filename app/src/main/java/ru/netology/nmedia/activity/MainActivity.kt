package ru.netology.nmedia.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostEventListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostActivityContract()) { text ->
            text ?: return@registerForActivityResult
            viewModel.editContent(text)
            viewModel.save()
        }

        val editPostLauncher = registerForActivityResult(EditPostActivityContract()) { text ->
            text ?: return@registerForActivityResult
            viewModel.editContent(text)
            viewModel.save()
        }

        val adapter = PostsAdapter(
            object : PostEventListener {
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(
                        intent,
                        getString(R.string.chooser_share_post)
                    )
                    startActivity(shareIntent)
                    viewModel.shareById(post.id)
                }

                override fun onEdit(post: Post) {
                    Intent().apply {
                        action = Intent.ACTION_EDIT
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    viewModel.edit(post)
                    editPostLauncher.launch(post.content)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onVideo(post: Post) {
                    val appIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:" + post.video)
                    )
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + post.video)
                    )
                    try {
                        startActivity(appIntent)
                    } catch (e: ActivityNotFoundException) {
                        startActivity(webIntent)
                    }
                }
            }
        )
        binding.list.adapter = adapter
        (binding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.create.setOnClickListener {
            newPostLauncher.launch()
        }
    }
}
