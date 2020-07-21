package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
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
}