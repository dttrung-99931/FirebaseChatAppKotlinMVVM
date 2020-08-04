package com.example.firebasechatappkotlinmvvm.data.remote.send_notification

import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName


/**
 * Created by Trung on 8/2/2020
 */
data class NotificationModel(
    @SerializedName("to") val receiverToken: String,
    @SerializedName("data") val data: NotificationData
)

data class NotificationData(
    val senderUserId: String,
    var senderNickname: String,
    var senderAvatarUrl: String,
    val msgType: String,
    var msgContent: String
){
    companion object{

        const val FIELD_SENDER_USER_ID = "senderUserId"
        const val FIELD_SENDER_NICKNAME = "senderNickname"
        const val FIELD_SENDER_AVATAR_URL = "senderAvatarUrl"
        const val FIELD_MSG_TYPE = "msgType"
        const val FIELD_MSG_CONTENT = "msgContent"
    }
}