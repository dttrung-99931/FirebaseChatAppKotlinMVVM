package com.example.firebasechatappkotlinmvvm.ui.main.dashboard

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class DashboardFragmentProvider {
    @ContributesAndroidInjector(modules = [DashboardFragmentModule::class])
    abstract fun provideDashboardFragmentFactory() : DashboardFragment
}