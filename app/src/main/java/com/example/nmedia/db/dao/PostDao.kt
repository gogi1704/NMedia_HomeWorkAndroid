package com.example.nmedia.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface PostDao {
//    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
//    fun getAll(): LiveData<List<PostEntity>>
//
//    @Insert
//    fun insert(post: PostEntity)
//
//    @Query("UPDATE PostEntity SET content = :content WHERE id = :id ")
//    fun updatePostById(id: Int, content: String)
//
//    fun save(post: PostEntity) {
//        if (post.id == 0) {
//            insert(post)
//        } else {
//            updatePostById(post.id, post.content)
//        }
//
//    }
//
//    @Query(
//        """
//        DELETE FROM PostEntity WHERE id = :id
//    """
//    )
//    fun delete(id: Int)
//
//    @Query(
//        """
//               UPDATE PostEntity SET
//               likes = likes + CASE WHEN isLiked THEN -1 ELSE + 1 END ,
//               isLiked = CASE WHEN isLiked THEN 0 ELSE 1 END
//               WHERE id = :id;
//           """
//    )
//    fun like(id: Int)
//
//    @Query(
//        """
//           UPDATE PostEntity SET
//           shares =  shares + 1 WHERE id = :id
//       """
//    )
//    fun share(id: Int)
}