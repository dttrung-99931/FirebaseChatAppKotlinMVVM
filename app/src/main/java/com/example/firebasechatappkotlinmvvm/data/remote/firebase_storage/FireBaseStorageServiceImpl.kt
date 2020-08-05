package com.example.firebasechatappkotlinmvvm.data.remote.firebase_storage

import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.MessageInfoProvider
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.InputStream
import java.util.*
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class FireBaseStorageServiceImpl @Inject constructor(val storage: FirebaseStorage) :
    FireBaseStorageService {
    override fun uploadAvatar(
        uid: String,
        avatarInputStream: InputStream?,
        resultCallBack: CallBack<String, String>
    ) {
        val avatarRef = avatarRef(uid)
        avatarRef.putStream(avatarInputStream!!)
            .addOnSuccessListener {
                avatarInputStream.close()
                avatarRef.downloadUrl.addOnSuccessListener {
                    resultCallBack.onSuccess(it.toString())
                }
            }
            .addOnFailureListener{
                avatarInputStream.close()
                CommonUtil.log("uploadAvatar failed ${it.message}")
            }
    }

    private fun avatarRef(uid: String): StorageReference {
        return storage.reference.child("users/$uid/avatar.jpg");
    }

    override fun getAvatarUrl(uid: String?, resultCallBack: CallBack<String, String>) {
        avatarRef(uid!!).downloadUrl
            .addOnSuccessListener {
                CommonUtil.log("FireBaseStorageServiceImpl.getAvatarUrl success: $it")
                resultCallBack.onSuccess(it.toString())
            }
            .addOnFailureListener {
                CommonUtil.log("FireBaseStorageServiceImpl.getAvatarUrl failed: " + it.message)
            }
    }

    override fun uploadMsgImg(
        messageInfoProvider: MessageInfoProvider,
        resultCallBack: CallBack<String, String>
    ) {
        val msgImgRef = randomMsgImgRef(messageInfoProvider.chatId)
        msgImgRef
            .putStream(messageInfoProvider.imgStream!!)
            .addOnSuccessListener {
                messageInfoProvider.imgStream?.close()
                msgImgRef.downloadUrl
                    .addOnSuccessListener {
                        resultCallBack.onSuccess(it.toString())
                    }
                    .addOnFailureListener {
                        CommonUtil.log("uploadMsgImg get url error ${it.message}")
                    }
            }
            .addOnFailureListener {
                messageInfoProvider.imgStream?.close()
                CommonUtil.log("uploadMsgImg error ${it.message}")
            }
    }

    private fun randomMsgImgRef(chatId: String): StorageReference {
        val uuid = UUID.randomUUID().toString()
        return storage.reference.child("chats/$chatId/images/$uuid.jpg");
    }
}