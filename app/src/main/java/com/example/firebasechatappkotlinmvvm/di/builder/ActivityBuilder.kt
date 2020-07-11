package com.example.firebasechatappkotlinmvvm.di.builder

import com.example.firebasechatappkotlinmvvm.ui.login.LoginFragmentBinder
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivity
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class, LoginFragmentBinder::class])
    abstract fun bindMainActivity(): MainActivity
}