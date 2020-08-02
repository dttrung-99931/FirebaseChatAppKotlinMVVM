package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ChatListFragmentProvider {
    @ContributesAndroidInjector(modules = [ChatListFragmentModule::class])
    abstract fun provideChatListFragmentFactory() : ChatListFragment
}