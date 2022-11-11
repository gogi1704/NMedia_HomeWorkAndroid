package com.example.nmedia.viewModels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.*
import com.example.nmedia.*
import com.example.nmedia.db.AppDb
import com.example.nmedia.model.Post
import com.example.nmedia.repository.PostRepositoryServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState


val emptyPost = Post(
    id = 0,
    author = "Netology",
    published = 12442341L,
    content = "content",
    authorAvatar = "netology.jpg",
    likes = 0,
    shares = 0,
    shows = 0,
    likedByMe = false,
    attachment = null,
    isSendToServer = false,
    isChecked = true

)
var countErrorPost = -1L

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefDraft = application.getSharedPreferences("draft", MODE_PRIVATE)
    private val sharedPrefEditor = sharedPrefDraft.edit()
    private val repository = PostRepositoryServer(AppDb.getInstance(application).postDao)

    private val checkError = false
    val errorCreateFragmentLiveData = MutableLiveData(checkError)


    private val _data: LiveData<FeedModel> = repository.data
        .map {
            FeedModel(posts = it.filter { post -> !post.isSendToServer } + it.filter { post -> post.isSendToServer })
        }
        .catch { e ->
            e.printStackTrace()
        }
        .asLiveData(Dispatchers.Default)

    val data: LiveData<FeedModel>
        get() = _data

    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { }
            .asLiveData(Dispatchers.Default)
    }


    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    val editedLiveData = MutableLiveData(emptyPost)


    init {
        loadPost()
    }

    fun loadPost() {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                repository.getAll()
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(errorType = ERROR_LOAD)
            }
        }

    }


    fun like(id: Long, isLiked: Boolean) {

        viewModelScope.launch {
            try {
                repository.like(id, isLiked)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(errorType = ERROR_LIKE)
            }
        }


    }

    fun share(id: Long) = repository.share(id)

    fun remove(id: Long) {

        viewModelScope.launch {
            try {
                repository.remove(id)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(errorType = ERROR_REMOVE)
            }
        }
    }

    fun checkPosts() = viewModelScope.launch { repository.checkAllPosts() }


    fun savePost() {
        viewModelScope.launch {
            try {
                _postCreated.postValue(Unit)
                editedLiveData.value?.let { repository.savePost(it.copy(id = countErrorPost)) }
            } catch (e: Exception) {
                _dataState.value = FeedModelState(errorType = ERROR_SAVE)
            }
            clearSharedPref()
        }
        countErrorPost--
        editedLiveData.value = emptyPost
        countErrorPost--
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
                editedLiveData.value =
                    editedLiveData.value?.copy(
                        content = trimContent,
                        id = countErrorPost,
                        isChecked = true
                    )
            }
        }
    }

    fun savePostAfterError(post: Post) {
        viewModelScope.launch {
            try {
                repository.savePost(post)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(errorType = ERROR_REPEAT_REQUEST)

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

    fun getPostById(id: Long): Post {
        val listPosts = data.value?.posts?.filter {
            it.id == id
        }
        return listPosts!![0]
    }


    private fun likePost(id: Long) {
        val listPost = _data.value?.posts?.toMutableList()
        val filteredList = _data.value?.posts?.filter {
            it.id == id
        }
        var post = filteredList?.get(0)
        val index = listPost?.indexOf(post)
        post = if (post?.likedByMe == false) {
            post.copy(likes = post.likes + 1, likedByMe = !post.likedByMe)
        } else {
            post?.copy(likes = post.likes - 1, likedByMe = !post.likedByMe)
        }

        listPost?.set(index!!, post!!)
        listPost as List<Post>
    }


}

