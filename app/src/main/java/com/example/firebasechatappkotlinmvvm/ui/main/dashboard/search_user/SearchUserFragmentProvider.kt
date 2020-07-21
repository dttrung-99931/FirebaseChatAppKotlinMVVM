package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Trung on 7/10/2020
 */
@Module
abstract class SearchUserFragmentProvider {
    @ContributesAndroidInjector(modules = [SearchUserFragmentModule::class])
    abstract fun provideSeachUserFragmentFactory() : SearchUserFragment
}