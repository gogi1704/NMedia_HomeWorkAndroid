package com.example.nmedia.viewModels

import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post

interface PostRepository {
    fun getData():MutableLiveData<MutableList<Post>>
    fun like(id:Int)
    fun share(id:Int)
    fun remove(id: Int)
    fun savePost(post: Post)

}