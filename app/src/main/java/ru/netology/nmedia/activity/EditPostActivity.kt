package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.content.requestFocus()

        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            binding.content.setText(it)
        }

        binding.save.setOnClickListener {
            if (binding.content.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
            val result = Intent()
                .putExtra(Intent.EXTRA_TEXT, binding.content.text.toString())

            setResult(RESULT_OK, result)

            finish()
        }
    }
}