package com.example.nmedia.viewModels

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.nmedia.BASE_URL_IMAGES
import com.example.nmedia.R

fun ImageView.loadAvatar(url: String) {
    Glide.with(this)
        .load("${BASE_URL_IMAGES}avatars/$url")
        .placeholder(R.drawable.ic_error_24)
        .timeout(10_000)
        .circleCrop()
        .into(this)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load("${BASE_URL_IMAGES}media/$url")
        .placeholder(R.drawable.ic_error_24)
        .timeout(10_000)
        .into(this)
}

fun ImageView.loadFitCenter(url: String) {
    Glide.with(this)
        .load("${BASE_URL_IMAGES}media/$url")
        .timeout(10_000)
        .fitCenter()
        .into(this)

}