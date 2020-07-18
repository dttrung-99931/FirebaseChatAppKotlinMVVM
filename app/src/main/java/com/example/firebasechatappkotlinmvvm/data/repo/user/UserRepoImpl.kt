package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class UserRepoImpl @Inject constructor(val mFireBaseAuthService: FireBaseAuthService):
    UserRepo {

    override fun login(appUser: AppUser) {
        mFireBaseAuthService.login(appUser)
    }

    override fun singUp(user: AppUser, callBack: CallBack<Unit, String>) {
        mFireBaseAuthService.singUp(user, callBack)
    }

    override fun checkAvailableEmail(
        email: String?,
        availableEmailCallBack: SingleCallBack<Boolean>
    ) {
        mFireBaseAuthService.checkAvailableEmail(email, availableEmailCallBack)
    }

    override fun checkAavailableNickname(
        nickname: String?,
        unavailableNicknameCallBack: SingleCallBack<Boolean>) {
        mFireBaseAuthService.checkAavailableNickname(nickname, unavailableNicknameCallBack)
    }

}