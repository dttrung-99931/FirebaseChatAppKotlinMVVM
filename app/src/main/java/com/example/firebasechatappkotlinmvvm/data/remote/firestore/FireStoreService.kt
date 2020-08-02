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

    fun checkAavailableNickname(
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
        onSearchUsersResult: CallBack<ExploreViewModel.SearchUserResult, String>
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

    fun getFirstCachedMessagesThenGetRefresh(
        chatId: String,
        onGetMessagesResult: CallBack<List<Messagee>, String>,
        count: Long?
    )

    fun removeCurEventMessageListener()

    fun getCachedUserChats(
        userId: String,
        onGetCachedUserChatsResult: CallBack<List<UserChat>, String>, count: Int?
    )

    fun updateUserOnline(user: FirebaseUser?)

    fun updateUserOffline(user: FirebaseUser?)

    fun listenAppUser(id: String, onChange: CallBack<AppUser, String>)

    fun listenUserChat(
        userChat: UserChat,
        meUserId: String,
        onUserChatChange: CallBack<UserChat, String>
    )

    fun resetNewMsg(meUserId: String, chatId: String)

    fun getRandomUsers(num: Int, onGetRandomUsersResult: CallBack<List<AppUser>, String>)

    fun removeCurAppUserListeners()

    fun removeCurUserChatListeners()

    fun getNextMessages(chatId: String, onGetNextMessagesResult: CallBack<List<Messagee>, String>)

    fun getRefreshUserChatsAndListen(
        meUserId: String,
        onChatEvents: CallBack<List<ChatEvent>, String>
    )
}