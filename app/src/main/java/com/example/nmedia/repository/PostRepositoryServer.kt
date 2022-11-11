package com.example.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.nmedia.db.PostEntity
import com.example.nmedia.db.dao.PostDao
import com.example.nmedia.db.toDto
import com.example.nmedia.db.toEntity
import com.example.nmedia.exceptions.ApiError
import com.example.nmedia.exceptions.AppError
import com.example.nmedia.exceptions.NetworkError
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okio.IOException


class PostRepositoryServer(private val dao: PostDao) : PostRepository {
    override val data = dao.getAllChecked()
        .map { it.toDto() }
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = PostApiServiceHolder.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            var body = response.body() ?: throw ApiError(response.code(), response.message())
            body = body.map {
                it.copy(isSendToServer = true , isChecked = true)
            }
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw   NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000)
            val response = PostApiServiceHolder.service.getAllNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.map {
                PostEntity.fromDto(it.copy(isSendToServer = true))
            })
            emit(body.size)
        }
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)


    override suspend fun like(id: Long, isLiked: Boolean) {
        try {
            dao.like(id)
            if (!isLiked) {
                val response = PostApiServiceHolder.service.like(id)
                if (!response.isSuccessful) {
                    dao.like(id)
                    throw ApiError(response.code(), response.message())
                }

            } else {
                val response = PostApiServiceHolder.service.disLike(id)
                if (!response.isSuccessful) {
                    dao.like(id)
                    throw ApiError(response.code(), response.message())
                }
            }
        } catch (e: IOException) {
            dao.like(id)
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override suspend fun remove(id: Long) {
        val post = data.asLiveData(Dispatchers.Default).value?.last() {
            it.id == id
        }
        try {
            dao.delete(id)
            val response = PostApiServiceHolder.service.deletePost(id)
            if (!response.isSuccessful) {
                dao.insert(PostEntity.fromDto(post!!))
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            dao.insert(PostEntity.fromDto(post!!))
            throw  NetworkError()
        } catch (e: Exception) {
            dao.insert(PostEntity.fromDto(post!!))
            throw UnknownError()
        }

    }


    override suspend fun savePost(post: Post) {
        try {
            dao.insert(PostEntity.fromDto(post))
            val response = PostApiServiceHolder.service.savePost(post.copy(id = 0))
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }else {
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.delete(post.id)
                dao.insert(PostEntity.fromDto(body.copy(isSendToServer = true , isChecked = true)))
            }

        } catch (e: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }

    }

    suspend fun checkAllPosts() {
        val list = dao.getAll().filter {
           !it.isChecked
        }
        dao.insert(list.map { it.copy(isChecked = true) })
    }

    override fun share(id: Long) {}


}