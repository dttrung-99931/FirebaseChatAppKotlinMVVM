package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.repo.chat.UserChat
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreViewModel
import com.google.firebase.auth.FirebaseUser
import java.io.InputStream
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class UserRepoImpl @Inject constructor(
    val mFireBaseAuthService: FireBaseAuthService,
    val mStorageService: FireBaseStorageService,
    val mFireStoreService: FireStoreService
) :
    UserRepo {
    override fun login(appUser: AppUser, resultCallBack: CallBack<Unit, String>) {
        mFireBaseAuthService.login(appUser, resultCallBack)
    }

    override fun singUp(user: AppUser, resultCallBack: CallBack<Unit, String>) {
        mFireBaseAuthService.singUp(user, resultCallBack)
    }

    override fun checkAvailableEmail(
        email: String?,
        resultCallBack: SingleCallBack<Boolean>
    ) {
        mFireBaseAuthService.checkAvailableEmail(email, resultCallBack)
    }

    override fun checkAvailableNickname(
        nickname: String?,
        resultCallBack: SingleCallBack<Boolean>
    ) {
        mFireBaseAuthService.checkAvailableNickname(nickname, resultCallBack)
    }

    override fun checkUserLoggedIn(resultCallBack: CallBack<Boolean, String>) {
        mFireBaseAuthService.checkUserLoggedIn(resultCallBack)
    }

    override fun signOut() {
        updateUserOffline()
        mFireBaseAuthService.signOut()
    }

    override fun getCurAuthUser(): FirebaseUser? {
        return mFireBaseAuthService.getCurAuthUser()
    }

    override fun getCurAuthUserId(): String {
        return mFireBaseAuthService.getCurAuthUserId()
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        resultCallBack: CallBack<String, String>
    ) {
        mFireBaseAuthService.changePassword(oldPassword, newPassword, resultCallBack)
    }

    override fun updateTokenForUser(userId: String) {
        mFireBaseAuthService.updateTokenForUser(userId)
    }

    // @return AppUser(nickname, avatarUrl, ...)
    // get uid, dislayName (nickname) from firebase auth
    // get avatar link from firebase storage
    override fun getCurAppUser(resultCallBack: CallBack<AppUser, String>) {
        mFireBaseAuthService.getCurAppUser(resultCallBack)
    }

    override fun uploadAvatar(
        avatarInputStream: InputStream?,
        resultCallBack: CallBack<String, String>
    ) {
        val currentFirebaseUser = getCurAuthUser()
        val uid = currentFirebaseUser?.uid
        if (uid != null) {
            mStorageService.uploadAvatar(uid, avatarInputStream,
                object : CallBack<String, String> {
                    override fun onSuccess(avatarUrl: String?) {
                        mFireStoreService.updateAvatarLink(
                            uid,
                            avatarUrl!!,
                            createUpdateAvatarUrlFirestoreCallBack(
                                resultCallBack, avatarUrl
                            )
                        )
                    }

                    override fun onError(errCode: String) {
                        resultCallBack.onError(errCode)
                    }

                    override fun onFailure(errCode: String) {
                        resultCallBack.onFailure(errCode)
                    }
                })
        }


    }

    override fun findUsers(
        userOrEmail: String,
        resultCallBack: CallBack<ExploreViewModel.SearchUserResult, String>
    ) {
        mFireStoreService.searchUsers(userOrEmail, resultCallBack)
    }

    override fun listenUserStatus(
        userChat: UserChat,
        onUserStatusInUserChatChange: CallBack<UserChat, String>
    ) {
        mFireStoreService.listenAppUser(userChat.chatUser.id,
            object : CallBack<AppUser, String> {
                override fun onSuccess(data: AppUser?) {
                    userChat.chatUser.online = data!!.online
                    userChat.chatUser.offlineAt = data.offlineAt
                    onUserStatusInUserChatChange.onSuccess(userChat)
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }
            })
    }

    override fun updateUserOnline() {
        if (getCurAuthUser() != null)
            mFireStoreService.updateUserOnline(getCurAuthUser())
    }

    override fun updateUserOffline() {
        if (getCurAuthUser() != null)
            mFireStoreService.updateUserOffline(getCurAuthUser())
    }

    override fun listenAppUser(
        userId: String,
        onAppUserChange: CallBack<AppUser, String>
    ) {
        mFireStoreService.listenAppUser(userId, onAppUserChange)
    }

    override fun getRandomUsers(num: Int, resultCallBack: CallBack<List<AppUser>, String>) {
        mFireStoreService.getRandomUsers(num, resultCallBack)
    }

    private fun createUpdateAvatarUrlFirestoreCallBack(
        uploadAvatarCallBack: CallBack<String, String>,
        url: String?
    ): CallBack<Any, String> = object : CallBack<Any, String> {
        override fun onSuccess(data: Any?) {
            uploadAvatarCallBack.onSuccess(url)
        }

        override fun onError(errCode: String) {
            uploadAvatarCallBack.onError(errCode)
        }

        override fun onFailure(errCode: String) {
            uploadAvatarCallBack.onFailure(errCode)
        }
    }

    override fun removeCurAppUserListeners() {
        mFireStoreService.removeCurAppUserListeners()
    }
}