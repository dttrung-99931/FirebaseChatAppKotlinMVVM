package com.example.firebasechatappkotlinmvvm.util

import android.content.Context
import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import java.util.Collections.swap
import kotlin.collections.ArrayList
import kotlin.random.Random

class CommonUtil {
    companion object {
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
            val strings = ArrayList<String>()
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
            return messages
        }

        fun createDatePattern(createdAt: Date): String {
            val now = Calendar.getInstance().time
            return if (createdAt.year == now.year)
                if (createdAt.month == now.month)
                    if (createdAt.day == now.day) "HH:mm"
                    else "HH:mm EEE"
                else "HH:mm MMM, dd"
            else "HH:mm MMM, dd, yyyy"
        }

        // return a new str is made from str param
        // with size <= shortLength
        fun getShortString(str: String, shortLength: Int): String {
            return if (str.length <= shortLength) str
            else str.substring(0, shortLength-4) + "..."
        }

        fun <T> getRandomList(list: List<T>, num: Int): List<T> {
            if (list.size <= num) return list
            val randomList = ArrayList<T>()
            for (i in 0 until num) {
                val bound = list.size - i // => 0 <= random value <= ranBound-1
                val ranIndex = Random.nextInt(bound)
                randomList.add(list[ranIndex])

                // Swap the selected element with the cur bound element
                // then in the next loop select a random element in [0->cur bound index-1]
                swap(list, ranIndex, bound-1)
            }
            return randomList
        }

    }
}
