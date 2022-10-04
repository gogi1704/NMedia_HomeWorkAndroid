package com.example.nmedia.viewModels

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.nmedia.BASE_URL
import com.example.nmedia.R
import com.example.nmedia.model.Attachment

fun ImageView.loadAvatar(url: String, view: ImageView) {
    Glide.with(view)
        .load("$BASE_URL/avatars/$url")
        .placeholder(R.drawable.ic_error_24)
        .timeout(10_000)
        .circleCrop()
        .into(this)
}

fun ImageView.loadImage(view: ImageView, attachments: Attachment) {
    Glide.with(view)
        .load("$BASE_URL/images/${attachments.url}")
        .placeholder(R.drawable.ic_error_24)
        .timeout(10_000)
        .into(this)
}