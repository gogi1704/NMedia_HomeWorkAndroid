package com.example.nmedia.repository

import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository

class InMemoryPostRepositoryImpl : PostRepository {
    private var posts =
        mutableListOf(
            Post(0, "Netology", "22.04.2022", " TRA TA TA TA Ta", 12, 80, 999_999),
            Post(1, "Anatomy", "22.04.2022", " Homo Sapiens", 12, 80, 999_999),
            Post(2, "Biology", "22.04.2022", "Butterfly's ", 12, 80, 999_999)
        )
    private var data = MutableLiveData(posts)


    override fun getData(): MutableLiveData<MutableList<Post>> {
        return data
    }

    override fun like(id: Int) {
        val like = if (!posts[id].isLiked) posts[id].likes + 1 else posts[id].likes - 1
        posts[id] = posts[id].copy(isLiked = !posts[id].isLiked, likes = like)
        update()
    }

    override fun share(id: Int) {
        posts[id] = posts[id].copy(shares = posts[id].shares + 1)
        update()

    }


    private fun update() {
        data.value = posts
    }
}