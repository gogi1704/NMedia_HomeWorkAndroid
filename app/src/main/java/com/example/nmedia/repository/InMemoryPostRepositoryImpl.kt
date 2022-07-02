package com.example.nmedia.repository

import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post

class InMemoryPostRepositoryImpl : PostRepository {
    private var post = Post(0, "Netology", "22.04.2022", " TRA TA TA TA Ta", 12, 80, 999_999)
    private val data = MutableLiveData(post)

    override fun getData(): MutableLiveData<Post> {
        return data
    }

    override fun like() {
        val like = if (!post.isLiked) post.likes+1 else post.likes-1
        post = post.copy(isLiked = !post.isLiked , likes = like)
        update()
    }

    override fun share() {
        post = post.copy(shares = post.shares + 1)
        update()

    }


    private fun update(){
        data.value = post
    }
}