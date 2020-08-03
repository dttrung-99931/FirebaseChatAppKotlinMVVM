package com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth

import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreServiceImpl
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class FireBaseAuthServiceImpl @Inject constructor(
    val auth: FirebaseAuth,
    val mFireStoreService: FireStoreService
) : FireBaseAuthService {

    override fun login(appUser: AppUser, callBack: CallBack<Unit, String>) {
        if (CommonUtil.isEmailForm(appUser.email)) {
            loginWithEmail(appUser, callBack)
        } else {
            loginWithNickname(appUser, callBack)
        }
    }

    private fun loginWithEmail(appUser: AppUser, callBack: CallBack<Unit, String>) {
        auth.signInWithEmailAndPassword(appUser.email, appUser.password)
            .addOnSuccessListener {
                callBack.onSuccess()
                updateTokenForUser(it.user!!.uid)
            }
            .addOnFailureListener {
                callBack.onFailure(AppConstants.AuthErr.LOGIN_FAILED)
            }
    }

    override fun updateTokenForUser(userId: String) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener {
                val tokenUpdate = mapOf(FireStoreServiceImpl.FIELD_TOKEN to it.token)
                mFireStoreService.updateAppUser(userId, tokenUpdate)
            }
            .addOnFailureListener {
                CommonUtil.log("updateAccessTokenForUser error ${it.message}")
            }
    }

    private var cachedCurAppUser: AppUser? = null

    override fun getCurAppUser(curAppUserCallBack: CallBack<AppUser, String>) {
        val curAuthUser = getCurAuthUser()
        if (curAuthUser == null) {
            curAppUserCallBack.onFailure(AppConstants.AuthErr.NOT_LOGGED_IN)
            return
        }

        if (cachedCurAppUser != null) {
            curAppUserCallBack.onSuccess(cachedCurAppUser)

        } else {
            mFireStoreService.getAppUser(
                curAuthUser.uid,
                object : CallBack<AppUser, String> {
                    override fun onSuccess(data: AppUser?) {
                        curAppUserCallBack.onSuccess(data)
                        cachedCurAppUser = data
                    }

                    override fun onError(errCode: String) {
                        curAppUserCallBack.onError(errCode)
                    }

                    override fun onFailure(errCode: String) {
                        curAppUserCallBack.onFailure(errCode)
                    }
                }
            )
        }
    }

    private fun loginWithNickname(appUser: AppUser, callBack: CallBack<Unit, String>) {
        mFireStoreService.searchUsers(appUser.nickname,

            object : CallBack<ExploreViewModel.SearchUserResult, String> {
                override fun onSuccess(data: ExploreViewModel.SearchUserResult?) {
                    if (!data!!.users.isNullOrEmpty()) {
                        appUser.email = data.users!![0].email
                        loginWithEmail(appUser, callBack)
                    } else callBack.onFailure(AppConstants.AuthErr.LOGIN_FAILED)
                }

                override fun onError(errCode: String) {
                }

                override fun onFailure(errCode: String) {
                }
            })
    }

    /*
    * Create user (email, password)
    * in firebase auth, then create users/uid{nickname} in firestore
    * */
    override fun singUp(user: AppUser, callBack: CallBack<Unit, String>) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val createdUser = it.result?.user
                    user.id = createdUser?.uid
                    mFireStoreService.addUser(user, callBack)
                }
            }
            .addOnFailureListener(OnFailureListener {
                Log.d("FireBaseAuthServiceImpl", "signUp onError: ${it.message}")
                if (it is FirebaseAuthException)
                    callBack.onFailure(it.errorCode)
                else callBack.onError(AppConstants.CommonErr.UNKNOWN)
            })
    }

    override fun checkAvailableEmail(
        email: String?,
        onCheckAvailableEmailResult: SingleCallBack<Boolean>
    ) {
        if (email != null) {
            auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val isAvailable = it.result?.signInMethods?.isEmpty()
                        if (isAvailable == null || isAvailable == true)
                            onCheckAvailableEmailResult.onSuccess(true)
                        else onCheckAvailableEmailResult.onSuccess(false)
                    } // Error here
                }
        }
    }

    override fun checkAvailableNickname(
        nickname: String?,
        onCheckAvailableNicknameResult: SingleCallBack<Boolean>
    ) {
        mFireStoreService.checkAavailableNickname(nickname, onCheckAvailableNicknameResult)
    }

    override fun checkUserLoggedIn(checkLoggedInCallBack: CallBack<Boolean, String>) {
        checkLoggedInCallBack.onSuccess(auth.currentUser != null)
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurAuthUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getCurAuthUserId(): String {
        return auth.currentUser!!.uid
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        onChangePasswordResult: CallBack<String, String>
    ) {
        if (CommonUtil.isWeekPassword(newPassword)) {
            onChangePasswordResult.onFailure(AppConstants.AuthErr.WEAK_PASSWORD)
            return
        }

        val curFirebaseUser = getCurAuthUser()
        val appUser = AppUser(
            "", curFirebaseUser!!.email!!, oldPassword
        )
        login(appUser,
            object : CallBack<Unit, String> {
                override fun onSuccess(data: Unit?) {
                    curFirebaseUser.updatePassword(newPassword)
                        .addOnSuccessListener {
                            onChangePasswordResult.onSuccess(
                                AppConstants.OK
                            )
                        }
                        .addOnFailureListener {
                            if (it is FirebaseAuthException) {
                                onChangePasswordResult.onFailure(it.errorCode)
                            }
                            CommonUtil.log("changePassword login error ${it.message}")
                        }
                }

                override fun onError(errCode: String) {
                    onChangePasswordResult.onError(errCode)
                }

                override fun onFailure(errCode: String) {
                    onChangePasswordResult.onFailure(AppConstants.AuthErr.INCORRECT_OLD_PASSWORD)
                }
            })
    }
}