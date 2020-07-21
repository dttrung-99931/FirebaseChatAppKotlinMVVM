package com.example.firebasechatappkotlinmvvm.ui.main.dashboard

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat.ChatListFragment
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore.ExploreFragment
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile.ProfileFragment
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user.SearchUserFragment


/**
 * Created by Trung on 7/12/2020
 */
class DashboardFramentPagerAdapter(fm: FragmentManager, val context: Context) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChatListFragment()
            1 -> ExploreFragment()
            else -> ProfileFragment()
        }
    }

    override fun getCount() = 3
}