package com.example.nmedia.viewModels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.AppDb
import com.example.nmedia.DEFAULT_VALUE
import com.example.nmedia.DRAFT
import com.example.nmedia.model.Post
import com.example.nmedia.repository.PostRepositorySQLImpl


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
    private val sharedPrefDraft  = application.getSharedPreferences("draft" , MODE_PRIVATE)
    private val sharedPrefEditor = sharedPrefDraft.edit()

    private val repository = PostRepositorySQLImpl(AppDb.getInstance(application).postDao)
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

    fun edit(post: Post) {
        editedLiveData.value = post
    }


    fun editContent(content: String) {
        editedLiveData.value?.let {
            val trimContent = content.trim()
            if (trimContent == it.content) {
                return
            } else {
                editedLiveData.value = editedLiveData.value?.copy(content = trimContent)
            }
        }
    }

    fun putSharedPref(string: String){
        sharedPrefEditor.putString(DRAFT, string)
        sharedPrefEditor.commit()
    }

    fun getSharedPref():String{
        return sharedPrefDraft.getString(DRAFT , DEFAULT_VALUE).toString()
    }

    fun clearSharedPref(){
        sharedPrefEditor.clear()
        sharedPrefEditor.commit()
    }

}

