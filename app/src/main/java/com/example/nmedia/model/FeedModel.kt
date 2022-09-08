package ru.netology.nmedia.model

import com.example.nmedia.model.Post


data class FeedModel(
    val posts:List<Post> = emptyList(),
    val isEmpty:Boolean = false,
    val loading:Boolean= false,
    val error:Boolean= false,
)
