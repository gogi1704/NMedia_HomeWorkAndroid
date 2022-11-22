package com.example.nmedia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nmedia.db.dao.PostDao


@Database(entities = [PostEntity::class] , version = 1)
abstract class AppDb():RoomDatabase() {
    abstract fun postDao(): PostDao

}