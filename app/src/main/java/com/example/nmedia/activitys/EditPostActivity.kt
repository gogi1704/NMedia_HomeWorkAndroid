package com.example.nmedia.activitys

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nmedia.*
import com.example.nmedia.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            textTitle.text = intent.getStringExtra(TITLE)
            textContent.text = intent.getStringExtra(CONTENT)
            textDate.text = intent.getStringExtra(DATE)
            buttonLike.text = intent.getIntExtra(LIKES, DEFAULT).toString()
            buttonShare.text = intent.getIntExtra(SHARES, DEFAULT).toString()
            textShowsCount.text = intent.getIntExtra(SHOWS, DEFAULT).toString()
            buttonLike.isChecked = intent.getBooleanExtra(ISLIKED, false)

            with(createContent){
            setText(intent.getStringExtra(CONTENT))
            requestFocus()
            }

            fabSave.setOnClickListener() {
                val postId = intent.getIntExtra(ID, DEFAULT)
                if (createContent.text.toString() != intent.getStringExtra(TITLE)) {
                    val content = createContent.text.toString()
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    intent.putExtra(ID ,postId )
                    intent.putExtra(CONTENT, content)
                    intent.putExtra(TITLE, textTitle.text)
                    intent.putExtra(DATE, textDate.text)
                    intent.putExtra(LIKES, buttonLike.text.toString().toInt())
                    intent.putExtra(SHARES, buttonShare.text.toString().toInt())
                    intent.putExtra(SHOWS, textShowsCount.text.toString().toInt())
                    intent.putExtra(ISLIKED, buttonLike.isChecked)
                } else setResult(Activity.RESULT_CANCELED, intent)

                finish()
            }
        }
    }
}