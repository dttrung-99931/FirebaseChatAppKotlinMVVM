package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.example.firebasechatappkotlinmvvm.util.extension.format
import kotlinx.android.synthetic.main.fragment_profile.view.mImgAvatar
import kotlinx.android.synthetic.main.fragment_profile.view.mTvNickname
import kotlinx.android.synthetic.main.item_chat.view.*

class SearchUserResultAdapter (val onItemClickedCallBack: OnItemClickListener<AppUser>):
    RecyclerView.Adapter<SearchUserResultAdapter.SearchUserResultViewholder>() {

    var searchUsers: List<AppUser>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserResultViewholder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_user_result, parent, false)
        return SearchUserResultViewholder(item)
    }

    override fun getItemCount(): Int {
        if (searchUsers != null) return searchUsers!!.size
        return 0
    }

    override fun onBindViewHolder(holder: SearchUserResultViewholder, position: Int) {
        holder.bindView(position)
    }

    inner class SearchUserResultViewholder(itemView: View) : BaseViewHolder(itemView) {
        override fun bindView(position: Int) {
            val clickPos = layoutPosition
            val user = searchUsers!!.get(clickPos)
            setData(user)
            itemView.setOnClickListener {
                onItemClickedCallBack.onItemClicked(clickPos, user)
            }
        }

        private fun setData(user: AppUser) {
            itemView.mTvNickname.text = user.nickname
            setAvatar(user)

            setOfflineTime(user)

            setUserStatus(user)
        }

        private fun setAvatar(user: AppUser) {
            if (user.avatarUrl.isNotEmpty())
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .placeholder(R.drawable.ic_no_avatar_50px)
                    .centerCrop()
                    .into(itemView.mImgAvatar)
            else if (isRecyclable)
                Glide.with(itemView.context)
                    .load(R.drawable.ic_no_avatar_50px)
                    .centerCrop()
                    .into(itemView.mImgAvatar)
        }

        private fun setOfflineTime(user: AppUser) {
            if (user.online || user.offlineAt == null)
                itemView.mTvOfflineTime.visibility = View.GONE
            else {
                val offlineTime = user
                    .offlineAt?.format(
                        CommonUtil.createDatePattern(user.offlineAt!!)
                    )
                itemView.mTvOfflineTime.text = offlineTime
                itemView.mTvOfflineTime.visibility = View.VISIBLE
            }
        }

        private fun setUserStatus(user: AppUser) {
            val statusIcId = if (user.online)
                R.drawable.ic_status_online
            else R.drawable.ic_status_offline
            itemView.mTvNickname
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, statusIcId, 0
                )
        }
    }

}
