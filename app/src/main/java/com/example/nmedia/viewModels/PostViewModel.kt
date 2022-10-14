package com.example.nmedia.viewModels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.DEFAULT_VALUE
import com.example.nmedia.DRAFT
import com.example.nmedia.SingleLiveEvent
import com.example.nmedia.model.Post
import com.example.nmedia.repository.PostRepositoryServer
import ru.netology.nmedia.model.FeedModel


val emptyPost = Post(
    id = 0,
    author = "Netology",
    content = "default content",
    authorAvatar = "netology.jpg",
    published = 0,
    likes = 0,
    shares = 0,
    shows = 0,
    likedByMe = false,
    attachment = null

)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefDraft = application.getSharedPreferences("draft", MODE_PRIVATE)
    private val sharedPrefEditor = sharedPrefDraft.edit()
    private val repository = PostRepositoryServer()

    private val checkError = false
    val errorCreateFragmentLiveData = MutableLiveData(checkError)


    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    val editedLiveData = MutableLiveData(emptyPost)


    fun loadPost() {

        _data.value = FeedModel(loading = true)
        repository.getDataFromServer(object : PostRepository.GetAllCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.value = FeedModel(posts = posts, isEmpty = posts.isEmpty())
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        })


    }


    fun like(id: Int, isLiked: Boolean) {
        val old = _data.value?.posts
        likePost(id)
        repository.like(id, isLiked, object : PostRepository.GetAllCallback<Post> {
            override fun onSuccess(posts: Post) {

            }

            override fun onError(e: Exception) {
                Toast.makeText(getApplication(), "Server not found. Try again", Toast.LENGTH_LONG).show()
                _data.postValue(_data.value?.copy(posts = old!!))
            }

        })

    }

    fun share(id: Int) = repository.share(id)

    fun remove(id: Int) {

        val old = _data.value?.posts.orEmpty()
        _data.value = _data.value?.copy(posts = _data.value?.posts.orEmpty()
            .filter { it.id != id })

        repository.remove(id, object : PostRepository.GetAllCallback<Unit> {

            override fun onSuccess(posts: Unit) {
                Toast.makeText(getApplication(), "Deleted successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Exception) {
                Toast.makeText(getApplication() , "Deletion error" , Toast.LENGTH_LONG).show()
                _data.postValue(_data.value?.copy(posts = old))

            }
        })


    }

    fun savePost() {
        editedLiveData.value?.let {
            repository.savePost(it, object : PostRepository.GetAllCallback<Unit> {
                override fun onSuccess(posts: Unit) {
                    clearSharedPref()
                }

                override fun onError(e: Exception) {
                    errorCreateFragmentLiveData.value = true

                }
            })

        }
        _postCreated.postValue(Unit)
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
            post.copy(likes = post.likes + 1, likedByMe = !post.likedByMe)
        } else {
            post?.copy(likes = post.likes - 1, likedByMe = !post.likedByMe)
        }

        listPost?.set(index!!, post!!)
        listPost as List<Post>
        _data.value = _data.value?.copy(posts = listPost)
    }

}

