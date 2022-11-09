package ru.netology.nmedia.model

import com.example.nmedia.model.Post


data class FeedModel(
    var posts:List<Post> = emptyList(),
    val isEmpty:Boolean = false,

)

data class FeedModelState(
    val loading:Boolean= false,
    val errorType:String= "null",
    val refreshing: Boolean = false
)
