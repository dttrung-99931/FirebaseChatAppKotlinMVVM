package com.example.firebasechatappkotlinmvvm.data.repo.user

import androidx.lifecycle.MutableLiveData
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class UserRepoImpl @Inject constructor(val fireBaseAuthService: FireBaseAuthService):
    UserRepo {

    override fun login(value: String?, value1: String?): User {
        return User("trung", "dz")
    }

}