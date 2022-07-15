package com.example.nmedia.repository

import androidx.lifecycle.MutableLiveData
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository

class InMemoryPostRepositoryImpl : PostRepository {

    private var posts =
        mutableListOf(
            Post(
                0,
                "Anatomy",
                "22.04.2022",
                "WeatherAPI.com interactive API explorer",
                12,
                80,
                999_999
            ),
            Post(
                1,
                "Biology",
                "22.04.2022",
                "IO Docs allows you to test our APIs and method",
                12,
                80,
                999_999
            ),
            Post(
                2,
                "Biology",
                "22.04.2022",
                "and methods. It returns response he",
                12,
                80,
                999_999
            ),
            Post(
                3,
                "Biology",
                "22.04.2022",
                "It returns response headers, response code and response body.",
                200,
                80,
                999_999
            ),
        )

    private var data = MutableLiveData(posts)

    override fun getData(): MutableLiveData<MutableList<Post>> {
        return data
    }

    override fun like(id: Int) {
        val like = if (!posts[id].isLiked) posts[id].likes + 1 else posts[id].likes - 1
        val post = posts[id].copy(isLiked = !posts[id].isLiked, likes = like)
        posts[id] = post
        updateLiveData()
    }

    override fun share(id: Int) {
        posts[id] = posts[id].copy(shares = posts[id].shares + 1)
        updateLiveData()
    }

    override fun remove(id: Int) {
        posts = posts.filter { it.id != id }
            .mapIndexed { index, post -> post.copy(id = index) }.toMutableList()

        updateLiveData()
    }

    override fun savePost(post: Post) {
        if (post.id == -1) {
            posts = (mutableListOf(post.copy()) + posts).toMutableList()
            posts = posts.mapIndexed { index, postItem -> postItem.copy(id = index) }.toMutableList()
        } else {
            posts = posts.mapIndexed { index, itemPost ->
                if (post.id != itemPost.id) itemPost.copy(id = index) else itemPost.copy(
                    id = index,
                    content = post.content
                )
            } as MutableList<Post>
        }
        updateLiveData()
    }

    private fun updateLiveData() {
        data.value = posts
    }
}