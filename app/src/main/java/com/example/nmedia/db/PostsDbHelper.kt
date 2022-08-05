package com.example.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PostsDbHelper(
    context: Context,
    dbName: String,
    dbVersion: Int,
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME" +
                    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT NOT NULL ," +
                    "$COLUMN_CONTENT TEXT ," +
                    "$COLUMN_DATE TEXT NOT NULL ," +
                    "$COLUMN_LIKES INTEGER NOT NULL DEFAULT 0," +
                    "$COLUMN_SHARES INTEGER NOT NULL DEFAULT 0 ," +
                    "$COLUMN_SHOWS INTEGER NOT NULL DEFAULT 0 ," +
                    "$COLUMN_VIDEO_URI TEXT ," +
                    "$COLUMN_IS_LIKED BOOlEAN NOT NULL DEFAULT 0 )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    companion object PostColumns {
        const val DATA_BASE_NAME = "POSTS_DATA_BASE"
        const val TABLE_NAME = "Posts"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "TITLE"
        const val COLUMN_CONTENT = "CONTENT"
        const val COLUMN_DATE = "DATE"
        const val COLUMN_LIKES = "LIKES"
        const val COLUMN_SHARES = "SHARES"
        const val COLUMN_SHOWS = "SHOWS"
        const val COLUMN_VIDEO_URI = "VIDEO_URI"
        const val COLUMN_IS_LIKED = "IS_LIKED"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_TITLE,
            COLUMN_CONTENT,
            COLUMN_DATE,
            COLUMN_LIKES,
            COLUMN_SHARES,
            COLUMN_SHOWS,
            COLUMN_VIDEO_URI,
            COLUMN_IS_LIKED
        )

    }
}
