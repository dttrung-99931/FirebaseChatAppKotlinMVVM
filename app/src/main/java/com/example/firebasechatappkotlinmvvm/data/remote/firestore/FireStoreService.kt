package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser


/**
 * Created by Trung on 7/10/2020
 */
interface FireStoreService {
    fun addUser(user: AppUser, callBack: CallBack<Unit, String>)
    fun checkUnavailableNickname(
        nickname: String?,
        availableNicknameCallBack: SingleCallBack<Boolean>
    )
}