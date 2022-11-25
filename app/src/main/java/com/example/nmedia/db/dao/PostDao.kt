package com.example.nmedia.db.dao

import android.content.ClipData
import androidx.room.*
import com.example.nmedia.db.PostEntity
import com.example.nmedia.model.Post
import kotlinx.coroutines.flow.Flow


@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    suspend fun getAll(): List<PostEntity>

    @Query("SELECT * FROM PostEntity ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getPagedList(limit: Int, offset: Int): List<PostEntity>

    @Query("SELECT * FROM PostEntity WHERE isChecked = 1  ORDER BY id DESC ")
    fun getAllChecked(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id ")
    suspend fun updatePostById(id: Long, content: String)


    @Query(
        """
        DELETE FROM PostEntity WHERE id = :id
    """
    )
    suspend fun delete(id: Long)

    @Query(
        """
               UPDATE PostEntity SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE + 1 END ,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
               WHERE id = :id;
           """
    )
    suspend fun like(id: Long)

    @Query(
        """
           UPDATE PostEntity SET
           shares =  shares + 1 WHERE id = :id
       """
    )
    fun share(id: Int)
}
