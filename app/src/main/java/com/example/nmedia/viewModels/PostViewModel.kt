package com.example.nmedia.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nmedia.model.Post
import com.example.nmedia.repository.InMemoryPostRepositoryImpl
import kotlin.random.Random


val emptyPost = Post(
    id = 0,
    title = "",
    content = "",
    date = "",
    likes = 0,
    shares = 0,
    shows = 0,
    isLiked = false

)

class PostViewModel : ViewModel() {
    private val repository = InMemoryPostRepositoryImpl()
    var data = repository.getData()
    val editedLiveData = MutableLiveData(emptyPost)

    fun like(id: Int) = repository.like(id)
    fun share(id: Int) = repository.share(id)
    fun remove(id: Int) = repository.remove(id)

    fun savePost() {
        editedLiveData.value?.let {
            repository.savePost(it)
        }
    }

    fun edit(post:Post) {
    editedLiveData.value = post

    }


    fun editContent(content: String) {
        editedLiveData.value?.let {
            val trimContent = content.trim()
            if (trimContent == it.content) {
                return
            }else{
                editedLiveData.value = emptyPost.copy(content = trimContent )
            }
        }
    }
}

