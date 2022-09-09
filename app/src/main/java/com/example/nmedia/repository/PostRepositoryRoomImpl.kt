package com.example.nmedia.repository


import androidx.lifecycle.Transformations
import com.example.nmedia.db.PostEntity
import com.example.nmedia.db.dao.PostDao
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private var gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val contentType = "application/json".toMediaType()

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9990"
        private const val METHOD_DELETE = "DELETE"
        private const val METHOD_POST = "POST"
    }

    override fun getData() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(
                id = it.id,
                author = it.title,
                content = it.content,
                published = it.date.toInt(),
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
                gson.fromJson(it, typeToken.type)
            }

    }


    override fun like(id: Int, isLiked: Boolean) {
        if (isLiked) {
            createRequestByLike(METHOD_DELETE, id)
        } else {
            createRequestByLike(METHOD_POST, id)
        }

        dao.like(id)
    }

    override fun share(id: Int) {
        dao.share(id)
    }

    override fun remove(id: Int) {
        dao.delete(id)
    }


    override fun savePost(post: Post) {

        val request = Request.Builder()
            .url("$BASE_URL/api/posts")
            .post(gson.toJson(post).toRequestBody(contentType))
            .build()

        client.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: throw RuntimeException("Body is null")
            }


        dao.save(
            PostEntity(
                id = post.id,
                title = post.author,
                date = post.published.toString(),
                content = post.content,
                likes = post.likes,
                shares = post.shares,
                shows = post.shows,
                videoUri = post.videoUri,
                isLiked = post.likedByMe
            )
        )


    }

    private fun createRequestByLike(method: String, id: Int) {
        val request = Request.Builder()
            .url("$BASE_URL/api/slow/posts/$id/likes")
            .method(method = method, "null".toRequestBody())
            .build()
        client.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: throw RuntimeException("Body is null")
            }
    }
}