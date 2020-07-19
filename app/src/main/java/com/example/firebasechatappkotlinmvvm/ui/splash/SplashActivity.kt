package com.example.firebasechatappkotlinmvvm.ui.splash

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import com.example.firebasechatappkotlinmvvm.ui.auth.AuthActivity
import com.example.firebasechatappkotlinmvvm.ui.base.BaseActivity
import com.example.firebasechatappkotlinmvvm.ui.base.NoInternetConnectionDialog
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivity


/**
 * Created by Trung on 7/19/2020
 */
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isNetworkAvailable()){
            AuthActivity.open(this)
            finish()
        } else openNoInternetFragment()
    }

    override fun onBackPressed() {
        finish()
    }
}