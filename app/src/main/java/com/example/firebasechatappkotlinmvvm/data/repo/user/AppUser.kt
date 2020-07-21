package com.example.firebasechatappkotlinmvvm.data.repo.user

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties


/**
 * Created by Trung on 7/11/2020
 */
@IgnoreExtraProperties
data class AppUser(var nickname: String  = "",
                // @get: Exclude: exclude properties form firestore serialization and deserialization
                   @get: Exclude var email: String = "",
                   @get: Exclude var password: String = "",
                   @get: Exclude var avatarUrl: String = "",
                   @get: Exclude var uid: String? = "") {
    constructor(firebaseUser: FirebaseUser) :
            this(uid = firebaseUser.uid,
                email = firebaseUser.email!!,
                nickname = firebaseUser.displayName!!)

    companion object{
        val DEFAULT_USER = AppUser()
    }
}