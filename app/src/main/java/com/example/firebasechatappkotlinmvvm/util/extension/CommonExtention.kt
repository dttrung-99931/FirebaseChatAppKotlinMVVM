package com.example.firebasechatappkotlinmvvm.util.extension

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Trung on 7/26/2020
 */
@SuppressLint("SimpleDateFormat")
fun Date.format(pattern: String): String{
    val dateFormatter = SimpleDateFormat(pattern)
    return dateFormatter.format(this)
}

fun Date.equalDay(date: Date): Boolean {
    return day == date.day &&
           month == date.month &&
           year == date.year;
}

val gson = Gson()

//convert a data class to a map
fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

//convert a map to a data class
inline fun <reified T> Map<String, Any>.toDataClass(): T {
    return convert()
}

//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}