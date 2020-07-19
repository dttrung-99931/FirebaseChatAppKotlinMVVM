package com.example.firebasechatappkotlinmvvm.util

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
    }
}
