package com.example.firebasechatappkotlinmvvm.data.remote.send_notification

import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.google.gson.annotations.SerializedName


/**
 * Created by Trung on 8/2/2020
 */
class NotificationModel(
    @SerializedName("to") val receiverToken: String,
    @SerializedName("data") val msg: Messagee
) {
}