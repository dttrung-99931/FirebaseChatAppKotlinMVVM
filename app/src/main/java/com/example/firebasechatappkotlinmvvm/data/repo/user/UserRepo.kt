package com.example.firebasechatappkotlinmvvm.data.repo.user

import androidx.lifecycle.MutableLiveData

interface UserRepo {
    fun login(value: String?, value1: String?): User
}
