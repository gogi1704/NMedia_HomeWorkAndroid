package com.example.nmedia

data class Post(
    val id:Int,
    val title: String,
    val date:String,
    val content:String,
    var likes:Int,
    var shares:Int,
    var shows:Int,
    var isLiked: Boolean = false
)