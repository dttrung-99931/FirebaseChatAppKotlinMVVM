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
        const val INCORRECT_OLD_PASSWORD = "INCORRECT_OLD_PASSWORD"
        const val MISSING_INFORMATION = "MISSING_INFORMATION"
    }

    companion object{
        val STR_IDS_AVATAR_OPTION = listOf(
            R.string.see,
            R.string.upload,
            R.string.take_a_photo
        )

        val STR_IDS_CLICK_SEARCH_USER_OPTION = listOf (
            R.string.chat
        )

        val PAGE_SIZE_MSG : Long = 15

        val OK = "OK"
    }

    object View {
        const val DRAWABLE_LEFT = 0
        const val  DRAWABLE_TOP = 1
        const val  DRAWABLE_RIGHT = 2
        const val  DRAWABLE_BOTTOM = 3
    }
}
