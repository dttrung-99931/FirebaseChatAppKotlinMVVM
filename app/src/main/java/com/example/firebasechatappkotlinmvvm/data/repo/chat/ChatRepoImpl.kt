package com.example.firebasechatappkotlinmvvm.data.repo.chat

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class ChatRepoImpl @Inject constructor(
    val mStorageService: FireBaseStorageService,
    val mFireStoreService: FireStoreService,
    val mFireBaseAuthService: FireBaseAuthService
):
    ChatRepo {


    override fun listenForMessageEvent(
        chatUser: AppUser,
        onMessageEvent: CallBack<MessageEvent, String>,
        onListeningSetupResult: CallBack<String, String>
    ) {
        val me = AppUser(mFireBaseAuthService.getCurAuthUser()!!)
        mFireStoreService.getChatId(chatUser, me,
            object : CallBack<String, String> {
                override fun onSuccess(data: String?) {
                    mFireStoreService.listenForMessageEvent(data!!, onMessageEvent, onListeningSetupResult)
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }
            })
    }

    override fun send(messageInfoProvider: MessageInfoProvider, onSendMessageResult: CallBack<String, String>) {
        messageInfoProvider.senderUserId = mFireBaseAuthService.getCurAuthUserId()
        mFireStoreService.send(messageInfoProvider, onSendMessageResult)
    }

    override fun getLastMessages(
        chatId: String,
        onGetLastMessagesResult: CallBack<List<Messagee>, String>,
        count: Int?
    ) {
        mFireStoreService.getLastMessages(chatId, object : CallBack<List<Messagee>, String> {
            override fun onSuccess(data: List<Messagee>?) {
                onGetLastMessagesResult.onSuccess(data)
            }

            override fun onError(errCode: String) {
            }

            override fun onFailure(errCode: String) {
            }
        }, count)
    }

    override fun removeCurEventMessageListener() {
        mFireStoreService.removeCurEventMessageListener()
    }
}