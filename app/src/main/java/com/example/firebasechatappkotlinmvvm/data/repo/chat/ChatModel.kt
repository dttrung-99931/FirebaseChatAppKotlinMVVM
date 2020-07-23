package com.example.firebasechatappkotlinmvvm.data.repo.chat

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


/**
 * Created by Trung on 7/22/2020
 */
// double 'e' for separating with other Message classes
data class Messagee(val senderUserId: String, val content: String,
                    @ServerTimestamp val createdAt: Date? = null){
    constructor(): this("", "")
}

data class MsgGroup(val massageList: List<Messagee>, @get: Exclude val createdAt: Calendar? = null)

data class Chat(val msgGroups: List<MsgGroup>)

data class NewMsg(var newMassage: Messagee? = null, var newMsgGroup: MsgGroup? = null)

data class UserChat(val chatUserId: String, val newMsgNum: Int = 0, val thumbMsg: String = "")

data class MessageInfoProvider(val massage: String, val chatId: String, var senderUserId: String = "") {
    fun toMsg(): Messagee {
        return Messagee(senderUserId, massage)
    }
}

data class MessageEvent(val message: Messagee, val eventType: DocumentChange.Type){
}
