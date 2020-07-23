package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import kotlinx.android.synthetic.main.item_chat_me.view.*

class ChatAdapter(val onMsgClickListener: OnItemClickListener<Messagee>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    lateinit var meId: String
    var messages: MutableList<Messagee> = ArrayList()

    companion object{
        const val VIEW_TYPE_CHAT_ME = 0
        const val VIEW_TYPE_CHAT_OTHER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderUserId == meId)
            VIEW_TYPE_CHAT_ME
        else VIEW_TYPE_CHAT_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType == VIEW_TYPE_CHAT_ME) {
            return ChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_me, parent, false)
            )
        }
        return ChatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_other, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bindView(position)
    }

    fun addMessage(message: Messagee) {
        messages.add(message)
        notifyItemInserted(messages.size-1)
    }

    inner class ChatViewHolder(itemView: View) : BaseViewHolder(itemView){
        override fun bindView(position: Int) {
            setData(position)
            itemView.setOnClickListener {
                onMsgClickListener.onItemClicked(position,
                    messages[position]
            ) }
        }

        private fun setData(position: Int) {
            val message = messages[position]
            itemView.mTvMsg.text = message.content
        }
    }
}
