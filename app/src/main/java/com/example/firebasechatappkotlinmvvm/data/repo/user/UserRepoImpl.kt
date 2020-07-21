package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage.FireBaseStorageService
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.io.InputStream
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class UserRepoImpl @Inject constructor(
    val mFireBaseAuthService: FireBaseAuthService,
    val mStorageService: FireBaseStorageService
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

    override fun uploadAvatar(avatarInputStream: InputStream?, uploadAvatarCallBack: CallBack<Any, String>) {
        val currentFirebaseUser = getCurrentFirebaseUser()
        val uid = currentFirebaseUser?.uid
        if (uid != null){
            mStorageService.uploadAvatar(uid, avatarInputStream,
                uploadAvatarCallBack)
        }
    }


}