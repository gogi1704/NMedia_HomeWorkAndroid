package com.example.nmedia.repository

import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostRepositoryServer() {

    fun getDataFromServer(callback: PostRepository.GetAllCallback<List<Post>>) {
        PostApiServiceHolder.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() ?: run {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })


    }

    fun savePost(post: Post, callback: PostRepository.GetAllCallback<Unit>) {

        PostApiServiceHolder.service.savePost(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                    } else callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }


    fun like(id: Int, isLiked: Boolean, callback: PostRepository.GetAllCallback<Post>) {
        if (isLiked) {
            PostApiServiceHolder.service.disLike(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                        } else {
                            val body = response.body() ?: throw (RuntimeException(""))
                            callback.onSuccess(body)
                        }
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException(t))
                    }
                })

        } else {
            PostApiServiceHolder.service.like(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                        } else {
                            val body = response.body() ?: throw (RuntimeException(""))
                            callback.onSuccess(body)
                        }
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException(t))
                    }
                })


        }
    }

    fun share(id: Int) {}

    fun remove(id: Int, callback: PostRepository.GetAllCallback<Unit>) {
        PostApiServiceHolder.service.deletePost(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                    } else callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

}