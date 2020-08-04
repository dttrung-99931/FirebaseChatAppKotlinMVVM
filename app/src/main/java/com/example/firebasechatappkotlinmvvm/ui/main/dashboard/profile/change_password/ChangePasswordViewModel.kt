package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile.change_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ChangePasswordViewModel @Inject constructor(val userRepo: UserRepo): BaseViewModel() {
    val newPassword = MutableLiveData<String>()
    val oldPassword = MutableLiveData<String>()
    val changePasswordResult = MutableLiveData<String>()

    private val changePasswordCallBack: CallBack<String, String> =
        object : CallBack<String, String> {
            override fun onSuccess(data: String?) {
                isLoading.value = false
                changePasswordResult.postValue(data)
            }

            override fun onError(errCode: String) {
                isLoading.value = false
                onError.postValue(errCode)
            }

            override fun onFailure(errCode: String) {
                isLoading.value = false
                changePasswordResult.postValue(errCode)
            }
        }

    fun onBtnOkClicked(){
        if (oldPassword.value.isNullOrEmpty() ||
            newPassword.value.isNullOrEmpty()){

            changePasswordResult.value =
                AppConstants.AuthErr.MISSING_INFORMATION

        } else {
            isLoading.value = true
            userRepo.changePassword(
                oldPassword.value!!,
                newPassword.value!!,
                changePasswordCallBack)
        }
    }

    class Factory(val provider: Provider<ChangePasswordViewModel>): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}