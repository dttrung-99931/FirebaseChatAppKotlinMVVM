package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties


/**
 * Created by Trung on 7/11/2020
 */
@IgnoreExtraProperties
data class AppUser(var nickname: String  = "",
                   var email: String = "",
                // @get: Exclude: exclude properties form firestore serialization and deserialization
                   @get: Exclude var password: String = "",
                   var avatarUrl: String = "",
                   @get: Exclude var uid: String? = "") {
    constructor(firebaseUser: FirebaseUser) :
            this(uid = firebaseUser.uid,
                email = firebaseUser.email!!,
                nickname = firebaseUser.displayName!!)

    companion object{
        fun listFromUserDocuments(documents: List<DocumentSnapshot>): List<AppUser>? {
            val appUsers = ArrayList<AppUser>()
            documents.forEach {
                val user = it.toObject(AppUser::class.java)
                user.let { appUsers.add(user!!) }
            }
            return appUsers
        }

        val DEFAULT_USER = AppUser()
    }
}