package com.example.firebasechatappkotlinmvvm.ui.auth.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.repo.user.User
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.auth.login.LoginViewModel
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class SignUpViewModel @Inject constructor(val userRepo: UserRepo): BaseViewModel() {
    var mUsername = MutableLiveData<String>()
    var mPassword = MutableLiveData<String>()

    fun onBtnLoginClicked(){
        mLoggedInUser.postValue(
            userRepo.login(mUsername.value, mPassword.value)
        )
    }

    var mLoggedInUser = MutableLiveData<User>()

    class Factory(val provider: Provider<SignUpViewModel>): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}