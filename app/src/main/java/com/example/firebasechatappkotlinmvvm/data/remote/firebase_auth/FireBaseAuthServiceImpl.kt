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
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class FireBaseAuthServiceImpl @Inject constructor(val auth: FirebaseAuth,
    val fireStoreService: FireStoreService): FireBaseAuthService {

    override fun login(appUser: AppUser) {
    }

    override fun singUp(user: AppUser, callBack: CallBack<Unit, String>) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    user.uid = it.result?.user?.uid
                    fireStoreService.addUser(user, callBack)
                }
            }
            .addOnFailureListener(OnFailureListener {
                Log.d("FireBaseAuthServiceImpl", "signUp onError: ${it.message}")
                if (it is FirebaseAuthException)
                    callBack.onFail(it.errorCode)
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
                    val isAvailable = it.result?.signInMethods?.isEmpty()
                    if (isAvailable == null || isAvailable == true)
                        availableEmailCallBack.onSuccess(true)
                    else availableEmailCallBack.onSuccess(false)
                }
        }
    }

    override fun checkAavailableNickname(
        nickname: String?,
        availableEmailCallBack: SingleCallBack<Boolean>
    ) {
        fireStoreService.checkUnavailableNickname(nickname, availableEmailCallBack)
    }


}