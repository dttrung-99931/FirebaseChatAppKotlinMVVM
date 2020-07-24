package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable


/**
 * Created by Trung on 7/11/2020
 */
@IgnoreExtraProperties
data class AppUser(var nickname: String  = "",
                   var email: String = "",
                // @get: Exclude: exclude properties form firestore serialization and deserialization
                   @get: Exclude var password: String = "",
                   var avatarUrl: String = "",
                   @get: Exclude var id: String? = ""): Serializable {

    fun toChatUser(): ChatUser {
        return ChatUser(id!!, nickname, avatarUrl)
    }

    constructor(firebaseUser: FirebaseUser) :
            this(id = firebaseUser.uid)

    companion object{
        fun listFromUserDocuments(documents: List<DocumentSnapshot>): List<AppUser>? {
            val appUsers = ArrayList<AppUser>()
            documents.forEach {
                val user = it.toObject(AppUser::class.java)
                val userUid = it.id
                user.let {
                    user!!.id = userUid
                    appUsers.add(user) }
            }
            return appUsers
        }
    }
}