package com.example.nmedia.api


import com.example.nmedia.auth.AuthState
import com.example.nmedia.model.Media
import com.example.nmedia.model.Post
import com.example.nmedia.service.PushToken
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*



interface PostsRetrofitService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getAllNewer(@Path("id") id: Long): Response<List<Post>>

    @POST("posts")
    suspend fun savePost(@Body post: Post): Response<Post>

    @POST("users/push-tokens")
    suspend fun sendPushToken(@Body token: PushToken): Response<Unit>

    @Multipart
    @POST("media")
    suspend fun upLoadImage(@Part media: MultipartBody.Part): Response<Media>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun like(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun disLike(@Path("id") id: Long): Response<Post>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun sigInUser(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<AuthState>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("pass") pass: String,
        @Field("name") name: String
    ): Response<AuthState>


}

