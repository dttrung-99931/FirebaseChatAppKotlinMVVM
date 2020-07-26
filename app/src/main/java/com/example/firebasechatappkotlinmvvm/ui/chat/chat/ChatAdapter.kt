package com.example.firebasechatappkotlinmvvm.ui.chat.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.util.extension.equalDay
import com.example.firebasechatappkotlinmvvm.util.extension.format
import kotlinx.android.synthetic.main.item_chat_me.view.*
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(val onMsgClickListener: OnItemClickListener<Messagee>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    lateinit var meId: String
    var messages: MutableList<Messagee> = ArrayList()

    companion object {
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
        notifyItemInserted(messages.size - 1)
    }

    // Used to check whether showing short or long message time
    // if long time between 2 continuous messages
    var theSameDay: Date? = null

    inner class ChatViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bindView(position: Int) {
            setData(position)
            itemView.setOnClickListener {
                onMsgClickListener.onItemClicked(
                    position,
                    messages[position]
                )
                if (itemView.mTvTime.visibility == View.GONE)
                    itemView.mTvTime.visibility = View.VISIBLE
                else itemView.mTvTime.visibility = View.GONE
            }
        }

        private fun setData(position: Int) {
            val message = messages[position]
            itemView.mTvMsg.text = message.content
            setTime(message, position)
            showTimeIfLongSpace(message, position)
        }

        private fun setTime(message: Messagee, position: Int) {
            if (message.createdAt == null) {
                itemView.mTvTime.text = ""
                return
            }

            if (theSameDay != null && theSameDay!!.equalDay(message.createdAt))
                itemView.mTvTime.text = message.createdAt?.format("HH:mm")
            else {
                itemView.mTvTime.text = message.createdAt?.format("HH:mm dd/MM/yyyy")
                theSameDay = message.createdAt
            }
        }

        private fun showTimeIfLongSpace(message: Messagee, position: Int) {
            if (message.createdAt == null) return;
            if (layoutPosition > 0) {
                val prevMessage = messages[position - 1]
                val timeSpace = message.createdAt?.time!! - prevMessage.createdAt?.time!!
                if (timeSpace > 60 * 60 * 1000)
                    itemView.mTvTime.visibility = View.VISIBLE
            } else itemView.mTvTime.visibility = View.VISIBLE
        }
    }
}
