package com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.MessageInfoProvider
import java.io.InputStream


/**
 * Created by Trung on 7/10/2020
 */
interface FireBaseStorageService {
    fun uploadAvatar(
        uid: String,
        avatarInputStream: InputStream?,
        resultCallBack: CallBack<String, String>
    )

    fun getAvatarUrl(uid: String?, resultCallBack: CallBack<String, String>)

    fun uploadMsgImg(
        messageInfoProvider: MessageInfoProvider,
        resultCallBack: CallBack<String, String>
    )
}