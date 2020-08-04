package com.example.firebasechatappkotlinmvvm.data.repo.user

import android.graphics.Bitmap
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.repo.chat.UserChat
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreViewModel
import java.io.InputStream

interface UserRepo : FireBaseAuthService {
    fun uploadAvatar(
        avatarInputStream: InputStream?,
        resultCallBack: CallBack<String, String>
    )

    fun findUsers(
        userOrEmail: String,
        resultCallBack: CallBack<ExploreViewModel.SearchUserResult, String>
    )

    fun listenUserStatus(
        userChat: UserChat,
        onUserStatusInUserChatChange: CallBack<UserChat, String>
    )

    fun updateUserOnline()

    fun updateUserOffline()

    fun listenAppUser(
        userId: String,
        onAppUserChange: CallBack<AppUser, String>
    )

    fun getRandomUsers(num: Int, resultCallBack: CallBack<List<AppUser>, String>)

    fun removeCurAppUserListeners()
}
