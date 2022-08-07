package com.example.nmedia.db.dao

import com.example.nmedia.model.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun delete(id: Int)
    fun like(id: Int)
    fun share(id: Int)
}