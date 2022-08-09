package com.example.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int ,
    val title: String ,
    val date: String,
    val content: String,
    val likes: Int = 0,
    val shares: Int,
    val shows: Int,
    val videoUri: String? = null,
    val isLiked: Boolean = false

)
