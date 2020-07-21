package com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.android.gms.tasks.OnFailureListener
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
        uploadAvatarCallBack: CallBack<Any, String>
    ) {
        avatarRef(uid).putStream(avatarInputStream!!)
            .addOnSuccessListener {
                uploadAvatarCallBack.onSuccess()
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