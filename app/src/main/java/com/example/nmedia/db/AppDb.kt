package com.example.nmedia.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nmedia.DATA_BASE_NAME
import com.example.nmedia.db.dao.PostDao


@Database(entities = [PostEntity::class] , version = 1)
abstract class AppDb():RoomDatabase() {
    abstract val postDao: PostDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDb(context).also { instance = it }
            }
        }

        private fun buildDb(context: Context) =
           Room.databaseBuilder(context , AppDb::class.java , DATA_BASE_NAME)
               .allowMainThreadQueries()
               .fallbackToDestructiveMigration()
               .build()
    }
}