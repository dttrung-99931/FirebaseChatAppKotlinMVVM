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

    fun getFirstCachedMessagesThenGetRefresh(
        chatId: String,
        onGetMessagesResult: CallBack<List<Messagee>, String>,
        count: Long? = null
    )

    fun getNextMessages(
        chatId: String,
        onGetNextMessagesResult: CallBack<List<Messagee>, String>
    )

    fun removeCurEventMessageListener()

    fun getCachedUserChats(
        userId: String,
        onGetCachedUserChatsResult: CallBack<List<UserChat>, String>,
        count: Int? = null
    )

    fun listenUserChat(
        userChat: UserChat,
        meUserId: String,
        onUserChatChange: CallBack<UserChat, String>
    )

    fun resetNewMsg(meUserId: String, chatId: String)

    fun removeCurUserChatListeners()

    fun getRefreshUserChatsAndListen(
        meUserId: String,
        onChatEvents: CallBack<List<ChatEvent>, String>
    )
}
