package com.example.firebasechatappkotlinmvvm.util

import android.util.Log

class CommonUtil {
    companion object{
        fun isEmailForm(email: String): Boolean {
            return email.matches(Regex("[\\w.-_]+@[\\w]+\\.[\\w]{0,4}"));
        }

        fun isWeekPassword(password: String?): Boolean {
            return password == null || password.length < 6
        }

        fun log(msg: String) {
            Log.d("AAA", msg);
        }
    }
}
