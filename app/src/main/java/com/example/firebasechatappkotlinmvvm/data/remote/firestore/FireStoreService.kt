package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.MessageEvent
import com.example.firebasechatappkotlinmvvm.data.repo.chat.MessageInfoProvider
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user.SearchUserViewModel


/**
 * Created by Trung on 7/10/2020
 */
interface FireStoreService {
    fun addUser(user: AppUser, callBack: CallBack<Unit, String>)
    fun checkUnavailableNickname(
        nickname: String?,
        availableNicknameCallBack: SingleCallBack<Boolean>
    )

    fun updateAvatarLink(
        uid: String,
        avatarUrl: String,
        updateAvatarUrlFirestoreCallBack: CallBack<Any, String>)

    fun searchUsers(userOrEmail: String,
                    mSearchUsersCallBack: CallBack<SearchUserViewModel.SearchUserResult, String>)

    fun getChatId(
        chatUser: AppUser,
        me: AppUser,
        onGetChatIdResult: CallBack<String, String>
    )

    fun getAppUser(uid: String, callBack: CallBack<AppUser, String>)

    fun listenForMessageEvent(
        chatId: String,
        onMessageEvent: CallBack<MessageEvent, String>,
        onListeningSetupResult: CallBack<String, String>
    )

    fun send(messageInfoProvider: MessageInfoProvider, onSendMessageResult: CallBack<String, String>)

    fun getLastMessages(
        chatId: String,
        onGetLastMessagesResult: CallBack<List<Messagee>, String>,
        count: Int?
    )

    fun removeCurEventMessageListener()
}