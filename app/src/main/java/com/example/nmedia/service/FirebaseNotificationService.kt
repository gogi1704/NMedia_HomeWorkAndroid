package com.example.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.nmedia.R
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.model.Like
import com.example.nmedia.model.Post
import com.example.nmedia.model.Share
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.random.Random

enum class Action {
    LIKE,
    SHARE,
    NEW_POST
}

data class PushToken(
    val token: String
)

data class PushMessage(
    val recipientId: Long?,
    val content: String,
)


const val channelId = "1"

@AndroidEntryPoint
class FirebaseNotificationService () : FirebaseMessagingService() {

    @Inject
    lateinit var auth: AppAuth

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
        try {
            val inputPush = gson.fromJson(message.data[content], PushMessage::class.java)

            if (inputPush.recipientId == auth.authStateFlow.value.id || inputPush.recipientId == null) {
// default notification
                val push = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(inputPush.content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                NotificationManagerCompat.from(this)
                    .notify(1000, push)

//                message.data[action]?.let {
//                    when (Action.valueOf(it)) {
//                        Action.LIKE -> handleLike(
//                            gson.fromJson(
//                                message.data[content],
//                                Like::class.java
//                            )
//                        )
//                        Action.SHARE -> handleShare(
//                            gson.fromJson(
//                                message.data[content],
//                                Share::class.java
//                            )
//                        )
//                        Action.NEW_POST -> handleNewPost(
//                            gson.fromJson(
//                                message.data[content],
//                                Post::class.java
//                            )
//                        )
//
//                    }
//                }
            } else if (inputPush.recipientId == 0L
                || inputPush.recipientId != 0L
                && inputPush.recipientId == auth.authStateFlow.value.id
            ) {
                auth.sendPushToken()
            }


        } catch (e: IllegalArgumentException) {
            println("IllegalArgumentException")
            return
        }

    }

    override fun onNewToken(token: String) {
        auth.sendPushToken(token)
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