package com.example.firebasechatappkotlinmvvm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.send_notification.NotificationData
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.example.firebasechatappkotlinmvvm.di.App
import com.example.firebasechatappkotlinmvvm.ui.chat.ChatActivity
import com.example.firebasechatappkotlinmvvm.ui.chat.chat.ChatFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject


/**
 * Created by Trung on 8/2/2020
 */
class NotificationReceiverService: FirebaseMessagingService() {

    @Inject
    lateinit var authService: FireBaseAuthService

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(remoteMsg: RemoteMessage) {
        super.onMessageReceived(remoteMsg)
        if (authService.getCurAuthUser() != null
            && (application as App).isAppNotRunning())
            sendNotification(remoteMsg.data)
    }

    private fun sendNotification(data: Map<String, String>) {
        val msgContent = data[NotificationData.FIELD_MSG_CONTENT]
        val senderNickname = data[NotificationData.FIELD_SENDER_NICKNAME]
        val senderUserId = data[NotificationData.FIELD_SENDER_USER_ID]
        val senderAvatarUrl = data[NotificationData.FIELD_SENDER_AVATAR_URL]
        val largeIconBitmap = BitmapFactory.decodeResource(
            resources, R.mipmap.we_chat_logo)

        // Create a pending intent openning ChatActivity
        val intent = Intent(this, ChatActivity::class.java)
        val chatUser = ChatUser(senderUserId!!, senderNickname!!, senderAvatarUrl!!)
        val chatUserBundle = bundleOf(ChatFragment.KEY_CHAT_USER to chatUser)
        intent.putExtra(ChatActivity.KEY_CHAT_USER_BUNDLE, chatUserBundle)
        val chatPendingIntent = PendingIntent.getActivity(
            this, 0,
            intent, PendingIntent.FLAG_ONE_SHOT
        )

        val notiBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(senderNickname)
            .setContentText(msgContent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.mipmap.we_chat_logo)
            .setLargeIcon(largeIconBitmap)
            .setAutoCancel(true)
            .setContentIntent(chatPendingIntent)

        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChanel(notiManager)
        }

        notiManager.notify(
            UUID.randomUUID().variant(),
            notiBuilder.build())
    }

    companion object{
        const val CHANNEL_ID = "WeChatId"
        const val CHANNEL_NAME = "WeChat"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel(notiManager: NotificationManager) {
        val newMsgNotiChanel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val audioAttr = AudioAttributes
            .Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        newMsgNotiChanel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            audioAttr
        )
        newMsgNotiChanel.enableVibration(true)
        notiManager.createNotificationChannel(newMsgNotiChanel)
    }
}