package com.example.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.nmedia.R
import com.example.nmedia.model.Like
import com.example.nmedia.model.Post
import com.example.nmedia.model.Share
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.lang.IllegalArgumentException
import kotlin.random.Random

enum class Action {
    LIKE,
    SHARE,
    NEW_POST
}


const val channelId = "1"

class FirebaseNotificationService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My notification"
            val descriptionText = "descText"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        try {
            message.data[action]?.let {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(
                        gson.fromJson(
                            message.data[content],
                            Like::class.java
                        )
                    )
                    Action.SHARE -> handleShare(
                        gson.fromJson(
                            message.data[content],
                            Share::class.java
                        )
                    )
                    Action.NEW_POST -> handleNewPost(
                        gson.fromJson(
                            message.data[content],
                            Post::class.java
                        )
                    )
                }
            }
        } catch (e: IllegalArgumentException) {
            println("IllegalArgumentException")
            return
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println(token)
    }

    private fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(until = 1000), notification)
    }

    private fun handleShare(share: Share) {

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_shared,
                    share.userName,
                    share.postTitle
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(until = 1000), notification)
    }

    private fun handleNewPost(post: Post) {

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_new_post,
                    post.id.toString(),
                    post.author
                )
            )
            .setContentText(post.content)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(until = 10000), notification)
    }


}