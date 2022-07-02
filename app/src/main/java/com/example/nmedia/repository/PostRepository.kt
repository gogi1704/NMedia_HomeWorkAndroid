package com.example.nmedia.repository

import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post

interface PostRepository {
    fun getData():MutableLiveData<Post>
    fun like()
    fun share()

}