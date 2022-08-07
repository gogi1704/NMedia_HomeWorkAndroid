package com.example.nmedia

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.nmedia.db.PostsDbHelper
import com.example.nmedia.db.PostsDbHelper.PostColumns.DATA_BASE_NAME
import com.example.nmedia.db.dao.PostDaoImpl

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDb(context)
                ).also { instance = it }
            }
        }

        private fun buildDb(context: Context) =
            PostsDbHelper(context, DATA_BASE_NAME, 1).writableDatabase
    }
}