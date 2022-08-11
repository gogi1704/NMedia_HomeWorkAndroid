package com.example.nmedia.repository

import androidx.lifecycle.Transformations
import com.example.nmedia.db.PostEntity
import com.example.nmedia.db.dao.PostDao
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {

    override fun getData() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(
                id = it.id,
                title = it.title,
                content = it.content,
                date = it.date,
                likes = it.likes,
                shares = it.shares,
                shows = it.shows,
                videoUri = it.videoUri,
                isLiked = it.isLiked
            )
        }

    }


    override fun like(id: Int) {
        dao.like(id)
    }

    override fun share(id: Int) {
        dao.share(id)
    }

    override fun remove(id: Int) {
        dao.delete(id)
    }

    override fun savePost(post: Post) {
        dao.save(
            PostEntity(
                id = post.id,
                title = post.title,
                date = post.date,
                content = post.content,
                likes = post.likes,
                shares = post.shares,
                shows = post.shows,
                videoUri = post.videoUri,
                isLiked = post.isLiked
            )
        )


    }
}