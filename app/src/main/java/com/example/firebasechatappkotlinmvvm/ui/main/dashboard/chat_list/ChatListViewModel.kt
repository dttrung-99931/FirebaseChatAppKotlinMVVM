package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatEvent
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatRepo
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ChatListViewModel @Inject constructor(
    private val chatRepo: ChatRepo, val userRepo: UserRepo
) : BaseViewModel() {

    val meUserId: String
    val cachedChats = MutableLiveData<List<Chat>>()

    val changedOrAddedChat = MutableLiveData<Chat>()
    val changedOrAddedChatStack = LinkedList<Chat>()

    var loadRefreshLinkChats = false

    private val onGetCachedChatsResult: CallBack<List<Chat>, String> =
        object : CallBack<List<Chat>, String> {
            override fun onSuccess(chatList: List<Chat>?) {
                this@ChatListViewModel.cachedChats.postValue(chatList)
                isLoading.postValue(false)
                cachedChats.postValue(chatList)
                getRefreshChatsAndListenChanges()
            }

            override fun onError(errCode: String) {
                onError.postValue(errCode)
                isLoading.postValue(false)
            }

            override fun onFailure(errCode: String) {
                onError.postValue(errCode)
                isLoading.postValue(false)
            }
        }

    init {
        isLoading.value = true
        chatRepo.getCachedChats(userRepo.getCurAuthUserId(), onGetCachedChatsResult)
        meUserId = userRepo.getCurAuthUserId()
    }

    val changedAppUser = MutableLiveData<AppUser>()
    val changedAppUserStack = LinkedList<AppUser>()

    private val onAppUserChange: CallBack<AppUser, String> =
        object : CallBack<AppUser, String> {
            override fun onSuccess(data: AppUser?) {
                changedAppUserStack.push(data)
                changedAppUser.postValue(data)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    private var idOfUserNeedToListenStatus: String? = null

    private val onChatEvents: CallBack<List<ChatEvent>, String> =
        object : CallBack<List<ChatEvent>, String> {
            override fun onSuccess(data: List<ChatEvent>?) {
                for (chatEvent in data!!) {
                    changedOrAddedChatStack.push(chatEvent.chat)
                    changedOrAddedChat.postValue(chatEvent.chat)

                    // if new chat added
                    if (loadRefreshLinkChats && chatEvent.isAdded()) {
                        // store user id to listen after update chat ui
                        // in onUpdateChatMetaComplete
                        idOfUserNeedToListenStatus = chatEvent.chat.chatUser.id
                    }
                }

                if (!loadRefreshLinkChats) {
                    loadRefreshLinkChats = true
                    getRefreshUserStatusAndListen(data)
                }
            }

            override fun onError(errCode: String) {
                onError.postValue(errCode)
            }

            override fun onFailure(errCode: String) {
            }
        }

    private fun getRefreshUserStatusAndListen(chatEvents: List<ChatEvent>) {
        chatEvents.forEach {
            userRepo.listenAppUser(it.chat.chatUser.id, onAppUserChange)
        }
    }

    private fun getRefreshChatsAndListenChanges() {
        chatRepo.getRefreshChatsAndListenChanges(meUserId, onChatEvents)
    }

    override fun onCleared() {
        userRepo.removeCurAppUserListeners()
        chatRepo.removeCurChatMetaListeners()
        super.onCleared()
    }

    // Check to make sure all values updated to Main thread
    // because changedChat.postValue() do not post all values to main thread,
    // just the latest one of multiples value to be updated to main thread
    // so use the stack to store missing values and update later
    fun onUpdateChatMetaComplete() {
        val updatedChat = changedOrAddedChatStack.pop()
        if (changedOrAddedChatStack.size != 0)
            changedOrAddedChat.value = changedOrAddedChatStack.first

        // Listen new user status of new added chat
        if (idOfUserNeedToListenStatus != null &&
            updatedChat.chatUser.id == idOfUserNeedToListenStatus){
            userRepo.listenAppUser(idOfUserNeedToListenStatus!!, onAppUserChange)
            idOfUserNeedToListenStatus = null
        }
    }

    fun onUpdateChatUserComplete() {
        changedAppUserStack.pop()
        if (changedAppUserStack.size != 0)
            changedAppUser.value = changedAppUserStack.first
    }

    class Factory(val provider: Provider<ChatListViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }

}