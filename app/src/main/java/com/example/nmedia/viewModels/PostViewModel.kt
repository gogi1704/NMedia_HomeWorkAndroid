package com.example.nmedia.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post
import com.example.nmedia.repository.SharedPrefsRepositoryImpl


val emptyPost = Post(
    id = -1,
    title = "",
    content = "",
    date = "",
    likes = 0,
    shares = 0,
    shows = 0,
    isLiked = false,
    videoUri = null

)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SharedPrefsRepositoryImpl(application)
    var data = repository.getData()
    val editedLiveData = MutableLiveData(emptyPost)

    fun like(id: Int) = repository.like(id)
    fun share(id: Int) = repository.share(id)
    fun remove(id: Int) = repository.remove(id)

    fun savePost() {
        editedLiveData.value?.let {
            repository.savePost(it)
        }
        editedLiveData.value = emptyPost
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
                editedLiveData.value = editedLiveData.value?.copy(content = trimContent )
            }
        }
    }
}

