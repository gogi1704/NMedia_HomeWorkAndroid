package com.example.nmedia.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nmedia.model.Attachment
import com.example.nmedia.model.AttachmentType
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
    val likedByMe: Boolean = false,
    val isSendToServer : Boolean = false,
    var isChecked : Boolean = false,
    @Embedded
     val attachments: AttachmentEmbeddable? = null,



){
    fun toDto() = Post(id, author, published ,content,authorAvatar,likes ,shares , shows,likedByMe , isSendToServer ,isChecked , attachments?.toDto())

    companion object {
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.author, dto.published, dto.content, dto.authorAvatar, dto.likes, dto.shares , dto.shows , dto.likedByMe,dto.isSendToServer ,dto.isChecked , AttachmentEmbeddable.fromDto(dto.attachment))

    }
}

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url , type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}



fun List<PostEntity>.toDto(): List<Post> = map { it.toDto() }
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)