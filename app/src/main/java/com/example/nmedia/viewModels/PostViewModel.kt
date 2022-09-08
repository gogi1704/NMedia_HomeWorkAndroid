package com.example.nmedia.viewModels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.db.AppDb
import com.example.nmedia.DEFAULT_VALUE
import com.example.nmedia.DRAFT
import com.example.nmedia.model.Post
import com.example.nmedia.repository.PostRepositoryRoomImpl
import ru.netology.nmedia.model.FeedModel
import java.io.IOException
import kotlin.concurrent.thread


val emptyPost = Post(
    id = 0,
    author = "default title",
    content = "default content",
    published = "default date",
    likes = 0,
    shares = 0,
    shows = 0,
    likedByMe = false,
    videoUri = null

)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefDraft = application.getSharedPreferences("draft", MODE_PRIVATE)
    private val sharedPrefEditor = sharedPrefDraft.edit()


    private val repository: PostRepository =
        PostRepositoryRoomImpl(AppDb.getInstance(application).postDao)



    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

   // var data = repository.getData()
    val editedLiveData = MutableLiveData(emptyPost)


    fun loadPost(){
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getDataServer()
                _data.postValue(FeedModel(posts = posts , isEmpty = posts.isEmpty()))
                Log.d("my" , "viewModel${posts}")
            }catch (e:IOException){
                FeedModel(error = true)
            }.also { _data::postValue }


        }

    }

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


    fun putSharedPref(string: String) {
        sharedPrefEditor.putString(DRAFT, string)
        sharedPrefEditor.commit()
    }

    fun getSharedPref(): String {
        return sharedPrefDraft.getString(DRAFT, DEFAULT_VALUE).toString()
    }

    fun clearSharedPref() {
        sharedPrefEditor.clear()
        sharedPrefEditor.commit()
    }

    fun getPostById(id: Int): Post {
        val listPosts = data.value?.posts?.filter {
            it.id == id
        }
        return listPosts!![0]
    }


}

