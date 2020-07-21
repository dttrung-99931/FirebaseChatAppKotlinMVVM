package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user.SearchUserViewModel
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
        unavailableNicknameCallBack: SingleCallBack<Boolean>) {
        mFireBaseAuthService.checkAavailableNickname(nickname, unavailableNicknameCallBack)
    }

    override fun checkUserLoggedIn(checkLoggedInCallBack: CallBack<Boolean, String>) {
        mFireBaseAuthService.checkUserLoggedIn(checkLoggedInCallBack)
    }

    override fun signOut() {
        mFireBaseAuthService.signOut()
    }

    override fun getCurrentFirebaseUser(): FirebaseUser? {
        return mFireBaseAuthService.getCurrentFirebaseUser()
    }

    // @return AppUser(nickname, avatarUrl, ...)
    // get uid, dislayName (nickname) from firebase auth
    // get avatar link from firebase storage
    override fun getCurrentAppUser(curAppUserCallBack: CallBack<AppUser, String>) {
        val curFirebaseUser = getCurrentFirebaseUser()
        if (curFirebaseUser != null) {
            val curAppUser = AppUser(curFirebaseUser)
            mStorageService.getAvatarUrl(curAppUser.uid, object : CallBack<String, String> {
                override fun onSuccess(data: String?) {
                    curAppUser.avatarUrl = data!!
                    curAppUserCallBack.onSuccess(curAppUser)
                }

                override fun onError(errCode: String) {
                    curAppUserCallBack.onError(errCode)
                }

                override fun onFailure(errCode: String) {
                    curAppUserCallBack.onFailure(errCode)
                }
            })
        }
        else curAppUserCallBack.onFailure(AppConstants.AuthErr.NOT_LOGGED_IN)
    }

    override fun uploadAvatar(avatarInputStream: InputStream?, uploadAvatarCallBack: CallBack<String, String>) {
        val currentFirebaseUser = getCurrentFirebaseUser()
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
        mSearchUsersCallBack: CallBack<SearchUserViewModel.SearchUserResult, String>
    ) {
        mFireStoreService.searchUsers(userOrEmail, mSearchUsersCallBack)
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