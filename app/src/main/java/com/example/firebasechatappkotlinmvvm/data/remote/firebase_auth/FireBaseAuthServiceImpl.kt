package com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth

import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class FireBaseAuthServiceImpl @Inject constructor(val auth: FirebaseAuth,
    val fireStoreService: FireStoreService): FireBaseAuthService {

    override fun login(appUser: AppUser, callBack: CallBack<Unit, String>) {
        auth.signInWithEmailAndPassword(appUser.email, appUser.password)
            .addOnSuccessListener {
                callBack.onSuccess()
            }
            .addOnFailureListener {
                callBack.onFailure(AppConstants.AuthErr.LOGIN_FAILED)
            }
    }

    /*
    * Create user (email, password, nickname(displayname)
    * in firebase auth, then create users/uid in firestore
    * */
    override fun singUp(user: AppUser, callBack: CallBack<Unit, String>) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val createdUser = it.result?.user
                    user.id = createdUser?.uid
                    fireStoreService.addUser(user, callBack)
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
        availableEmailCallBack: SingleCallBack<Boolean>
    ) {
        if (email != null){
            auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val isAvailable = it.result?.signInMethods?.isEmpty()
                        if (isAvailable == null || isAvailable == true)
                            availableEmailCallBack.onSuccess(true)
                        else availableEmailCallBack.onSuccess(false)
                    } // Error here
                }
        }
    }

    override fun checkAavailableNickname(
        nickname: String?,
        availableEmailCallBack: SingleCallBack<Boolean>
    ) {
        fireStoreService.checkUnavailableNickname(nickname, availableEmailCallBack)
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
}