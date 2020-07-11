package com.example.firebasechatappkotlinmvvm.ui.login

import com.example.firebasechatappkotlinmvvm.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class LoginFragmentBinder {
    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    abstract fun bindLoginFragment() : LoginFragment
}