package com.example.firebasechatappkotlinmvvm.util

import android.content.Context
import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.ArrayList

class CommonUtil {
    companion object{
        fun isEmailForm(email: String): Boolean {
            return email.matches(Regex("[\\w.-_]+@[\\w]+\\.[\\w]{0,4}"));
        }

        fun isWeekPassword(password: String?): Boolean {
            return password == null || password.length < 6
        }

        fun log(msg: String) {
            Log.d("AAAA", msg);
        }

        fun toStrings(stringResIds: List<Int>, context: Context): List<String> {
            val strings =  ArrayList<String>()
            stringResIds.forEach {
                strings.add(context.getString(it))
            }
            return strings;
        }

        fun toMessagesFromMessageDocuments(documents: List<DocumentSnapshot>): List<Messagee>? {
            val messages = ArrayList<Messagee>()
            documents.forEach {
                messages.add(it.toObject(Messagee::class.java)!!)
            }
            return  messages
        }

    }
}
