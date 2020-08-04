package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ExploreViewModel @Inject constructor(val userRepo: UserRepo): BaseViewModel() {
    val searchText = MutableLiveData("")

    val searchUsers = MutableLiveData<List<AppUser>>()

    var latestStartSearchTimeInMilis = Calendar.getInstance().timeInMillis

    var randomUsers = mutableListOf<AppUser>()

    val getRandomUsersCallBack: CallBack<List<AppUser>, String> =
        object : CallBack<List<AppUser>, String> {
            override fun onSuccess(data: List<AppUser>?) {
                randomUsers = data!!.toMutableList()
                searchUsers.postValue(data)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    init{
        userRepo.getRandomUsers(10, getRandomUsersCallBack)
    }

    private val searchUsersCallBack: CallBack<SearchUserResult, String> =
        object : CallBack<SearchUserResult, String> {
            override fun onSuccess(data: SearchUserResult?) {
                // Show random user when search text is empty
                if (searchText.value.isNullOrEmpty()){
                    searchUsers.postValue(randomUsers)
                }
                else
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
        if (!searchText.value.isNullOrEmpty())
            userRepo.findUsers(searchText.value!!, searchUsersCallBack)
    }

    fun loadRandomUsers() {
        searchUsers.value = randomUsers
    }

    // Use searchingStartTime, searchText to prepare async search results
    // that have unordered finish
    // The latter search result show be updated
    data class SearchUserResult(
        val searchText: String,
        var users: List<AppUser>? = null,
        val searchingStartTimeInMilis: Long = Calendar.getInstance().timeInMillis
    )

    class Factory(val provider: Provider<ExploreViewModel>): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}