package com.example.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.nmedia.activitys.EditPostActivity
import com.example.nmedia.model.Post

class PostUpdateContract() : ActivityResultContract<Post, Post?>() {

    override fun createIntent(context: Context, input: Post): Intent {
        val intent = Intent(context, EditPostActivity::class.java).apply {
            putExtra(ID,input.id)
            putExtra(CONTENT, input.content)
            putExtra(TITLE, input.title)
            putExtra(DATE, input.date)
            putExtra(LIKES, input.likes)
            putExtra(SHARES, input.shares)
            putExtra(SHOWS, input.shows)
            putExtra(ISLIKED, input.isLiked)
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Post? {
        if (resultCode == Activity.RESULT_CANCELED) {
            return null
        } else {
            val post = Post(
                id = intent?.getIntExtra(ID, DEFAULT)!!,
                title = intent.getStringExtra(TITLE).toString(),
                content = intent.getStringExtra(CONTENT).toString(),
                date = intent.getStringExtra(DATE).toString(),
                likes = intent.getIntExtra(LIKES, DEFAULT),
                shares = intent.getIntExtra(SHARES, DEFAULT),
                shows = intent.getIntExtra(SHOWS, DEFAULT),
                isLiked = intent.getStringExtra(ISLIKED).toBoolean()
            )
            return post
        }
    }


}