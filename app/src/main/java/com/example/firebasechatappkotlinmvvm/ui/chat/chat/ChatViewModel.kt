package com.example.firebasechatappkotlinmvvm.ui.chat.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.*
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewModel
import com.google.firebase.firestore.DocumentChange
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
class ChatViewModel @Inject constructor(val chatRepo: ChatRepo, val userRepo: UserRepo) :
    BaseViewModel() {

    lateinit var chatUser: ChatUser
    lateinit var curChatId: String

    // bind with mEdtChat
    val messageInput = MutableLiveData<String>()

    val firstMessages = MutableLiveData<List<Messagee>>()
    val nextMessages = MutableLiveData<List<Messagee>>()
    val newMessage = MutableLiveData<Messagee>()
    val meId: String = userRepo.getCurAuthUserId()

    val onGetCurChatIdSuccess = MutableLiveData<String>()
    val isLoadingMoreMsg = MutableLiveData<Boolean>()

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
            val msg = Messagee(
                meId, chatUser.id,
                Messagee.MSG_TYPE_TEXT, messageInput.value!!
            )

            val messageInfoProvider = MessageInfoProvider(msg, curChatId)
            chatRepo.send(messageInfoProvider, onSendMessageResult)
        }
    }

    private val onMessageEvent: CallBack<MessageEvent, String> =
        object : CallBack<MessageEvent, String> {
            override fun onSuccess(event: MessageEvent?) {
                when (event!!.eventType) {
                    DocumentChange.Type.ADDED -> {
                        newMessage.postValue(event.message)
                        chatRepo.resetNewMsg(meId, curChatId)
                    }
                }
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    private val onGetFirstMessagesResult: CallBack<List<Messagee>, String> =
        object : CallBack<List<Messagee>, String> {
            override fun onSuccess(data: List<Messagee>?) {
                firstMessages.postValue(data)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    private val onSetupResult: CallBack<String, String> =
        object : CallBack<String, String> {
            override fun onSuccess(chatId: String?) {
                onGetCurChatIdSuccess.postValue(chatId)
                curChatId = chatId!!
                chatRepo.getFirstCachedMessagesThenRefresh(
                    curChatId,
                    onGetFirstMessagesResult
                )
                chatRepo.resetNewMsg(meId, chatId)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    fun setupChat(chatUser: ChatUser) {
        this.chatUser = chatUser
        chatRepo.setupChat(chatUser, onMessageEvent, onSetupResult)
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

    fun sendImageMessage(imgStream: InputStream?) {
        val msg = Messagee(meId, chatUser.id, Messagee.MSG_TYPE_IMG)
        val messageInfoProvider = MessageInfoProvider(msg, curChatId)
        messageInfoProvider.imgStream = imgStream
        chatRepo.send(messageInfoProvider, onSendMessageResult)
    }

    var reachedLastMessage = false

    val onGetNextMessagesResult: CallBack<List<Messagee>, String> =
        object : CallBack<List<Messagee>, String> {
            override fun onSuccess(data: List<Messagee>?) {
                if (data.isNullOrEmpty()) reachedLastMessage = true
                nextMessages.postValue(data)
                isLoadingMoreMsg.postValue(false)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }

    fun loadMoreMessages() {
        isLoadingMoreMsg.value = true
        chatRepo.getNextMessages(curChatId, onGetNextMessagesResult)
    }

    fun canLoadMoreMsg(): Boolean {
        return (isLoadingMoreMsg.value == null || !isLoadingMoreMsg.value!!)
                && !reachedLastMessage
    }
}