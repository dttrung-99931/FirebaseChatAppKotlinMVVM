package com.example.firebasechatappkotlinmvvm.data.repo.user


/**
 * Created by Trung on 7/11/2020
 */
data class User(var username: String, var password: String) {
    companion object{
        val DEFAULT_USER = User("", "")
    }
}