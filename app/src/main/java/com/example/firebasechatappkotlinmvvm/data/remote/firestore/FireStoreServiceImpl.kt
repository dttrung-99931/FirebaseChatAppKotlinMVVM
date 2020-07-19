package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class FireStoreServiceImpl  @Inject constructor(val firestore: FirebaseFirestore):
    FireStoreService {
    companion object{
        const val COLLECTION_USERS = "users"
        const val FIELD_EMAIL = "email"
        const val FIELD_NICKNAME = "nickname"
        const val TAG = "FireStoreServiceImpl"
    }

    override fun addUser(user: AppUser, callBack: CallBack<Unit, String>) {
        if (!user.uid.isNullOrEmpty())
            firestore.collection(COLLECTION_USERS)
                .document(user.uid!!)
                .set(user)
                .addOnSuccessListener {
                    callBack.onSuccess()
                }
                .addOnFailureListener {
                    callBack.onFailure(AppConstants.CommonErr.UNKNOWN)
                    Log.d(TAG, "addUser: " + it.message)
                }
        else {
            callBack.onFailure(AppConstants.CommonErr.UNKNOWN)
            Log.d(TAG, "addUser: " + " Empty uid")
        }

    }

    override fun checkUnavailableNickname(
        nickname: String?,
        availableNicknameCallBack: SingleCallBack<Boolean>
    ) {
        firestore.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_NICKNAME, nickname)
            .get()
            .addOnSuccessListener {
                if (it.size() == 0)
                    availableNicknameCallBack.onSuccess(true)
                else availableNicknameCallBack.onSuccess(false)
            }
    }
}