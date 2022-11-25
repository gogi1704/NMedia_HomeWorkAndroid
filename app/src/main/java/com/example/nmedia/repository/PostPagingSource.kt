package com.example.nmedia.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nmedia.db.dao.PostDao
import com.example.nmedia.db.toDto
import com.example.nmedia.exceptions.ApiError
import com.example.nmedia.model.Post
import kotlinx.coroutines.delay
import javax.inject.Inject

//
//class PostPagingSource @Inject constructor(private val apiService: PostsRetrofitService) :
//    PagingSource<Long, Post>() {
//    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null
//
//    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
//        try {
//
//
//            val response = when (params) {
//                is LoadParams.Refresh -> apiService.getLatest(params.loadSize)
//                is LoadParams.Append -> apiService.getBefore(
//                    id = params.key,
//                    count = params.loadSize
//                )
//                is LoadParams.Prepend -> return LoadResult.Page(
//                    data = emptyList(),
//                    prevKey = params.key,
//                    nextKey = null
//                )
//            }
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//
//            val nextKey = if (body.isEmpty()) null else body.last().id
//            return LoadResult.Page(
//                data = body, prevKey = params.key, nextKey = nextKey
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//}

class PostPagingSource @Inject constructor(private val dao: PostDao) :
    PagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val page = params.key ?: 0
            if (page != 0) delay(4000)
            val entities = dao.getPagedList(params.loadSize, page * params.loadSize)
            LoadResult.Page(
                entities.toDto(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}