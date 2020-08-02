package com.example.firebasechatappkotlinmvvm.data.repo.chat

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class ChatRepoImpl @Inject constructor(
    val mStorageService: FireBaseStorageService,
    val mFireStoreService: FireStoreService,
    val mFireBaseAuthService: FireBaseAuthService
) :
    ChatRepo {

    override fun setupChat(
        otherChatUser: ChatUser,
        onMessageEvent: CallBack<MessageEvent, String>,
        onListeningSetupResult: CallBack<String, String>
    ) {
        mFireStoreService.getAppUser(mFireBaseAuthService.getCurAuthUserId(),
            object : CallBack<AppUser, String> {
                override fun onSuccess(data: AppUser?) {
                    getChatIdThenListenChat( // if chat is not exists then create chat
                        data!!.toChatUser(), otherChatUser,
                        onMessageEvent, onListeningSetupResult
                    )
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }
            })

    }

    private fun getChatIdThenListenChat(
        meChatUser: ChatUser,
        otherChatUser: ChatUser,
        onMessageEvent: CallBack<MessageEvent, String>,
        onListeningSetupResult: CallBack<String, String>
    ) {
        mFireStoreService.getChatId(otherChatUser, meChatUser,
            object : CallBack<String, String> {
                override fun onSuccess(data: String?) {
                    mFireStoreService.listenMessageEvent(
                        data!!,
                        onMessageEvent, onListeningSetupResult
                    )
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }
            })
    }

    override fun send(
        messageInfoProvider: MessageInfoProvider,
        onSendMessageResult: CallBack<String, String>
    ) {

        when (messageInfoProvider.message.type) {
            Messagee.MSG_TYPE_TEXT -> mFireStoreService.send(
                messageInfoProvider,
                onSendMessageResult
            )
            Messagee.MSG_TYPE_IMG -> sendImgMsg(messageInfoProvider, onSendMessageResult)
        }
    }

    private fun sendImgMsg(
        messageInfoProvider: MessageInfoProvider,
        onSendMessageResult: CallBack<String, String>
    ) {
        mStorageService.uploadMsgImg(messageInfoProvider,
            object : CallBack<String, String>{
                override fun onSuccess(data: String?) {
                    messageInfoProvider.message.content = data!!
                    mFireStoreService.send(
                        messageInfoProvider,
                        onSendMessageResult
                    )
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }

            })
    }

    override fun getFirstCachedMessagesThenGetRefresh(
        chatId: String,
        onGetMessagesResult: CallBack<List<Messagee>, String>,
        count: Long?
    ) {
        mFireStoreService.getFirstCachedMessagesThenGetRefresh(chatId, object : CallBack<List<Messagee>, String> {
            override fun onSuccess(data: List<Messagee>?) {
                onGetMessagesResult.onSuccess(data)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }, count)
    }

    override fun getNextMessages(
        chatId: String,
        onGetNextMessagesResult: CallBack<List<Messagee>, String>
    ) {
        mFireStoreService.getNextMessages(chatId, onGetNextMessagesResult)
    }

    override fun removeCurEventMessageListener() {
        mFireStoreService.removeCurEventMessageListener()
    }

    override fun getCachedUserChats(
        userId: String,
        onGetCachedUserChatsResult: CallBack<List<UserChat>, String>,
        count: Int?
    ) {
        mFireStoreService.getCachedUserChats(userId, onGetCachedUserChatsResult, count)
    }

    override fun listenUserChat(
        userChat: UserChat,
        meUserId: String,
        onUserChatChange: CallBack<UserChat, String>
    ) {
        mFireStoreService.listenUserChat(userChat, meUserId, onUserChatChange)
    }

    override fun resetNewMsg(meUserId: String, chatId: String) {
        mFireStoreService.resetNewMsg(meUserId, chatId)
    }

    override fun removeCurUserChatListeners() {
        mFireStoreService.removeCurUserChatListeners()
    }

    override fun getRefreshUserChatsAndListen(
        meUserId: String,
        onChatEvents: CallBack<List<ChatEvent>, String>
    ) {
        mFireStoreService.getRefreshUserChatsAndListen(
            meUserId, onChatEvents
        )
    }
}