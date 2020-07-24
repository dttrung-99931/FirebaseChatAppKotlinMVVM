package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.*
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user.SearchUserViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import javax.inject.Inject


/**
 * Created by Trung on 7/10/2020
 */
class FireStoreServiceImpl @Inject constructor(val firestore: FirebaseFirestore) :
    FireStoreService {

    companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_EMAIL = "email"
        const val FIELD_NICKNAME = "nickname"
        const val FIELD_AVATAR_URL = "avatarUrl"

        const val COLLECTION_CHATS = "chats"
        const val COLLECTION_MESSAGES = "messages"
        const val PATH_CHAT_USER_ID = "chatUser.id"
        const val FIELD_MSG_LIST = "msgList"
        const val FIELD_MESSAGE = "message"
        const val FIELD_CREATED_AT = "createdAt"

        const val TAG = "FireStoreServiceImpl"
    }

    private var listenRemover: ListenerRegistration? = null

    private var ignoredDocumentsChangedEventsAfterSetListener = false

    override fun addUser(user: AppUser, callBack: CallBack<Unit, String>) {
        if (!user.id.isNullOrEmpty())
            firestore.collection(COLLECTION_USERS)
                .document(user.id!!)
                .set(user)
                .addOnSuccessListener {
                    callBack.onSuccess()
                }
                .addOnFailureListener {
                    callBack.onFailure(AppConstants.CommonErr.UNKNOWN)
                    Log.d(TAG, "addUser: " + it.message)
                }
        else {
            callBack.onFailure(AppConstants.CommonErr.UNKNOWN)
            Log.d(TAG, "addUser: " + " Empty uid")
        }

    }

    override fun checkUnavailableNickname(
        nickname: String?,
        availableNicknameCallBack: SingleCallBack<Boolean>
    ) {
        firestore.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_NICKNAME, nickname)
            .get()
            .addOnSuccessListener {
                if (it.size() == 0)
                    availableNicknameCallBack.onSuccess(true)
                else availableNicknameCallBack.onSuccess(false)
            }
    }

    override fun updateAvatarLink(
        uid: String,
        avatarUrl: String,
        updateAvatarUrlFirestoreCallBack: CallBack<Any, String>
    ) {
        userDocument(uid)
            .update(FIELD_AVATAR_URL, avatarUrl)
            .addOnSuccessListener {
                updateAvatarUrlFirestoreCallBack.onSuccess()
            }
            .addOnFailureListener {
                CommonUtil.log("FireStoreServiceImpl.updateAvatarLink failed ${it.message}")
            }
    }

    override fun searchUsers(
        userOrEmail: String,
        mSearchUsersCallBack: CallBack<SearchUserViewModel.SearchUserResult, String>
    ) {
        val searchUserResult = SearchUserViewModel.SearchUserResult(userOrEmail)
        firestore.collection(COLLECTION_USERS)
            // find users by nickname first
            .whereEqualTo(FIELD_NICKNAME, userOrEmail)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    searchUserResult.users = AppUser.listFromUserDocuments(it.documents)

                    mSearchUsersCallBack.onSuccess(searchUserResult)
                }
                // Keep start time in searchUserResult
                else searchUsersByEmail(userOrEmail, mSearchUsersCallBack, searchUserResult)
            }
    }

    private fun searchUsersByEmail(
        userOrEmail: String,
        mSearchUsersCallBack: CallBack<SearchUserViewModel.SearchUserResult, String>,
        searchUserResult: SearchUserViewModel.SearchUserResult
    ) {
        firestore.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_EMAIL, userOrEmail)
            .get()
            .addOnSuccessListener {
                searchUserResult.users = AppUser.listFromUserDocuments(it.documents)

                mSearchUsersCallBack.onSuccess(searchUserResult)
            }
    }

    private fun userDocument(uid: String) = firestore
        .collection(COLLECTION_USERS)
        .document(uid)

    override fun getChatId(
        chatUser: ChatUser,
        me: ChatUser,
        onGetChatIdResult: CallBack<String, String>
    ) {
        userDocument(me.id)
            .collection(COLLECTION_CHATS)
            .whereEqualTo(PATH_CHAT_USER_ID, chatUser.id)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    onGetChatIdResult.onSuccess(it.documents[0].id)
                } else createChatAndGetChatId(chatUser, me, onGetChatIdResult)
            }
    }

    override fun getAppUser(uid: String, callBack: CallBack<AppUser, String>) {
        userDocument(uid).get()
            .addOnSuccessListener {
                val appUser = it.toObject(AppUser::class.java)
                appUser?.id = it.id
                callBack.onSuccess(appUser)
            }
            .addOnFailureListener {
                CommonUtil.log("getAppUser failed ${it.message}")
            }
    }

    override fun listenForMessageEvent(
        chatId: String,
        onMessageEvent: CallBack<MessageEvent, String>,
        onListeningSetupResult: CallBack<String, String>
    ) {
        ignoredDocumentsChangedEventsAfterSetListener = false
        listenRemover = chatDocument(chatId)
            .collection(COLLECTION_MESSAGES)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    CommonUtil.log("listenForNewMsg error: ${error.message}")
                    return@addSnapshotListener
                }

                // if change is saved in the backend
                if (value != null) {
                    notifyMessageEvent(value, onMessageEvent)
                }
            }

        // Still don't find a way to know listening setup is successfully
        // so assume if no error the success is after call the addSnapshotListener
        onListeningSetupResult.onSuccess(chatId)
    }

    private fun notifyMessageEvent(
        value: QuerySnapshot,
        onMessageEvent: CallBack<MessageEvent, String>
    ) {
        if (ignoredDocumentsChangedEventsAfterSetListener)
            for (documentChange in value.documentChanges) {
                val message = documentChange.document.toObject(Messagee::class.java)
                CommonUtil.log("Document change $message")
                onMessageEvent.onSuccess(
                    MessageEvent(message, documentChange.type)
                )
            }
        else ignoredDocumentsChangedEventsAfterSetListener = true
    }

    override fun send(
        messageInfoProvider: MessageInfoProvider,
        onSendMessageResult: CallBack<String, String>
    ) {
        chatDocument(messageInfoProvider.chatId)
            .collection(COLLECTION_MESSAGES)
            .add(messageInfoProvider.toMsg())
            .addOnSuccessListener {
                onSendMessageResult.onSuccess()
            }
            .addOnFailureListener {
                CommonUtil.log("send error ${it.message}")
            }
    }

    override fun getLastMessages(
        chatId: String,
        onGetLastMessagesResult: CallBack<List<Messagee>, String>,
        count: Int?
    ) {
        chatDocument(chatId)
            .collection(COLLECTION_MESSAGES)
            // Do not implement pagination wit count param
            .orderBy(FIELD_CREATED_AT)
            .get()
            .addOnSuccessListener {
                onGetLastMessagesResult.onSuccess(
                    CommonUtil.toMessagesFromMessageDocuments(it.documents)
                )
            }
            .addOnFailureListener {
                CommonUtil.log("getLastMessages err: ${it.message}")
            }
    }

    override fun removeCurEventMessageListener() {
        listenRemover.let {
            listenRemover!!.remove()
        }
    }

    override fun getChats(
        userId: String,
        onGetChatResult: CallBack<List<Chat>, String>,
        count: Int?
    ) {
        userDocument(userId)
            .collection(COLLECTION_CHATS)
            .get()
            .addOnSuccessListener {
                onGetChatResult.onSuccess(Chat.createList(it.documents))
            }
            .addOnFailureListener {
                CommonUtil.log("getChats error: ${it.message}" )
            }
    }

    private fun chatDocument(id: String) = firestore
        .collection(COLLECTION_CHATS)
        .document(id)


    private fun createChatAndGetChatId(
        chatUser: ChatUser,
        me: ChatUser,
        onGetChatIdResult: CallBack<String, String>
    ) {
        firestore
            .collection(COLLECTION_CHATS)
            .add(mapOf("o" to "")) // just to create a new document
            .addOnSuccessListener {
                val chatId = it.id
                createChatForUser(me, chatUser, chatId)
                    .addOnSuccessListener {
                        if (chatUser.id!! != me.id!!)
                            createChatForUser(chatUser, me, chatId)
                                .addOnSuccessListener {
                                    onGetChatIdResult.onSuccess(chatId)
                                }
                                .addOnFailureListener {
                                    CommonUtil.log("createChatAndGetChatId createUserChat(chatUser) failed ${it.message}")
                                }
                        else onGetChatIdResult.onSuccess(chatId)
                    }
                    .addOnFailureListener {
                        CommonUtil.log("createChatAndGetChatId createUserChat(me) failed ${it.message}")
                    }
            }
            .addOnFailureListener {
                CommonUtil.log("createChatAndGetChatId createChat failed ${it.message}")
            }
    }

    private fun createChatForUser(
        meUser: ChatUser,
        otherUser: ChatUser,
        chatId: String
    ): Task<Void> {
        return userDocument(meUser.id)
            .collection(COLLECTION_CHATS)
            .document(chatId)
            .set(Chat(otherUser))
    }

}