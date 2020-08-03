package com.example.firebasechatappkotlinmvvm.service

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.di.App
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
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

    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        if (authService.getCurAuthUser() != null
            && (application as App).isAppNotRunning())
            sendNotification(msg.data)
    }

    private fun sendNotification(messagee: Map<String, String>) {
        val content = messagee[Messagee.FIELD_CONTENT]
        val senderNickname = messagee[Messagee.FIELD_SENDER_NICKNAME]
        val largeIconBitmap = BitmapFactory.decodeResource(
            resources, R.mipmap.we_chat_logo)

        val notiBuilder = NotificationCompat.Builder(this)
            .setContentTitle(senderNickname)
            .setContentText(content)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.mipmap.we_chat_logo)
            .setLargeIcon(largeIconBitmap)
            .setAutoCancel(true)

        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notiManager.notify(
            UUID.randomUUID().variant(),
            notiBuilder.build())
    }
}