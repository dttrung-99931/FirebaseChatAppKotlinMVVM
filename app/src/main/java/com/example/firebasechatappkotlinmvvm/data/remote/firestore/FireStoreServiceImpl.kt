package com.example.firebasechatappkotlinmvvm.data.remote.firestore

import android.util.Log
import com.example.firebasechatappkotlinmvvm.data.callback.CallBack
import com.example.firebasechatappkotlinmvvm.data.callback.SingleCallBack
import com.example.firebasechatappkotlinmvvm.data.repo.chat.*
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreViewModel
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
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
        const val FIELD_IS_ONLINE = "online"
        const val FIELD_OFFLINE_AT = "offlineAt"
        const val FIELD_THUMB_MSG = "thumbMsg"
        const val FIELD_NEW_MSG_NUM = "newMsgNum"

        const val TAG = "FireStoreServiceImpl"
    }

    private var chatListenerRemover: ListenerRegistration? = null

    private val appUserListenerRemovers = mutableListOf<ListenerRegistration>()

    private val chatMetaListenerRemovers = mutableListOf<ListenerRegistration>()

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
        mSearchUsersCallBack: CallBack<ExploreViewModel.SearchUserResult, String>
    ) {
        val searchUserResult = ExploreViewModel.SearchUserResult(userOrEmail)
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
        mSearchUsersCallBack: CallBack<ExploreViewModel.SearchUserResult, String>,
        searchUserResult: ExploreViewModel.SearchUserResult
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

    override fun listenMessageEvent(
        chatId: String,
        onMessageEvent: CallBack<MessageEvent, String>,
        onListeningSetupResult: CallBack<String, String>
    ) {
        onListeningSetupResult.onSuccess(chatId)
        ignoredDocumentsChangedEventsAfterSetListener = false
        chatListenerRemover = chatDocument(chatId)
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
    }

    private fun notifyMessageEvent(
        value: QuerySnapshot,
        onMessageEvent: CallBack<MessageEvent, String>
    ) {
        if (ignoredDocumentsChangedEventsAfterSetListener)
            for (documentChange in value.documentChanges) {
                val message = documentChange.document.toObject(Messagee::class.java)
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
        // add the message to chat with chatId
        chatDocument(messageInfoProvider.chatId)
            .collection(COLLECTION_MESSAGES)
            .add(messageInfoProvider.message)
            .addOnSuccessListener {
                onSendMessageResult.onSuccess()
            }
            .addOnFailureListener {
                CommonUtil.log("send error ${it.message}")
            }

        // add thumb message, update new msg num to chat of receiver user
        val thumbMsg = messageInfoProvider.message.getThumbMsg()
        val metaChatUpdate = mapOf(
            FIELD_THUMB_MSG to thumbMsg,
            FIELD_NEW_MSG_NUM to FieldValue.increment(1)
        )
        userDocument(messageInfoProvider.message.receiverUserId)
            .collection(COLLECTION_CHATS)
            .document(messageInfoProvider.chatId)
            .update(metaChatUpdate)
            .addOnFailureListener {
                CommonUtil.log("send() metaChatUpdate error ${it.message}")
            }
    }

    lateinit var preTopMessageDocument: DocumentSnapshot

    override fun getFirstCachedMessagesThenRefresh(
        chatId: String,
        onGetMessagesResult: CallBack<List<Messagee>, String>,
        count: Long?
    ) {
        val pageSize = count ?: AppConstants.PAGE_SIZE_MSG

        chatDocument(chatId)
            .collection(COLLECTION_MESSAGES)
            // Do not implement pagination wit count param
            .orderBy(FIELD_CREATED_AT)
            .limitToLast(pageSize)
            .get(Source.CACHE)
            .addOnSuccessListener {
                onGetMessagesResult.onSuccess(
                    CommonUtil.toMessagesFromMessageDocuments(it.documents)
                )
                chatDocument(chatId)
                    .collection(COLLECTION_MESSAGES)
                    .orderBy(FIELD_CREATED_AT)
                    .limitToLast(pageSize.toLong())
                    .get()
                    .addOnSuccessListener { it1 ->
                        if (!it1.isEmpty) {
                            preTopMessageDocument = it1.first()
                            onGetMessagesResult.onSuccess(
                                CommonUtil.toMessagesFromMessageDocuments(it1.documents)
                            )
                        } else onGetMessagesResult.onSuccess(listOf())
                    }
            }
            .addOnFailureListener {
                CommonUtil.log("getLastMessages err: ${it.message}")
            }
    }

    override fun getNextMessages(
        chatId: String,
        onGetNextMessagesResult: CallBack<List<Messagee>, String>
    ) {

        chatDocument(chatId)
            .collection(COLLECTION_MESSAGES)
            .orderBy(FIELD_CREATED_AT)
            .endBefore(preTopMessageDocument)
            .limitToLast(AppConstants.PAGE_SIZE_MSG)
            .get()
            .addOnSuccessListener { it1 ->
                if (!it1.isEmpty) {
                    preTopMessageDocument = it1.first()
                    onGetNextMessagesResult.onSuccess(
                        CommonUtil.toMessagesFromMessageDocuments(it1.documents)
                    )
                } else onGetNextMessagesResult.onSuccess(listOf())
            }
            .addOnFailureListener {
                CommonUtil.log("getNextMessages err: ${it.message}")
            }
    }

    override fun removeCurEventMessageListener() {
        if (chatListenerRemover != null) {
            chatListenerRemover!!.remove()
            chatListenerRemover = null
        }
    }

    override fun getChats(
        userId: String,
        onGetChatResult: CallBack<List<Chat>, String>,
        count: Int?
    ) {
        userDocument(userId)
            .collection(COLLECTION_CHATS)
            .get(Source.CACHE)
            .addOnSuccessListener {
                // Display cached chats first
                val cachedChats = Chat.createList(it.documents)
                onGetChatResult.onSuccess(cachedChats)

                userDocument(userId)
                    .collection(COLLECTION_CHATS)
                    .get()
                    .addOnSuccessListener {
                        val refreshChats = Chat.createList(it.documents)
                        onGetChatResult.onSuccess(refreshChats)
                    }
                    .addOnFailureListener {
                        CommonUtil.log("getChats refresh error: ${it.message}")
                    }
            }
            .addOnFailureListener {
                CommonUtil.log("getChats error: ${it.message}")
            }
    }

    override fun updateUserOnline(user: FirebaseUser?) {
        userDocument(user!!.uid)
            .update(FIELD_IS_ONLINE, true)
    }

    override fun updateUserOffline(user: FirebaseUser?) {
        val map = mapOf(
            FIELD_IS_ONLINE to false,
            FIELD_OFFLINE_AT to FieldValue.serverTimestamp()
        )
        userDocument(user!!.uid)
            .update(map)
    }

    override fun listenAppUser(id: String, onChange: CallBack<AppUser, String>) {
        appUserListenerRemovers.add(
            userDocument(id)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        CommonUtil.log("listenAppUserChange error: ${error.message}")
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        val appUser = value.toObject(AppUser::class.java)
                        appUser!!.id = value.id
                        onChange.onSuccess(appUser)
                    }
                })
    }

    // Listen (me User) user.chats changes
    override fun listenChatMetaInUser(
        chat: Chat,
        meUserId: String,
        onChatChange: CallBack<Chat, String>
    ) {
        chatMetaListenerRemovers.add(
            userDocument(meUserId)
                .collection(COLLECTION_CHATS)
                .document(chat.id)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        CommonUtil.log("listenMetaChatInUserChange error: ${error.message}")
                        return@addSnapshotListener
                    }

                    if (value != null) {

                        val changedChat = value.toObject(Chat::class.java)

                        // Just update these fields, not chat.chatUser
                        // because chat.chatUser will be
                        // listen change in UserRepo - FirebaseAuthService
                        chat.newMsgNum = changedChat!!.newMsgNum
                        chat.thumbMsg = changedChat.thumbMsg

                        onChatChange.onSuccess(chat)
                    }
                }
        )
    }

    override fun resetNewMsg(meUserId: String, chatId: String) {
        userDocument(meUserId)
            .collection(COLLECTION_CHATS)
            .document(chatId)
            .update(FIELD_NEW_MSG_NUM, 0)
    }

    /**
     * Get @param(num) of random users from COLLECTION_USER
     * Need to exclude users who are in chat list of the user
     * */
    override fun getRandomUsers(num: Int, onGetRandomUsersResult: CallBack<List<AppUser>, String>) {
        firestore.collection(COLLECTION_USERS)
            .orderBy(FIELD_IS_ONLINE, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val randomUserDocuments = CommonUtil
                    .getRandomList<DocumentSnapshot>(it.documents, num)

                val users = AppUser.listFromUserDocuments(randomUserDocuments)
                onGetRandomUsersResult.onSuccess(users)
            }
            .addOnFailureListener {
                CommonUtil.log("Get random users error ${it.message} ")
            }
    }

    override fun removeCurAppUserListeners() {
        appUserListenerRemovers.forEach {
            it.remove()
        }
        appUserListenerRemovers.clear()
    }

    override fun removeCurChatMetaListeners() {
        chatMetaListenerRemovers.forEach {
            it.remove()
        }
        chatMetaListenerRemovers.clear()
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
                onGetChatIdResult.onSuccess(chatId)
                createChatForUser(me, chatUser, chatId)
                    .addOnSuccessListener {
                        if (chatUser.id!! != me.id!!)
                            createChatForUser(chatUser, me, chatId)
                                .addOnSuccessListener {
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