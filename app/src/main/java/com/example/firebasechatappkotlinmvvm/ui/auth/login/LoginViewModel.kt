package com.example.firebasechatappkotlinmvvm.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class LoginViewModel @Inject constructor(val userRepo: UserRepo): BaseViewModel() {
    var mUsernameOrEmail = MutableLiveData<String>()
    var mPassword = MutableLiveData<String>()

    var mLoggedInUser = MutableLiveData<AppUser>()

    fun onBtnLoginClicked(){
    }


    class Factory(val provider: Provider<LoginViewModel>): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}