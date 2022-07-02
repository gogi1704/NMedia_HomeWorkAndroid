package com.example.nmedia.model

data class Post(
    val id:Int,
    val title: String,
    val date:String,
    val content:String,
    val likes:Int = 0,
    val shares:Int,
    val shows:Int,
    val isLiked: Boolean = false
)