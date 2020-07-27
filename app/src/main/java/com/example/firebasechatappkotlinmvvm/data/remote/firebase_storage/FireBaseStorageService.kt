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
        uploadAvatarCallBack: CallBack<String, String>
    )

    fun getAvatarUrl(uid: String?, gatAvatarUrlCallBack: CallBack<String, String>)

    fun uploadMsgImg(messageInfoProvider: MessageInfoProvider, callBack: CallBack<String, String>)
}