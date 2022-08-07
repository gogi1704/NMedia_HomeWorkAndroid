package com.example.nmedia.db.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.Transformations.map
import com.example.nmedia.db.PostsDbHelper.PostColumns.ALL_COLUMNS
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_CONTENT
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_DATE
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_ID
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_IS_LIKED
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_LIKES
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_SHARES
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_SHOWS
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_TITLE
import com.example.nmedia.db.PostsDbHelper.PostColumns.COLUMN_VIDEO_URI
import com.example.nmedia.db.PostsDbHelper.PostColumns.TABLE_NAME
import com.example.nmedia.model.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            TABLE_NAME,
            ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts

    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != -1) {
                put(COLUMN_ID, post.id)
            }

            put(COLUMN_TITLE, "post.title")
            put(COLUMN_CONTENT, post.content)
            put(COLUMN_DATE, " post.date")
            put(COLUMN_LIKES, post.likes)
            put(COLUMN_SHARES, post.shares)
            put(COLUMN_SHOWS, post.shows)
            put(COLUMN_VIDEO_URI, post.videoUri)
            put(COLUMN_IS_LIKED, post.isLiked)

        }
        val id = db.replace(TABLE_NAME, null, values)
        db.query(
            TABLE_NAME,
            ALL_COLUMNS,
            "$COLUMN_ID = ? ",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun delete(id: Int) {
        db.execSQL(
            """
            DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = $id;
        """.trimIndent()
        )
    }

    override fun like(id: Int) {
        db.execSQL(
            """
               UPDATE $TABLE_NAME SET
               $COLUMN_LIKES = $COLUMN_LIKES + CASE WHEN $COLUMN_IS_LIKED THEN -1 ELSE + 1 END ,
               $COLUMN_IS_LIKED = CASE WHEN $COLUMN_IS_LIKED THEN 0 ELSE 1 END
               WHERE $COLUMN_ID = ?; 
           """.trimIndent(), arrayOf(id)
        )
    }

    override fun share(id: Int) {
        db.execSQL(
            """
           UPDATE $TABLE_NAME SET
           $COLUMN_SHARES =  $COLUMN_SHARES + 1 WHERE $COLUMN_ID = $id;
       """.trimIndent()
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                title = getString(getColumnIndexOrThrow(COLUMN_TITLE)),
                content = getString(getColumnIndexOrThrow(COLUMN_CONTENT)),
                date = getString(getColumnIndexOrThrow(COLUMN_DATE)),
                likes = getInt(getColumnIndexOrThrow(COLUMN_LIKES)),
                shares = getInt(getColumnIndexOrThrow(COLUMN_SHARES)),
                shows = getInt(getColumnIndexOrThrow(COLUMN_SHOWS)),
                videoUri = getString(getColumnIndexOrThrow(COLUMN_VIDEO_URI)),
                isLiked = getInt(getColumnIndexOrThrow(COLUMN_IS_LIKED)) != 0
            )
        }

    }
}