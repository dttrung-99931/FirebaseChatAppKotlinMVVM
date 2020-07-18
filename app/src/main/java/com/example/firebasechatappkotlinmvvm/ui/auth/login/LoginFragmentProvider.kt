package com.example.firebasechatappkotlinmvvm.ui.auth.login

import com.example.firebasechatappkotlinmvvm.ui.auth.sign_up.SignUpFragment
import com.example.firebasechatappkotlinmvvm.ui.auth.sign_up.SignUpFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class LoginFragmentProvider {
    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    abstract fun provideLoginFragmentFactory() : LoginFragment
}