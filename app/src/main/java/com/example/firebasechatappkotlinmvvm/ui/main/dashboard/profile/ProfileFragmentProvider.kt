package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ProfileFragmentProvider {
    @ContributesAndroidInjector(modules = [ProfileFragmentModule::class])
    abstract fun provideProfileFragmentFactory() : ProfileFragment
}