package com.example.firebasechatappkotlinmvvm.util.extension

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
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

fun Date.subInMilis(date: Date): Long {
    return time - date.time
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

/*
* Used to check whether app running or not
* Note that: when any app's services running -> app running
* */
fun Context.isAppRunning(): Boolean{
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val procInfos = activityManager.runningAppProcesses
    if (procInfos != null) {
        for (processInfo in procInfos) {
            CommonUtil.log(processInfo.processName)
            if (processInfo.processName == "com.example.firebasechatappkotlinmvvm") {
                return true
            }
        }
    }
    return false
}

fun Bitmap.toInputStream(): InputStream{
    val byteArrayOS = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS)
    return ByteArrayInputStream(byteArrayOS.toByteArray())
}