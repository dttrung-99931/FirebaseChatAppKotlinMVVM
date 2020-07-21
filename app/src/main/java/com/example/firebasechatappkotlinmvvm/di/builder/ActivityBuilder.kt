package com.example.firebasechatappkotlinmvvm.di.builder

import com.example.firebasechatappkotlinmvvm.ui.auth.AuthActivity
import com.example.firebasechatappkotlinmvvm.ui.auth.login.LoginFragmentProvider
import com.example.firebasechatappkotlinmvvm.ui.auth.sign_up.SignUpFragmentProvider
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivity
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivityModule
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.DashboardFragmentProvider
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat.ChatListFragmentProvider
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile.ProfileFragmentProvider
import com.example.firebasechatappkotlinmvvm.ui.start.StartActivity
import com.example.firebasechatappkotlinmvvm.ui.start.StartActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        DashboardFragmentProvider::class,
        ChatListFragmentProvider::class,
        ProfileFragmentProvider::class
    ])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [
        LoginFragmentProvider::class, SignUpFragmentProvider::class])
    abstract fun bindAuthActivity(): AuthActivity

    @ContributesAndroidInjector(modules = [StartActivityModule::class])
    abstract fun bindStartActivity(): StartActivity


}