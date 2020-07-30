package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
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
):
    UserRepo {
    override fun login(appUser: AppUser, callBack: CallBack<Unit, String>) {
        mFireBaseAuthService.login(appUser, callBack)
    }

    override fun singUp(user: AppUser, callBack: CallBack<Unit, String>) {
        mFireBaseAuthService.singUp(user, callBack)
    }

    override fun checkAvailableEmail(
        email: String?,
        availableEmailCallBack: SingleCallBack<Boolean>
    ) {
        mFireBaseAuthService.checkAvailableEmail(email, availableEmailCallBack)
    }

    override fun checkAavailableNickname(
        nickname: String?,
        availableEmailCallBack: SingleCallBack<Boolean>) {
        mFireBaseAuthService.checkAavailableNickname(nickname, availableEmailCallBack)
    }

    override fun checkUserLoggedIn(checkLoggedInCallBack: CallBack<Boolean, String>) {
        mFireBaseAuthService.checkUserLoggedIn(checkLoggedInCallBack)
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

    // @return AppUser(nickname, avatarUrl, ...)
    // get uid, dislayName (nickname) from firebase auth
    // get avatar link from firebase storage
    override fun getCurrentAppUser(curAppUserCallBack: CallBack<AppUser, String>) {
        val curAuthUser = getCurAuthUser()
        if (curAuthUser != null) {
            mFireStoreService.getAppUser(curAuthUser.uid,
                curAppUserCallBack)
        }
        else curAppUserCallBack.onFailure(AppConstants.AuthErr.NOT_LOGGED_IN)
    }

    override fun uploadAvatar(avatarInputStream: InputStream?, uploadAvatarCallBack: CallBack<String, String>) {
        val currentFirebaseUser = getCurAuthUser()
        val uid = currentFirebaseUser?.uid
        if (uid != null){
            mStorageService.uploadAvatar(uid, avatarInputStream,
                object : CallBack<String, String> {
                    override fun onSuccess(avatarUrl: String?) {
                        mFireStoreService.updateAvatarLink(
                            uid,
                            avatarUrl!!,
                            createUpdateAvatarUrlFirestoreCallBack(
                                uploadAvatarCallBack, avatarUrl
                            )
                        )
                    }

                    override fun onError(errCode: String) {
                        uploadAvatarCallBack.onError(errCode)
                    }

                    override fun onFailure(errCode: String) {
                        uploadAvatarCallBack.onFailure(errCode)
                    }
                })
        }


    }

    override fun findUsers(
        userOrEmail: String,
        mSearchUsersCallBack: CallBack<ExploreViewModel.SearchUserResult, String>
    ) {
        mFireStoreService.searchUsers(userOrEmail, mSearchUsersCallBack)
    }

    override fun listenUserStatus(
        chat: Chat,
        onUserStatusInChatChange: CallBack<Chat, String>
    ) {
        mFireStoreService.listenAppUser(chat.chatUser.id,
            object : CallBack<AppUser, String> {
                override fun onSuccess(data: AppUser?) {
                    chat.chatUser.online = data!!.online
                    chat.chatUser.offlineAt = data.offlineAt
                    onUserStatusInChatChange.onSuccess(chat)
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


}