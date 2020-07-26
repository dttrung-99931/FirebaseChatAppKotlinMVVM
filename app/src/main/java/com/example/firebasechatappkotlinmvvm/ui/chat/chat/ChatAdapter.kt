package com.example.firebasechatappkotlinmvvm.ui.chat.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Messagee
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.util.extension.format
import com.example.firebasechatappkotlinmvvm.util.extension.subInMilis
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
            showTimeIfLongTimeSpacing(message, position)
        }

        // Set time for message
        // If the next msg has time with the same day/month/year
        // then just show time in HH:mm for it
        // Otherwise show full time in HH:mm dd/MM/yyyy
        private fun setTime(message: Messagee, position: Int) {
            if (message.createdAt == null) {
                itemView.mTvTime.text = ""
                return
            }

            val pattern: String = createPatternByCurDate(message.createdAt)

            itemView.mTvTime.text = message.createdAt?.format(pattern)
        }

        private fun createPatternByCurDate(createdAt: Date): String {
            val now = Calendar.getInstance().time
            return if (createdAt.year == now.year)
                if (createdAt.month == now.month)
                    if (createdAt.day == now.day) "HH:mm"
                    else "HH:mm EEE"
                else "HH:mm MMM, dd"
            else "HH:mm MMM, dd, yyyy"
        }

        // By default msg time is gone
        // This func check if the spacing time is long then show the time
        private fun showTimeIfLongTimeSpacing(message: Messagee, position: Int) {
            if (message.createdAt == null) {
                // When message.createdAt = null and If the
                // recycler item view is visible, then hide it
                if (itemView.mTvTime.visibility == View.VISIBLE)
                    itemView.mTvTime.visibility = View.GONE
                return
            }

            val timeSpacing: Long
            if (position > 0)
                timeSpacing = message.createdAt.subInMilis(
                    messages[position-1].createdAt!!)
            else timeSpacing = messages[1].createdAt!!.subInMilis(
                message.createdAt)

            if (timeSpacing > 2 * 60 * 1000) // timeSpacing > 2 minutes
                itemView.mTvTime.visibility = View.VISIBLE
        }
    }
}
