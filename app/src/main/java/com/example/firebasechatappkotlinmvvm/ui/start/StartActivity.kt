package com.example.firebasechatappkotlinmvvm.ui.start

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.firebasechatappkotlinmvvm.ui.auth.AuthActivity
import com.example.firebasechatappkotlinmvvm.ui.base.BaseActivity
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * Created by Trung on 7/19/2020
 */
class StartActivity : BaseActivity() {
    @Inject
    lateinit var vm: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        observe()
        if (isNetworkAvailable()){
            vm.processNavToAuthOrMainActivity()
        } else openNoInternetFragment()

    }

    private fun observe() {
        vm.onCheckUserLoggedInRes.observe(this, Observer {
            if (it) MainActivity.open(this)
            else AuthActivity.open(this)
            finish()
        })
    }

    override fun onBackPressed() {
        finish()
    }

}