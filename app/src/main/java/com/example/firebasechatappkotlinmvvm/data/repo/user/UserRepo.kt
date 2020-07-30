package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreViewModel
import java.io.InputStream

interface UserRepo: FireBaseAuthService {
    fun getCurrentAppUser(curAppUserCallBack: CallBack<AppUser, String>)
    fun uploadAvatar(
        avatarInputStream: InputStream?,
        uploadAvatarCallBack: CallBack<String, String>
    )

    fun findUsers(
        userOrEmail: String,
        mSearchUsersCallBack: CallBack<ExploreViewModel.SearchUserResult, String>
    )

    fun listenUserStatus(chat: Chat, onUserStatusInChatChange: CallBack<Chat, String>)

    fun updateUserOnline()

    fun updateUserOffline()

    fun listenAppUser(
        userId: String,
        onAppUserChange: CallBack<AppUser, String>
    )

    fun getRandomUsers(num: Int, onGetRandomUsersResult: CallBack<List<AppUser>, String>)
}
