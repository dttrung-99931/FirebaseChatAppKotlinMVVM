package com.example.firebasechatappkotlinmvvm.ui.start

import androidx.lifecycle.MutableLiveData
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import javax.inject.Inject


/**
 * Created by Trung on 7/19/2020
 */
class StartViewModel @Inject constructor(val userRepo: UserRepo): BaseViewModel() {
    val onCheckUserLoggedInResult = MutableLiveData<Boolean>()

    private val checkLoggedInCallBack : CallBack<Boolean, String> =
       object : CallBack<Boolean, String> {
           override fun onSuccess(data: Boolean?) {
               onCheckUserLoggedInResult.postValue(data)
           }

           override fun onError(errCode: String) {
               onError.postValue(errCode)
           }

           override fun onFailure(errCode: String) {
               onError.postValue(errCode)
           }
       }

    fun processNavToAuthOrMainActivity() {
        userRepo.checkUserLoggedIn(checkLoggedInCallBack)
    }
}