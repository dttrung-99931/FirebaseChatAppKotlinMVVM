package com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.InputStream
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class FireBaseStorageServiceImpl @Inject constructor(val storage: FirebaseStorage):
    FireBaseStorageService{
    override fun uploadAvatar(
        uid: String,
        avatarInputStream: InputStream?,
        uploadAvatarCallBack: CallBack<String, String>
    ) {
        val avatarRef = avatarRef(uid)
        avatarRef.putStream(avatarInputStream!!)
            .addOnSuccessListener {
                avatarRef.downloadUrl.addOnSuccessListener {
                    uploadAvatarCallBack.onSuccess(it.toString())
                }
            }
    }

    private fun avatarRef(uid: String): StorageReference {
        return storage.reference.child("users/$uid/avatar.jpg");
    }

    override fun getAvatarUrl(uid: String?, gatAvatarUrlCallBack: CallBack<String, String>) {
        avatarRef(uid!!).downloadUrl
            .addOnSuccessListener {
                CommonUtil.log("FireBaseStorageServiceImpl.getAvatarUrl success: $it")
                gatAvatarUrlCallBack.onSuccess(it.toString())
            }
            .addOnFailureListener {
                CommonUtil.log("FireBaseStorageServiceImpl.getAvatarUrl failed: " + it.message)
            }
    }

}