package com.example.nmedia.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
):Parcelable


enum class AttachmentType{
    IMAGE,
    VIDEO
}
