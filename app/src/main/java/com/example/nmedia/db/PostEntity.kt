package com.example.nmedia.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nmedia.model.Attachment
import com.example.nmedia.model.Post


@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: Long,
    val content: String,
    val authorAvatar: String,
    val likes: Long = 0,
    val shares: Long,
    val shows: Long,
   // val attachments: Attachment? = null,
    val likedByMe: Boolean = false,
    val isSendToServer : Boolean = false,
    var isChecked : Boolean = false



){
    fun toDto() = Post(id, author, published ,content,authorAvatar,likes ,shares , shows, null,likedByMe , isSendToServer ,isChecked)

    companion object {
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.author, dto.published, dto.content, dto.authorAvatar, dto.likes, dto.shares , dto.shows , dto.likedByMe,dto.isSendToServer ,dto.isChecked)

    }
}



fun List<PostEntity>.toDto(): List<Post> = map { it.toDto() }
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)