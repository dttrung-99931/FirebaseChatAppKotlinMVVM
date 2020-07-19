package com.example.firebasechatappkotlinmvvm.ui.auth.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class SignUpViewModel @Inject constructor(val userRepo: UserRepo) : BaseViewModel() {
    val password = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    var isValidUsername = false

    val onSignUpSuccess = MutableLiveData<Any>()
    val onSignUpFailureWithCode = MutableLiveData<String>()

    fun onBtnSignUpClicked() {
        if (isValidUsername && checkValidPassword()) {
            userRepo.singUp(bundleUser(), signUpCallBack)
        } else userRepo.checkAavailableNickname(nickname.value,
            object : SingleCallBack<Boolean> {
                override fun onSuccess(data: Boolean) {
                    if (data) {
                        userRepo.singUp(bundleUser(), signUpCallBack)
                        isValidUsername = true
                    } else {
                        onSignUpFailureWithCode.postValue(
                            AppConstants.AuthErr.UNAVAILABLE_NICKNAME
                        )
                        isValidUsername = false
                    }
                }
            })
        isLoading.value = true
    }

    private fun checkValidPassword(): Boolean {
        if (CommonUtil.isWeekPassword(password.value)) {
            onSignUpFailureWithCode.value = AppConstants.AuthErr.WEAK_PASSWORD
            return false
        }
        return true
    }

    private fun bundleUser(): AppUser {
        return AppUser(nickname.value!!, email.value!!, password.value!!)
    }

    val signUpCallBack = object : CallBack<Unit, String> {
        override fun onSuccess(data: Unit?) {
            onSignUpSuccess.postValue(Any())
            isLoading.postValue(false)
        }

        override fun onError(errCode: String) {
            isLoading.postValue(false)
        }

        override fun onFailure(errCode: String) {
            onSignUpFailureWithCode.postValue(errCode)
            isLoading.postValue(false)
        }
    }

    fun onTypeEmailComplete() {
        if (!email.value.isNullOrEmpty()){
            if (CommonUtil.isEmailForm(email.value!!)){
                userRepo.checkAvailableEmail(email.value,
                    object : SingleCallBack<Boolean> {
                        override fun onSuccess(data: Boolean) {
                            if (!data) onSignUpFailureWithCode.postValue(
                                AppConstants.AuthErr.UNAVAILABLE_EMAIL
                            )
                        }
                    })
            } else onSignUpFailureWithCode.postValue(AppConstants.AuthErr.INVALID_EMAIL_FORMAT)
        }
    }

    fun onTypeNicknameComplete() {
        if (!nickname.value.isNullOrEmpty()) {
            userRepo.checkAavailableNickname(nickname.value,
                object : SingleCallBack<Boolean> {
                    override fun onSuccess(data: Boolean) {
                        if (!data) {
                            onSignUpFailureWithCode.postValue(
                                AppConstants.AuthErr.UNAVAILABLE_NICKNAME
                            )
                            isValidUsername = false
                        } else isValidUsername = true
                    }
                })
        }
    }

    fun onTypePasswordComplete() {
        if (!password.value.isNullOrEmpty())
            checkValidPassword()
    }


    class Factory(val provider: Provider<SignUpViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}