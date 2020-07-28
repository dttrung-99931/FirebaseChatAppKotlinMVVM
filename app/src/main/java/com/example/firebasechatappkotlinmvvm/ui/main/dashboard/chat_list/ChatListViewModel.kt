package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatRepo
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ChatListViewModel @Inject constructor(
    val chatRepo: ChatRepo, val userRepo: UserRepo
) : BaseViewModel() {

    val meUserId: String
    val chats = MutableLiveData<List<Chat>>()

    // first load chats (containing avatar, nickname),
    // then load chatWithMoreInfo (containing isOnline, offlineAt) for each chat in chats
    val changedChat = MutableLiveData<Chat>()
    val changedChatQueue = LinkedList<Chat>()

    val onLoginFailure = MutableLiveData<String>()
    var loadCachedChats = false

    private val onGetChatResult: CallBack<List<Chat>, String> =
        object : CallBack<List<Chat>, String> {
            override fun onSuccess(chatList: List<Chat>?) {
                this@ChatListViewModel.chats.postValue(chatList)
                if (loadCachedChats) {

                    // @Warning do not listen this.chats.value (live data)
                    // although chats.postValue(chatList) called, because
                    // listenChatChange run before chats.postValue done
                    listenChatChange(chatList)
                } else {
                    isLoading.postValue(false)
                    loadCachedChats = true
                }
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
        meUserId = userRepo.getCurAuthUserId()
    }

    private val onUserStatusInChatChange: CallBack<Chat, String> =
        object : CallBack<Chat, String> {
            override fun onSuccess(data: Chat?) {
                changedChatQueue.push(data)
                changedChat.postValue(data)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    private fun listenChatChange(chatList: List<Chat>?) {
        for (chat in chatList!!) {
            // listen off/online user status, offlineAt
            userRepo.listenUserStatus(chat, onUserStatusInChatChange)

//          latter add  chatRepo.listenMessageChange(chat, onUserStatusInChatChange)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    // Check if changedChat is updated
    // because changedChat.postValue() do not post all values to main thread
    // if there is changedChat is not still updated then post it again
    fun onUpdateChangedChatComplete() {
        changedChatQueue.pollLast()
        if (changedChatQueue.size != 0)
            changedChat.value = changedChatQueue.last
    }

    class Factory(val provider: Provider<ChatListViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }

}