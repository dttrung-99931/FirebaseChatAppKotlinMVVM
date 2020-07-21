package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class SearchUserViewModel @Inject constructor(val userRepo: UserRepo): BaseViewModel() {
    val searchText = MutableLiveData("")

    val searchUsersResult = MutableLiveData<List<AppUser>>()

    private val mSearchUsersCallBack: CallBack<List<AppUser>, String> =
        object : CallBack<List<AppUser>, String> {
            override fun onSuccess(data: List<AppUser>?) {
                if (data?.size!! > 0)
                    searchUsersResult.postValue(data)
            }

            override fun onError(errCode: String) {
                onError.postValue(errCode)
            }

            override fun onFailure(errCode: String) {
            }
        }

    fun onSearchTextChanged() {
        userRepo.findUsers(searchText.value!!, mSearchUsersCallBack)
    }

    class Factory(val provider: Provider<SearchUserViewModel>): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}