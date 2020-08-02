package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile.change_password

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ChangePasswordFragmentProvider {
    @ContributesAndroidInjector(modules = [ChangePasswordFragmentModule::class])
    abstract fun provideChangePasswordFragmentFactory() : ChangePasswordDialog
}