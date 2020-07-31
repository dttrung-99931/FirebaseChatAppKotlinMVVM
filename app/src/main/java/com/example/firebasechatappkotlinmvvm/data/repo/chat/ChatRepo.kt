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

    fun getFirstCachedMessagesThenRefresh(
        chatId: String,
        onGetMessagesResult: CallBack<List<Messagee>, String>,
        count: Long? = null
    )

    fun getNextMessages(
        chatId: String,
        onGetNextMessagesResult: CallBack<List<Messagee>, String>
    )

    fun removeCurEventMessageListener()

    fun getChats(
        userId: String,
        onGetChatResult: CallBack<List<Chat>, String>,
        count: Int? = null
    )

    fun listenChatMetaInUserChange(
        chat: Chat,
        meUserId: String,
        onChatChange: CallBack<Chat, String>
    )

    fun resetNewMsg(meUserId: String, chatId: String)

    fun removeCurChatMetaListeners()
}
