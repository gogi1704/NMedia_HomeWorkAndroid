package com.example.nmedia.activitys

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nmedia.AndroidUtils
import com.example.nmedia.CONTENT
import com.example.nmedia.databinding.ActivityCreatePostBinding

class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            textEdit.requestFocus()
            AndroidUtils.showKeyboard(parentConstraint)
        }


        binding.fbSavePost.setOnClickListener {

            if (binding.textEdit.text.isNotBlank()) {
                val intent = Intent()
                val content = binding.textEdit.text.toString()
                setResult(Activity.RESULT_OK, intent)
                intent.putExtra(CONTENT, content)

            } else {
                setResult(Activity.RESULT_CANCELED, intent)
            }

            finish()
        }


    }
}