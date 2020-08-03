package com.example.firebasechatappkotlinmvvm.data.remote.send_notification

import com.example.firebasechatappkotlinmvvm.util.AppConstants
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * Created by Trung on 8/2/2020
 */
interface NotificationSenderService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=" + AppConstants.KEY_MSG_SERVER
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: NotificationModel): retrofit2.Call<Response>
}
