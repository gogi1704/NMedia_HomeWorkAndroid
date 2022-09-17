package com.example.nmedia.viewModels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.db.AppDb
import com.example.nmedia.DEFAULT_VALUE
import com.example.nmedia.DRAFT
import com.example.nmedia.SingleLiveEvent
import com.example.nmedia.model.Post
import com.example.nmedia.repository.PostRepositoryRoomImpl
import ru.netology.nmedia.model.FeedModel
import java.io.IOException
import kotlin.concurrent.thread


val emptyPost = Post(
    id = 0,
    author = "default title",
    content = "default content",
    published = 0,
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

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    val editedLiveData = MutableLiveData(emptyPost)


    fun loadPost() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getDataServer()
                _data.postValue(FeedModel(posts = posts, isEmpty = posts.isEmpty()))
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also { _data::postValue }


        }

    }

    fun like(id: Int, isLiked: Boolean) {
        thread {
            repository.like(id, isLiked)
            likePost(id)
        }


    }

    fun share(id: Int) = thread { repository.share(id) }
    fun remove(id: Int) = thread {
        repository.remove(id)
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id })
        )

        try {
            repository.remove(id)
        } catch (io: IOException) {
            _data.postValue(_data.value?.copy(posts = old))
        }
    }

    fun savePost() {
        editedLiveData.value?.let {
            thread {
                repository.savePost(it)
                _postCreated.postValue(Unit)
            }

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

    private fun likePost(id: Int) {
        val listPost = _data.value?.posts?.toMutableList()
        val filteredList = _data.value?.posts?.filter {
            it.id == id
        }
        var post = filteredList?.get(0)
        val index = listPost?.indexOf(post)
        post = if (post?.likedByMe == false) {
            post.copy(likes = post.likes + 1 , likedByMe = !post.likedByMe)
        } else {
            post?.copy(likes = post.likes - 1 ,  likedByMe = !post.likedByMe)
        }

        listPost?.set(index!!, post!!)
        listPost as List<Post>
        _data.postValue(_data.value?.copy(posts = listPost))
    }
}

