package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.UserChat
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
    val cachedUserChats = MutableLiveData<List<UserChat>>()

    val changedOrAddedUserChat = MutableLiveData<UserChat>()
    val changedOrAddedUserChatStack = LinkedList<UserChat>()

    var loadedRefreshUserChats = false

    private val getCachedUserChatsCallBack: CallBack<List<UserChat>, String> =
        object : CallBack<List<UserChat>, String> {
            override fun onSuccess(userChatList: List<UserChat>?) {
                this@ChatListViewModel.cachedUserChats.postValue(userChatList)
                isLoading.postValue(false)
                cachedUserChats.postValue(userChatList)
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
        chatRepo.getCachedUserChats(userRepo.getCurAuthUserId(), getCachedUserChatsCallBack)
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
                    changedOrAddedUserChatStack.push(chatEvent.userChat)
                    changedOrAddedUserChat.postValue(chatEvent.userChat)

                    // if new userChat added
                    if (loadedRefreshUserChats && chatEvent.isAdded()) {
                        // store user id to listen after updating chat ui
                        // in onUpdateChatMetaComplete
                        idOfUserNeedToListenStatus = chatEvent.userChat.chatUser.id
                    }
                }

                if (!loadedRefreshUserChats) {
                    loadedRefreshUserChats = true
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
            userRepo.listenAppUser(it.userChat.chatUser.id, onAppUserChange)
        }
    }

    private fun getRefreshChatsAndListenChanges() {
        chatRepo.getRefreshUserChatsAndListen(meUserId, onChatEvents)
    }

    override fun onCleared() {
        userRepo.removeCurAppUserListeners()
        chatRepo.removeCurUserChatListeners()
        super.onCleared()
    }

    // Check to make sure all values updated to Main thread
    // because changedChat.postValue() do not post all values to main thread,
    // just the latest one of multiples value to be updated to main thread
    // so use the stack to store missing values and update later
    fun onUpdateUserChatComplete() {
        val updatedUserChat = changedOrAddedUserChatStack.pop()
        if (changedOrAddedUserChatStack.size != 0)
            changedOrAddedUserChat.value = changedOrAddedUserChatStack.first

        // Listen new user status of new added chat
        if (idOfUserNeedToListenStatus != null &&
            updatedUserChat.chatUser.id == idOfUserNeedToListenStatus){
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