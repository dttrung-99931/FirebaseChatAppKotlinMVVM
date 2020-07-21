package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.ui.base.BaseViewHolder
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemWithPositionClickListener
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import kotlinx.android.synthetic.main.item_profile_optiion.view.*

class ProfileOptionAdapter(val onItemClickListener: OnItemWithPositionClickListener) :
    RecyclerView.Adapter<ProfileOptionAdapter.ProfileOptionViewHolder>() {
    var a = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileOptionViewHolder {
        val view =  LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_optiion, parent, false)
        return ProfileOptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ProfileOptionViewHolder, position: Int) {
        holder.bindView(position)
    }

    inner class ProfileOptionViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun bindView(position: Int) {
            bindData(position)
            itemView.setOnClickListener {
                onItemClickListener.onItemWithPositionClicked(position)
            }
        }

        private fun bindData(position: Int) {
            var optionText = ""
            var imgProfileOptionResId = -1
            val context = itemView.context
            when (position) {
                0 -> {
                    optionText = context.getString(R.string.sign_out)
                    imgProfileOptionResId = R.drawable.ic_sign_out_50px
                }
                else -> {
                    optionText = context.getString(R.string.sign_out)
                    imgProfileOptionResId = R.drawable.ic_sign_out_50px
                }
            }

            if (optionText != ""){
                itemView.mTvProfileOption.text = optionText
            } else CommonUtil.log("ProfileOptionViewHolder.bindView unknown optionText")

            if (imgProfileOptionResId != -1)
                itemView.mImgProfileOption.setImageResource(imgProfileOptionResId)
            else CommonUtil.log("ProfileOptionViewHolder.bindView unknown imgProfileOptionResId")
        }

    }
}
