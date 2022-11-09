package com.example.nmedia.repository

import com.example.nmedia.BASE_URL
import com.example.nmedia.BuildConfig
import com.example.nmedia.model.Post
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .baseUrl(BASE_URL)
    .build()


interface PostsRetrofitService {
    @GET("posts")
    suspend fun getAll():Response< List<Post>>

    @POST("posts")
    suspend fun savePost(@Body post: Post):Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long):Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun like(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun disLike(@Path("id") id: Long): Response<Post>


}

object PostApiServiceHolder {
    val service: PostsRetrofitService by lazy {
        retrofit.create()
    }
}