package com.example.nmedia.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post
import kotlinx.coroutines.flow.Flow
import okhttp3.Callback

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun like(id: Long, isLiked: Boolean)
    fun share(id: Long)
    suspend fun remove(id: Long)
    suspend fun savePost(post: Post)
    suspend fun getAll()
    fun getNewerCount(id: Long):Flow<Int>

}