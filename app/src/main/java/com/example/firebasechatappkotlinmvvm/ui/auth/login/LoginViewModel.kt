package com.example.firebasechatappkotlinmvvm.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class LoginViewModel @Inject constructor(
    val userRepo: UserRepo
) : BaseViewModel() {

    val usernameOrEmail = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginResult = MutableLiveData<String>()

    private val mLoginCallBack: CallBack<Unit, String> =
        object : CallBack<Unit, String> {
            override fun onSuccess(data: Unit?) {
                loginResult.postValue(AppConstants.OK)
                isLoading.postValue(false)
                userRepo.updateUserOnline()
            }

            override fun onError(errCode: String) {
                onError.postValue(errCode)
                isLoading.postValue(false)
            }

            override fun onFailure(errCode: String) {
                loginResult.postValue(errCode)
                isLoading.postValue(false)
            }
        }

    fun onBtnLoginClicked() {
        isLoading.value = true
        if (usernameOrEmail.value.isNullOrEmpty() ||
            password.value.isNullOrEmpty()
        ) {
            loginResult.value = AppConstants.AuthErr.LOGIN_FAILED
            isLoading.value = false
        } else {
            val appUser = AppUser(usernameOrEmail.value!!,
                usernameOrEmail.value!!, password.value!!)
            userRepo.login(appUser, mLoginCallBack)
        }
    }


    class Factory(val provider: Provider<LoginViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}