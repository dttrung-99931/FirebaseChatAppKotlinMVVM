package com.example.firebasechatappkotlinmvvm.data.repo.chat

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser

interface ChatRepo {
    fun listenForMessageEvent(
        chatUser: AppUser,
        onMessageEvent: CallBack<MessageEvent, String>,
        onListeningSetupResult: CallBack<String, String>
    )

    fun send(
        messageInfoProvider: MessageInfoProvider,
        onSendMessageResult: CallBack<String, String>
    )

    fun getLastMessages(
        chatId: String,
        onGetLastMessagesResult: CallBack<List<Messagee>, String>,
        count: Int? = null
    )

    fun removeCurEventMessageListener()
}
