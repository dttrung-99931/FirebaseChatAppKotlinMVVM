package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import java.io.InputStream

interface UserRepo: FireBaseAuthService {
    fun getCurrentAppUser(curAppUserCallBack: CallBack<AppUser, String>)
    fun uploadAvatar(
        avatarInputStream: InputStream?,
        uploadAvatarCallBack: CallBack<String, String>
    )

    fun findUsers(userOrEmail: String, mSearchUsersCallBack: CallBack<List<AppUser>, String>)
}
