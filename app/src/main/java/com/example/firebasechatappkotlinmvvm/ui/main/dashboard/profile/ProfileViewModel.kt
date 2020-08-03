package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ProfileViewModel @Inject constructor(val userRepo: UserRepo): BaseViewModel() {

    val curAppUser = MutableLiveData<AppUser>()
    val usernameOrEmail = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val onLoginFailure = MutableLiveData<String>()

    val onLoginSuccess = MutableLiveData<Unit>()

    private val mLoginCallBack: CallBack<Unit, String> =
        object : CallBack<Unit, String> {
            override fun onSuccess(data: Unit?) {
                onLoginSuccess.postValue(Unit)
                isLoading.postValue(false)
            }

            override fun onError(errCode: String) {
                onError.postValue(errCode)
                isLoading.postValue(false)
            }

            override fun onFailure(errCode: String) {
                onLoginFailure.postValue(errCode)
                isLoading.postValue(false)
            }
        }

    fun onBtnLoginClicked(){
        isLoading.value = true
        if (usernameOrEmail.value.isNullOrEmpty() ||
            password.value.isNullOrEmpty()){
            onLoginFailure.value = AppConstants.AuthErr.LOGIN_FAILED
            isLoading.value = false
        }
        else {
            val appUser = AppUser("", usernameOrEmail.value!!, password.value!!)
            userRepo.login(appUser, mLoginCallBack)
        }
    }

    fun signOut() {
        userRepo.signOut()
    }

    private val getCurrentUserCallBack: CallBack<AppUser, String> =
        object : CallBack<AppUser, String> {
            override fun onSuccess(data: AppUser?) {
                curAppUser.postValue(data)
            }

            override fun onError(errCode: String) {
                onError.postValue(errCode)
            }

            override fun onFailure(errCode: String) {
                onError.postValue(errCode)
            }
        }

    fun loadUserProfile() {
        userRepo.getCurAppUser(getCurrentUserCallBack)
    }

    val uploadAvatarCallBack: CallBack<String, String> =
        object : CallBack<String, String> {
            override fun onSuccess(data: String?) {
                isLoading.postValue(false)
            }

            override fun onError(errCode: String) {
                onError.postValue(errCode)
            }

            override fun onFailure(errCode: String) {
                onError.postValue(errCode)
            }
        }

    fun uploadAvatar(avatarInputStream: InputStream?) {
        isLoading.value = true
        userRepo.uploadAvatar(avatarInputStream, uploadAvatarCallBack)
    }

    class Factory(val provider: Provider<ProfileViewModel>): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}