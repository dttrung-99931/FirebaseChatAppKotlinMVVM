package com.example.firebasechatappkotlinmvvm.data.repo.chat

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.remote.send_notification.NotificationData
import com.example.firebasechatappkotlinmvvm.data.remote.send_notification.NotificationModel
import com.example.firebasechatappkotlinmvvm.data.remote.send_notification.NotificationSenderService
import com.example.firebasechatappkotlinmvvm.data.remote.send_notification.Response
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class ChatRepoImpl @Inject constructor(
    val mStorageService: FireBaseStorageService,
    val mFireStoreService: FireStoreService,
    val mFireBaseAuthService: FireBaseAuthService,
    val mNotiSenderService: NotificationSenderService
) :
    ChatRepo {

    override fun setupChat(
        otherChatUser: ChatUser,
        onMessageEvent: CallBack<MessageEvent, String>,
        resultCallBack: CallBack<String, String>
    ) {
        mFireStoreService.getAppUser(mFireBaseAuthService.getCurAuthUserId(),
            object : CallBack<AppUser, String> {
                override fun onSuccess(data: AppUser?) {
                    getChatIdThenListenChat( // if chat is not exists then create chat
                        data!!.toChatUser(), otherChatUser,
                        onMessageEvent, resultCallBack
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
        resultCallBack: CallBack<String, String>
    ) {
        when (messageInfoProvider.message.type) {
            Messagee.MSG_TYPE_TEXT -> mFireStoreService.send(
                messageInfoProvider,
                resultCallBack
            )
            Messagee.MSG_TYPE_IMG -> sendImgMsg(messageInfoProvider, resultCallBack)
        }

        getNotiDataMsgAndSendNoti(messageInfoProvider)
    }

    private fun getNotiDataMsgAndSendNoti(messageInfoProvider: MessageInfoProvider) {
        // get receiver token, then get sender info (id, nickname, avatarUrl) then send noti
        mFireStoreService.getAppUser(
            messageInfoProvider.message.receiverUserId,
            object : CallBack<AppUser, String> {
                override fun onSuccess(receiver: AppUser?) {
                    if (receiver!!.token.isNotEmpty()) {
                        getSenderInfoNicknameAndSendNoti(messageInfoProvider, receiver.token)
                    } else CommonUtil.log("sendNotification getAppUser token is null")
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }
            })
    }

    private fun getSenderInfoNicknameAndSendNoti(
        messageInfoProvider: MessageInfoProvider,
        receiverToken: String
    ) {
        mFireBaseAuthService.getCurAppUser(object : CallBack<AppUser, String> {
            override fun onSuccess(sender: AppUser?) {
                val data = NotificationData(
                    messageInfoProvider.message.senderUserId,
                    sender!!.nickname,
                    sender.avatarUrl,
                    messageInfoProvider.message.type,
                    messageInfoProvider.message.content
                )
                mNotiSenderService.sendNotification(
                    NotificationModel(receiverToken, data)
                ).enqueue(onSendNotiResult)
            }

            override fun onError(errCode: String) {
                CommonUtil.log("getAdditionalSenderNicknameAndSendNoti getCurAppUser " +
                        "onError")
            }

            override fun onFailure(errCode: String) {
                CommonUtil.log("getAdditionalSenderNicknameAndSendNoti getCurAppUser " +
                        "onFailure")
            }
        })
    }

    val onSendNotiResult = object : Callback<Response?> {
        override fun onFailure(call: Call<Response?>, t: Throwable) {
            CommonUtil.log("onSendNotiResult.onFailure ${t.message}")
        }

        override fun onResponse(
            call: Call<Response?>,
            response: retrofit2.Response<Response?>
        ) {
            if (response.isSuccessful) {
                CommonUtil.log("Send noti success, body ${response.body().toString()}, " +
                        "Code : ${response.code()}")
            } else CommonUtil.log("Send noti error: code ${response.code()}, body " +
                    response.body().toString()
            )
        }
    }

    private fun sendImgMsg(
        messageInfoProvider: MessageInfoProvider,
        onSendMessageResult: CallBack<String, String>
    ) {
        mStorageService.uploadMsgImg(messageInfoProvider,
            object : CallBack<String, String> {
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
        resultCallBack: CallBack<List<Messagee>, String>,
        count: Long?
    ) {
        mFireStoreService.getFirstCachedMessagesThenGetRefresh(
            chatId,
            object : CallBack<List<Messagee>, String> {
                override fun onSuccess(data: List<Messagee>?) {
                    resultCallBack.onSuccess(data)
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }
            },
            count
        )
    }

    override fun getNextMessages(
        chatId: String,
        resultCallBack: CallBack<List<Messagee>, String>
    ) {
        mFireStoreService.getNextMessages(chatId, resultCallBack)
    }

    override fun removeCurEventMessageListener() {
        mFireStoreService.removeCurEventMessageListener()
    }

    override fun getCachedUserChats(
        userId: String,
        resultCallBack: CallBack<List<UserChat>, String>,
        count: Int?
    ) {
        mFireStoreService.getCachedUserChats(userId, resultCallBack, count)
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