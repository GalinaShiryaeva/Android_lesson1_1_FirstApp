package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener {

            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    R.string.empty_content_error,
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_CANCELED)
            } else {
                val result = Intent()
                    .putExtra(Intent.EXTRA_TEXT, binding.content.text.toString())
                setResult(RESULT_OK, result)
            }
            finish()
        }

//        binding.send.setOnClickListener {
//            val intent = Intent()
//                .putExtra(Intent.EXTRA_TEXT, "Test text")
//                .setAction(Intent.ACTION_SEND)
//                .setType("text/plain")
//
//            val createChooser = Intent.createChooser(intent, "Choose app")
//            startActivity(createChooser)
//        }
//
//        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
//            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//        } ?: run {
//            Snackbar.make(
//                binding.root,
//                getString(R.string.empty_content_error),
//                Snackbar.LENGTH_LONG
//            )
//                .setAction(android.R.string.ok) {
//                    finish()
//                }
//                .show()
//        }
    }
}