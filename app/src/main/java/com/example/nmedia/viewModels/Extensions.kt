package com.example.nmedia.viewModels

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.nmedia.BASE_URL
import com.example.nmedia.R

fun ImageView.loadImage(url:String , view: ImageView){
    Glide.with(view)
        .load("$BASE_URL/avatars/$url")
        .placeholder(R.drawable.ic_error_24)
        .timeout(10_000)
        .into(this)
}