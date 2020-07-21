package com.example.firebasechatappkotlinmvvm.util

import com.example.firebasechatappkotlinmvvm.R

class AppConstants {

    object CommonErr{
        const val UNKNOWN = "UNKNOWN_ERROR"
    }

    object AuthErr{
        const val INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT"
        const val UNAVAILABLE_NICKNAME = "UNAVAILABLE_NICKNAME"
        const val UNAVAILABLE_EMAIL = "ERROR_EMAIL_ALREADY_IN_USE"
        const val WEAK_PASSWORD = "ERROR_WEAK_PASSWORD"
        const val LOGIN_FAILED = "LOGIN_FAILED"
        const val NOT_LOGGED_IN = "NOT_LOGGED_IN"
    }

    companion object{
        val STR_IDS_AVATAR_OPTION = listOf(
            R.string.see,
            R.string.upload,
            R.string.take_a_photo
        )
    }
}
