package com.example.firebasechatappkotlinmvvm.service

import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.google.firebase.iid.FirebaseInstanceIdService
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * Created by Trung on 8/2/2020
 */
class UpdateTokenService: FirebaseInstanceIdService() {
    @Inject
    lateinit var authService: FireBaseAuthService

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onTokenRefresh() {
        authService.updateTokenForUser(authService.getCurAuthUserId())
        super.onTokenRefresh()
    }
}