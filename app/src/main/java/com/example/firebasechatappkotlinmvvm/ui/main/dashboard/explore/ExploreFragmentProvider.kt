package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class ExploreFragmentProvider {
    @ContributesAndroidInjector(modules = [ExploreFragmentModule::class])
    abstract fun provideDashboardFragmentFactory() : ExploreFragment
}