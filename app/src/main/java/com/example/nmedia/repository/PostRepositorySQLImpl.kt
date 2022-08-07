package com.example.nmedia.repository

import androidx.lifecycle.MutableLiveData
import com.example.nmedia.db.dao.PostDao
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository

class PostRepositorySQLImpl(private val dao: PostDao) : PostRepository {
    private var posts = listOf<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getData(): MutableLiveData<List<Post>> {
        return data
    }

    override fun like(id: Int) {
        dao.like(id)
        updateDb()
    }

    override fun share(id: Int) {
        dao.share(id)
        updateDb()
    }

    override fun remove(id: Int) {
        dao.delete(id)
        updateDb()
    }

    override fun savePost(post: Post) {
        val id = post.id
        val savedPost = dao.save(post)
        posts = if (id == -1) {
            listOf(savedPost) + posts
        } else {
            posts.map {
                if (it.id == id) it else savedPost
            }
        }
        updateDb()
    }

    private fun updateDb() {
        data.value = dao.getAll()
    }
}