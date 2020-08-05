package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile

import android.graphics.Bitmap
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
class ProfileViewModel @Inject constructor(val userRepo: UserRepo) : BaseViewModel() {

    val curAppUser = MutableLiveData<AppUser>()

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

    private val uploadAvatarCallBack: CallBack<String, String> =
        object : CallBack<String, String> {
            override fun onSuccess(data: String?) {
                curAppUser.value?.avatarUrl = data!!
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

    val onOpenAvatarImgViewer = MutableLiveData<String>()

    fun onSeeAvatarOptionClicked() {
        if (curAppUser.value != null) {
            onOpenAvatarImgViewer.value = curAppUser.value!!.avatarUrl
        } else onError.value = AppConstants.CommonErr.UNKNOWN
    }

    class Factory(val provider: Provider<ProfileViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}