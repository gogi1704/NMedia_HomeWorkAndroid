package com.example.nmedia.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(

    val id: Long,
    val author: String,
    val published: Long,
    val content: String,
    val authorAvatar:String = "",
    val likes: Long = 0,
    val shares: Long,
    val shows: Long,
    val attachment: Attachment? = null,
    var likedByMe: Boolean = false,
    var isSendToServer : Boolean = false,
    var isChecked : Boolean = false

) : Parcelable {

    fun showCounts(num: Long): String {
        val str: String = if (num < 1000) {
            "$num"
        } else if (num in 1000..9999) {
            val result = num / 1000
            val result2 = num / 100 % 10
            "${result}.${result2}K"
        } else if (num in 10_000..1_000_000) {
            val result = num / 1000
            "${result}K"
        } else {
            val result = num / 1_000_000
            val result2 = result % 10
            "${result}.${result2}M"
        }

        return str
    }
}
