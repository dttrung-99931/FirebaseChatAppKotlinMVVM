package com.example.firebasechatappkotlinmvvm.ui.auth.sign_up

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class SignUpFragmentProvider {
    @ContributesAndroidInjector(modules = [SignUpFragmentModule::class])
    abstract fun provideLoginFragmentFactory() : SignUpFragment
}