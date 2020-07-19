package com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser


/**
 * Created by Trung on 7/10/2020
 */
interface FireBaseAuthService {
    fun login(appUser: AppUser, callBack: CallBack<Unit, String>)
    fun singUp(user: AppUser, callBack: CallBack<Unit, String>)
    fun checkAvailableEmail(email: String?, availableEmailCallBack: SingleCallBack<Boolean>)
    fun checkAavailableNickname(nickname: String?, availableEmailCallBack: SingleCallBack<Boolean>)
    fun checkUserLoggedIn(checkLoggedInCallBack: CallBack<Boolean, String>)
}