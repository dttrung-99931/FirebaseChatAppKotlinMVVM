package com.example.firebasechatappkotlinmvvm.ui.auth

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.ui.auth.login.LoginFragment
import com.example.firebasechatappkotlinmvvm.ui.auth.sign_up.SignUpFragment


/**
 * Created by Trung on 7/12/2020
 */
class AuthFramentPagerAdapter(fm: FragmentManager, val context: Context) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return if (position == 0) LoginFragment()
        else SignUpFragment();
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) context.getString(R.string.login)
        else context.getString(R.string.sign_up)
    }
}