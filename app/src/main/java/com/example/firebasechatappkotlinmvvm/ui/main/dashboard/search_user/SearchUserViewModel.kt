package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class SearchUserViewModel @Inject constructor(val userRepo: UserRepo) : BaseViewModel() {
    val searchText = MutableLiveData("")

    val searchUsers = MutableLiveData<List<AppUser>>()

    var latestStartSearchTimeInMilis = Calendar.getInstance().timeInMillis

    private val mSearchUsersCallBack: CallBack<SearchUserResult, String> =
        object : CallBack<SearchUserResult, String> {
            override fun onSuccess(data: SearchUserResult?) {
                // Update search results if the results are earlier
                if (latestStartSearchTimeInMilis <
                    data!!.searchingStartTimeInMilis){
                    searchUsers.postValue(data.users)
                    latestStartSearchTimeInMilis = data.searchingStartTimeInMilis
                }
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

    class Factory(val provider: Provider<SearchUserViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }

    // Use searchingStartTime, searchText to prepare async search results
    // that have unordered finish
    data class SearchUserResult(
        val searchText: String,
        var users: List<AppUser>? = null,
        val searchingStartTimeInMilis: Long = Calendar.getInstance().timeInMillis
    )
}