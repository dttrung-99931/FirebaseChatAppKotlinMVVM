package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.mImgAvatar

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
    }

}
