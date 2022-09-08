package com.example.nmedia.repository

import android.util.Log
import androidx.lifecycle.Transformations
import com.example.nmedia.db.PostEntity
import com.example.nmedia.db.dao.PostDao
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private var gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
       private const val BASE_URL = "http://10.0.2.2:9990"
    }

    override fun getData() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(
                id = it.id,
                author = it.title,
                content = it.content,
                published = it.date,
                likes = it.likes,
                shares = it.shares,
                shows = it.shows,
                videoUri = it.videoUri,
                likedByMe = it.isLiked
            )
        }

    }

     override fun getDataServer(): List<Post> {
        val request = Request.Builder()
            .url("$BASE_URL/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("Body is null") }
            .let {
                gson.fromJson(it, typeToken.type) }

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
                title = post.author,
                date = post.published,
                content = post.content,
                likes = post.likes,
                shares = post.shares,
                shows = post.shows,
                videoUri = post.videoUri,
                isLiked = post.likedByMe
            )
        )


    }
}