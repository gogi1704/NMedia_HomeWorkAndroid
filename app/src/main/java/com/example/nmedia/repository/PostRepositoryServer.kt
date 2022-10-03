package com.example.nmedia.repository

import com.example.nmedia.BASE_URL
import com.example.nmedia.METHOD_DELETE
import com.example.nmedia.METHOD_POST
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class PostRepositoryServer {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private var gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val contentTypePost = "application/json".toMediaType()


    fun getDataFromServer(callback: PostRepository.GetAllCallback) {
        val request = Request.Builder()
            .url("$BASE_URL/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    fun savePost(post: Post) {

        val request = Request.Builder()
            .url("$BASE_URL/api/slow/posts")
            .post(gson.toJson(post).toRequestBody(contentTypePost))
            .build()

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                }

                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }

            })


    }

    fun like(id: Int, isLiked: Boolean) {
        if (isLiked) {
            createRequestByLike(METHOD_DELETE, id)
        } else {
            createRequestByLike(METHOD_POST, id)
        }
    }

    fun share(id: Int) {}

    fun remove(id: Int) {}

    private fun createRequestByLike(method: String, id: Int) {
        val request = Request.Builder()
            .url("$BASE_URL/api/posts/$id/likes")
            .method(method = method, "null".toRequestBody())
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                }

                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }


            })

    }
}