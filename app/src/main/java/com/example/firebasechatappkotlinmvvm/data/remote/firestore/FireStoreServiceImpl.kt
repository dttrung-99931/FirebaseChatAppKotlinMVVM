package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
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
        const val FIELD_AVATAR_URL = "avatarUrl"
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

    override fun updateAvatarLink(
        uid: String,
        avatarUrl: String,
        updateAvatarUrlFirestoreCallBack: CallBack<Any, String>
    ) {
        findUserDocument(uid)
            .update(FIELD_AVATAR_URL, avatarUrl)
            .addOnSuccessListener {
                updateAvatarUrlFirestoreCallBack.onSuccess()
            }
            .addOnFailureListener{
                CommonUtil.log("FireStoreServiceImpl.updateAvatarLink failed ${it.message}")
            }
    }

    override fun searchUsers(
        userOrEmail: String,
        mSearchUsersCallBack: CallBack<List<AppUser>, String>
    ) {
        firestore.collection(COLLECTION_USERS)
            // find users by nickname first
            .whereEqualTo(FIELD_NICKNAME, userOrEmail)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) mSearchUsersCallBack
                    .onSuccess(AppUser.listFromUserDocuments(it.documents))
                else searchUsersByEmail(userOrEmail, mSearchUsersCallBack)
            }
    }

    private fun searchUsersByEmail(userOrEmail: String, mSearchUsersCallBack: CallBack<List<AppUser>, String>) {
        firestore.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_EMAIL, userOrEmail)
            .get()
            .addOnSuccessListener {
                mSearchUsersCallBack
                .onSuccess(AppUser.listFromUserDocuments(it.documents))
        }
    }

    private fun findUserDocument(uid: String) = firestore
        .collection(COLLECTION_USERS)
        .document(uid)
}