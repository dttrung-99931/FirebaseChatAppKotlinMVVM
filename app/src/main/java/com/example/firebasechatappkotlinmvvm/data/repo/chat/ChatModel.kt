package com.example.firebasechatappkotlinmvvm.data.repo.chat

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.google.firebase.firestore.*
import java.io.InputStream
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Trung on 7/22/2020
 */

// double 'e' for separating with other Message classes
data class Messagee(
    val senderUserId: String,
    @get: Exclude
    val receiverUserId: String,
    val type: String,
    var content: String = "",
    @ServerTimestamp val createdAt: Date? = null
) {
    fun getThumbMsg(): String {
        return when (type) {
            MSG_TYPE_IMG -> "[New image message]"
            MSG_TYPE_VOICE -> "[New voice message]"
            else -> content
        }
    }

    companion object {
        const val MSG_TYPE_TEXT = "text"
        const val MSG_TYPE_IMG = "img"
        const val MSG_TYPE_VOICE = "voice"
    }

    constructor() : this("", MSG_TYPE_TEXT, "")
}

data class Chat(
    var chatUser: ChatUser = ChatUser.DEFAULT_CHAT_USER,
    var newMsgNum: Int = 0,
    var thumbMsg: String = "",
    @get: Exclude
    var id: String = ""
) {

    constructor() : this(ChatUser.DEFAULT_CHAT_USER)

    companion object {
        fun createList(chatDocuments: List<DocumentSnapshot>): List<Chat> {
            val chats = ArrayList<Chat>();
            chatDocuments.forEach {
                val chat = it.toObject(Chat::class.java)!!
                chat.id = it.id
                chats.add(chat)
            }
            return chats
        }

    }
}

// Used in ChatList
// Like AppUser, but the excluded fields will be loaded latter
// after the rest load completely
data class ChatUser(
    val id: String,
    val nickname: String,
    val avatarUrl: String,
    @get: Exclude var online: Boolean = false,
    @get: Exclude var offlineAt: Date? = null
) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    constructor() : this("", "", "")


    companion object CREATOR : Parcelable.Creator<ChatUser> {
        override fun createFromParcel(parcel: Parcel): ChatUser {
            return ChatUser(parcel)
        }

        override fun newArray(size: Int): Array<ChatUser?> {
            return arrayOfNulls(size)
        }

        fun fromAppUser(user: AppUser): ChatUser {
            return ChatUser(user.id!!, user.nickname, user.avatarUrl)
        }

        val DEFAULT_CHAT_USER = ChatUser("", "", "")

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nickname)
        parcel.writeString(avatarUrl)
    }

    override fun describeContents(): Int {
        return hashCode()
    }
}

data class MessageInfoProvider(
    val message: Messagee,
    val chatId: String,
    var imgStream: InputStream? = null
) {
}

data class MessageEvent(val message: Messagee, val eventType: DocumentChange.Type) {
}
