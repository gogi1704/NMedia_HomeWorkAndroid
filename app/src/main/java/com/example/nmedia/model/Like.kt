package com.example.nmedia.model

data class Like(
    val userId: String,
    val userName: String,
    val postId: Int,
    val postAuthor: String
)
