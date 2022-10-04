package com.example.nmedia.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(

    val id: Int,
    val author: String,
    val published: Int,
    val content: String,
    val authorAvatar:String = "",
    val likes: Int = 0,
    val shares: Int,
    val shows: Int,
    val attachment: Attachment?,
    var likedByMe: Boolean = false
) : Parcelable {

    fun showCounts(num: Int): String {
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
