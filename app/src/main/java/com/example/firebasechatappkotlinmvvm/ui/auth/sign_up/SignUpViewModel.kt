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

    // Used to ignore redundant nickname checking
    var isValidNickname = false

    val signUpResult = MutableLiveData<String>()

    val loginResult = MutableLiveData<Boolean>()

    fun onBtnSignUpClicked() {
        if (isValidNickname && checkValidPassword()) {
            userRepo.singUp(bundleUser(), signUpCallBack)
        } else userRepo.checkAvailableNickname(nickname.value,
            object : SingleCallBack<Boolean> {
                override fun onSuccess(data: Boolean) {
                    if (data) {
                        userRepo.singUp(bundleUser(), signUpCallBack)
                        isValidNickname = true
                    } else {
                        signUpResult.postValue(
                            AppConstants.AuthErr.UNAVAILABLE_NICKNAME
                        )
                        isValidNickname = false
                    }
                }
            })
        isLoading.value = true
    }

    private fun checkValidPassword(): Boolean {
        if (CommonUtil.isWeekPassword(password.value)) {
            signUpResult.value = AppConstants.AuthErr.WEAK_PASSWORD
            return false
        }
        return true
    }

    private fun bundleUser(): AppUser {
        return AppUser(nickname.value!!, email.value!!, password.value!!)
    }

    val signUpCallBack = object : CallBack<Unit, String> {
        override fun onSuccess(data: Unit?) {
            signUpResult.postValue(AppConstants.OK)
            isLoading.postValue(false)
        }

        override fun onError(errCode: String) {
            isLoading.postValue(false)
        }

        override fun onFailure(errCode: String) {
            signUpResult.postValue(errCode)
            isLoading.postValue(false)
        }
    }

    fun onTypeEmailComplete() {
        if (!email.value.isNullOrEmpty()) {
            if (CommonUtil.isEmailForm(email.value!!)
                && email.value!!.length in 7..40) {

                userRepo.checkAvailableEmail(email.value,
                    object : SingleCallBack<Boolean> {
                        override fun onSuccess(data: Boolean) {
                            if (!data) signUpResult.postValue(
                                AppConstants.AuthErr.UNAVAILABLE_EMAIL
                            )
                        }
                    })
            } else {
                signUpResult.value = AppConstants.AuthErr.INVALID_EMAIL_FORMAT_OR_LENGTH
            }
        }
    }

    fun onTypeNicknameComplete() {
        if (!nickname.value.isNullOrEmpty()) {
            if (CommonUtil.isEmailForm(nickname.value!!)
                || !(nickname.value!!.length in 2..20)
            ) {
                signUpResult.value = AppConstants.AuthErr.INVALID_NICKNAME_FORMAT_OR_LENGTH
                isValidNickname = false
            } else {
                userRepo.checkAvailableNickname(nickname.value,
                    object : SingleCallBack<Boolean> {
                        override fun onSuccess(data: Boolean) {
                            isValidNickname = if (!data) {
                                signUpResult.postValue(
                                    AppConstants.AuthErr.UNAVAILABLE_NICKNAME
                                )
                                false
                            } else true
                        }
                    })
            }
        }
    }

    fun onTypePasswordComplete() {
        if (!password.value.isNullOrEmpty())
            checkValidPassword()
    }

    private val mLoginCallBack: CallBack<Unit, String> =
        object : CallBack<Unit, String> {
            override fun onSuccess(data: Unit?) {
                loginResult.postValue(true)
                isLoading.postValue(false)
                userRepo.updateUserOnline()
            }

            override fun onError(errCode: String) {
                isLoading.postValue(false)
                onError.postValue(errCode)
            }

            override fun onFailure(errCode: String) {
                isLoading.postValue(false)
                onError.postValue(AppConstants.CommonErr.UNKNOWN)
            }
        }

    fun loginAfterSignUpSuccessfully() {
        isLoading.value = true
        val appUser = AppUser("", email.value!!, password.value!!)
        userRepo.login(appUser, mLoginCallBack)
    }

    class Factory(val provider: Provider<SignUpViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}