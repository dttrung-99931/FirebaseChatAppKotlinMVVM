package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatRepo
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ChatListViewModel @Inject constructor(
    val chatRepo: ChatRepo, val userRepo: UserRepo): BaseViewModel() {

    val chats = MutableLiveData<List<Chat>>()
    val onLoginFailure = MutableLiveData<String>()

    private val onGetChatResult: CallBack<List<Chat>, String> =
        object : CallBack<List<Chat>, String> {
            override fun onSuccess(data: List<Chat>?) {
                chats.postValue(data)
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

    init {
        isLoading.value = true
        chatRepo.getChats(userRepo.getCurAuthUserId(), onGetChatResult)
    }

    class Factory(val provider: Provider<ChatListViewModel>): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }
}