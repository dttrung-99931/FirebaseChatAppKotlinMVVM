package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_chat.view.mImgAvatar
import kotlinx.android.synthetic.main.item_chat.view.mTvNickname
import kotlinx.android.synthetic.main.item_text_msg_me.view.*

class ChatListAdapter(val onChatClickListener: OnItemClickListener<Chat>) :
    RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

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

    inner class ChatListViewHolder(itemView: View) : BaseViewHolder(itemView){
        override fun bindView(position: Int) {
            setData(position)
            itemView.setOnClickListener {
                onChatClickListener.onItemClicked(position,
                    chats[position]
            ) }
        }

        private fun setData(position: Int) {
            val chat = chats[position]
            val chatUser = chat.chatUser

            itemView.mTvNickname.text = chatUser!!.nickname

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
