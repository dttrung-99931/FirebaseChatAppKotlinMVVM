package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import kotlinx.android.synthetic.main.item_chat.view.mImgAvatar
import kotlinx.android.synthetic.main.item_chat.view.mTvNickname

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

    fun updateChat(it: Chat?) {
        for (i in chats.indices)
            if (chats[i].chatUser.id == it!!.chatUser.id) {
                chats[i].chatUser = it.chatUser
                notifyItemChanged(i)
                break
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

            setUserStatus(chatUser)

            setAvatar(chatUser)
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
