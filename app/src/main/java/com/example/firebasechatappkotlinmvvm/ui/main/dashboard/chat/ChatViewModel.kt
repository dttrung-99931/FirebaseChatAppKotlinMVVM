package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.*
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.google.firebase.firestore.DocumentChange
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ChatViewModel @Inject constructor(val chatRepo: ChatRepo, val userRepo: UserRepo) :
    BaseViewModel() {

    lateinit var chatUser: AppUser
    lateinit var curChatId: String

    // bind with mEdtChat
    val messageInput = MutableLiveData<String>()

    val messages = MutableLiveData<List<Messagee>>()
    val newMessage = MutableLiveData<Messagee>()
    val meId: String = userRepo.getCurAuthUserId()

    private val onSendMessageResult: CallBack<String, String> =
        object : CallBack<String, String> {
            override fun onSuccess(data: String?) {
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    fun onBtnSendClicked() {
        if (!messageInput.value.isNullOrEmpty()) {
            val messageInfoProvider = MessageInfoProvider(messageInput.value!!, curChatId)
            chatRepo.send(messageInfoProvider, onSendMessageResult)
        }
    }

    private val onMessageEvent: CallBack<MessageEvent, String> =
        object : CallBack<MessageEvent, String> {
            override fun onSuccess(event: MessageEvent?) {
                when (event!!.eventType) {
                    DocumentChange.Type.ADDED -> newMessage.postValue(event.message)
                }
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    private val onGetLastMessagesResult: CallBack<List<Messagee>, String> =
        object : CallBack<List<Messagee>, String> {
            override fun onSuccess(data: List<Messagee>?) {
                messages.postValue(data)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    private val onListenerSetupResult: CallBack<String, String> =
        object : CallBack<String, String> {
            override fun onSuccess(data: String?) {
                curChatId = data!!
                chatRepo.getLastMessages(curChatId, onGetLastMessagesResult)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    fun setupChat(chatUser: AppUser) {
        this.chatUser = chatUser
        chatRepo.listenForMessageEvent(chatUser, onMessageEvent, onListenerSetupResult)
    }

    class Factory(val provider: Provider<ChatViewModel>) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    }

    override fun onCleared() {
        chatRepo.removeCurEventMessageListener()
        super.onCleared()
    }
}