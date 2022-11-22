package com.example.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.auth.AuthState
import com.example.nmedia.model.Media
import com.example.nmedia.model.MediaUpload
import com.example.nmedia.model.Post
import kotlinx.coroutines.flow.Flow
import okhttp3.Callback

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun like(id: Long, isLiked: Boolean)
    fun share(id: Long)
    suspend fun remove(id: Long)
    suspend fun savePost(post: Post)
    suspend fun saveWithAttachments(post: Post, upload: MediaUpload)
    suspend fun getAll()
    suspend fun upLoad(upload: MediaUpload): Media
    suspend fun updateUser(login: String, pass: String): AuthState
    suspend fun registerUser(login: String, pass: String, name: String): AuthState

    suspend fun checkAllPosts()
    fun getNewerCount(id: Long): Flow<Int>


}