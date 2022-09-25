package com.example.nmedia.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post
import okhttp3.Callback

interface PostRepository {
    fun getData(): LiveData<List<Post>>
    fun like(id: Int, isLiked: Boolean)
    fun share(id: Int)
    fun remove(id: Int)
    fun savePost(post: Post)
   // fun getDataServer(): List<Post>
    fun getDataFromServer(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

}