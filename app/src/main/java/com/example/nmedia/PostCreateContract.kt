package com.example.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.nmedia.activitys.CreatePostActivity

class PostCreateContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(context, CreatePostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?):String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(CONTENT).toString()

        } else {
            null
        }
}