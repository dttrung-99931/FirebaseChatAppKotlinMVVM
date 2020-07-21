package com.example.firebasechatappkotlinmvvm.ui.main.dashboard

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat.ChatListFragment
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user.SearchUserFragment


/**
 * Created by Trung on 7/12/2020
 */
class DashboardFramentPagerAdapter(fm: FragmentManager, val context: Context) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return if (position == 0) ChatListFragment()
        else ChatListFragment();
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) context.getString(R.string.chat)
        else context.getString(R.string.explore)
    }
}