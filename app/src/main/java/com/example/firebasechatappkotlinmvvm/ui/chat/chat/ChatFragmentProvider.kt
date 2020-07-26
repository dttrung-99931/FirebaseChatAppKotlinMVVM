package com.example.firebasechatappkotlinmvvm.ui.chat.chat

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ChatFragmentProvider {
    @ContributesAndroidInjector(modules = [ChatFragmentModule::class])
    abstract fun provideDashboardFragmentFactory() : ChatFragment
}