package com.example.nmedia.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nmedia.db.PostEntity
import com.example.nmedia.model.Post


@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
     fun getAll(): LiveData<List<PostEntity>>

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