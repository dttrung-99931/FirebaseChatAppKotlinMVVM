package com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.google.firebase.auth.FirebaseUser


/**
 * Created by Trung on 7/10/2020
 */
interface FireBaseAuthService {
    fun login(appUser: AppUser, callBack: CallBack<Unit, String>)

    fun singUp(user: AppUser, callBack: CallBack<Unit, String>)

    fun checkAvailableEmail(email: String?, onCheckAvailableEmailResult: SingleCallBack<Boolean>)

    fun checkAvailableNickname(nickname: String?, onCheckAvailableNicknameResult: SingleCallBack<Boolean>)

    fun checkUserLoggedIn(checkLoggedInCallBack: CallBack<Boolean, String>)

    fun signOut()

    fun getCurAuthUser(): FirebaseUser?

    fun getCurAuthUserId(): String

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onChangePasswordResult: CallBack<String, String>
    )

}