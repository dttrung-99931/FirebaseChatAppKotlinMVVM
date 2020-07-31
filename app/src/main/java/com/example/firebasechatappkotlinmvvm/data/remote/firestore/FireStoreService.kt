package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.*
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreViewModel
import com.google.firebase.auth.FirebaseUser


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
        updateAvatarUrlFirestoreCallBack: CallBack<Any, String>
    )

    fun searchUsers(
        userOrEmail: String,
        mSearchUsersCallBack: CallBack<ExploreViewModel.SearchUserResult, String>
    )

    fun getChatId(
        chatUser: ChatUser,
        me: ChatUser,
        onGetChatIdResult: CallBack<String, String>
    )

    fun getAppUser(uid: String, callBack: CallBack<AppUser, String>)

    fun listenMessageEvent(
        chatId: String,
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
        count: Int?
    )

    fun removeCurEventMessageListener()

    fun getChats(userId: String,
        onGetChatResult: CallBack<List<Chat>, String>, count: Int?
    )

    fun updateUserOnline(user: FirebaseUser?)

    fun updateUserOffline(user: FirebaseUser?)

    fun listenAppUser(id: String, onChange: CallBack<AppUser, String>)

    fun listenChatMetaInUser(
        chat: Chat,
        meUserId: String,
        onChatChange: CallBack<Chat, String>
    )

    fun resetNewMsg(meUserId: String, chatId: String)

    fun getRandomUsers(num: Int, onGetRandomUsersResult: CallBack<List<AppUser>, String>)

    fun removeCurAppUserListeners()
    fun removeCurChatMetaListeners()
}