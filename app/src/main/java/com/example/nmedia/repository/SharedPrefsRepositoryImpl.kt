package com.example.nmedia.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//
//class SharedPrefsRepositoryImpl(context: Context) : PostRepository {
//    private val gson = Gson()
//    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
//    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
//    private val key = "posts"
//    private var posts = mutableListOf<Post>()
//    private val data = MutableLiveData(posts)
//
//    init {
//        prefs.getString(key, null)?.let {
//            posts = gson.fromJson(it, type)
//            data.value = posts
//        }
//    }
//
//    override fun getData(): MutableLiveData<MutableList<Post>> = data
//
//
//    override fun like(id: Int) {
//        val like = if (!posts[id].isLiked) posts[id].likes + 1 else posts[id].likes - 1
//        val post = posts[id].copy(isLiked = !posts[id].isLiked, likes = like)
//        posts[id] = post
//        sync()
//        updateLiveData()
//    }
//
//    override fun share(id: Int) {
//        posts[id] = posts[id].copy(shares = posts[id].shares + 1)
//        sync()
//        updateLiveData()
//    }
//
//    override fun remove(id: Int) {
//        posts = posts
//            .filter { it.id != id }
//            .mapIndexed { index, post -> post.copy(id = index) }.toMutableList()
//        sync()
//        updateLiveData()
//    }
//
//    override fun savePost(post: Post) {
//        if (post.id == -1) {
//            posts = (mutableListOf(post.copy()) + posts).toMutableList()
//            posts = posts.mapIndexed { index, postItem -> postItem.copy(id = index) }.toMutableList()
//        } else {
//            posts = posts.mapIndexed { index, itemPost ->
//                if (post.id != itemPost.id) itemPost.copy(id = index) else itemPost.copy(
//                    id = index,
//                    content = post.content
//                )
//            } as MutableList<Post>
//        }
//        sync()
//        updateLiveData()
//    }
//
//    private fun sync(){
//        with(prefs.edit()){
//            putString(key ,gson.toJson(posts))
//            apply()
//        }
//    }
//
//    private fun updateLiveData() {
//        data.value = posts
//    }
//}