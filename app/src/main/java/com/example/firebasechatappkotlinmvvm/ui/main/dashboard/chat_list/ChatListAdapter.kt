package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.example.firebasechatappkotlinmvvm.util.extension.format
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatListAdapter(
    val onChatClickListener: OnItemClickListener<Chat>
) :
    RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    // Used to determine self chat -> self chat user is always online
    lateinit var meUserId: String

    val areCachedChats = true

    var chats: MutableList<Chat> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holderList: ChatListViewHolder, position: Int) {
        holderList.bindView(position)
    }

    fun updateOrAddChat(it: Chat?) {
        var isChatUpdated = false
        for (i in chats.indices)
            if (chats[i].chatUser.id == it!!.chatUser.id) {
                // @Warning: do not assign chat[i] = it
                // because it just contain updated chat meta (newMsgNum, thumbMsg)
                chats[i].thumbMsg = it.thumbMsg
                chats[i].newMsgNum = it.newMsgNum
                notifyItemChanged(i)
                isChatUpdated = true
                break
            }
        if (!isChatUpdated) {
            addTopAndNotify(it!!)
        }
    }

    private fun addTopAndNotify(chat: Chat) {
        chats.add(0, chat)
        notifyItemInserted(0)
    }

    fun updateUserStatusOrAddChat(appUser: AppUser) {
        var isUserStatusUpdated = false
        for (i in chats.indices)
            if (chats[i].chatUser.id == appUser.id) {
                chats[i].chatUser = appUser.toChatUser()
                notifyItemChanged(i)
                isUserStatusUpdated = true
                break
            }
        if (!isUserStatusUpdated) {
            val chat = Chat(appUser.toChatUser())
            addTopAndNotify(chat)
        }
    }

    inner class ChatListViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bindView(position: Int) {
            setData(position)
            itemView.setOnClickListener {
                onChatClickListener.onItemClicked(
                    position,
                    chats[position]
                )
            }
        }

        private fun setData(position: Int) {
            val chat = chats[position]
            val chatUser = chat.chatUser

            itemView.mTvNickname.text = chatUser.nickname

            itemView.mTvThumbMsg.text =
                CommonUtil.getShortString(chat.thumbMsg,30)

            setNewMsgNum(chat)

            setOfflineTime(chatUser)

            setUserStatus(chatUser)

            setAvatar(chatUser)
        }

        private fun setNewMsgNum(chat: Chat) {
            if (chat.newMsgNum > 0) {
                val newDisplayMsgNum =
                    if (chat.newMsgNum <= 9) chat.newMsgNum.toString()
                    else "${chat.newMsgNum}+"
                itemView.mTvViewBadgeNewMsg.text = newDisplayMsgNum
                itemView.mTvViewBadgeNewMsg.visibility = View.VISIBLE
            } else itemView.mTvViewBadgeNewMsg.visibility = View.GONE
        }

        private fun setOfflineTime(chatUser: ChatUser) {
            if (chatUser.online || chatUser.offlineAt == null)
                itemView.mTvOfflineTime.visibility = View.GONE
            else {
                val offlineTime = chatUser
                    .offlineAt?.format(
                        CommonUtil.createDatePattern(chatUser.offlineAt!!)
                    )
                itemView.mTvOfflineTime.text = offlineTime
                itemView.mTvOfflineTime.visibility = View.VISIBLE
            }
        }

        private fun setUserStatus(chatUser: ChatUser) {
            val statusIcId = if (chatUser.online || chatUser.id == meUserId)
                R.drawable.ic_status_online
            else R.drawable.ic_status_offline
            itemView.mTvNickname
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, statusIcId, 0
                )
        }

        private fun setAvatar(chatUser: ChatUser) {
            if (chatUser.avatarUrl.isNotEmpty())
                Glide.with(itemView.context)
                    .load(chatUser.avatarUrl)
                    .placeholder(R.drawable.ic_no_avatar_50px)
                    .centerCrop()
                    .into(itemView.mImgAvatar)
            else if (isRecyclable)
                Glide.with(itemView.context)
                    .load(R.drawable.ic_no_avatar_50px)
                    .centerCrop()
                    .into(itemView.mImgAvatar)
        }
    }
}
