package com.example.firebasechatappkotlinmvvm.data.repo.chat

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack

interface ChatRepo {
    fun setupChat(
        otherChatUser: ChatUser,
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

    fun getChats(
        userId: String,
        onGetChatResult: CallBack<List<Chat>, String>,
        count: Int? = null
    )
}
