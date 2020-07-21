package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.search_user

import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class SearchUserFragmentModule {
    @Provides
    fun provideDashboardViewModel(userRepo: UserRepo) = SearchUserViewModel(userRepo)

    @Provides
    fun provideFactory(provider: Provider<SearchUserViewModel>)
        = SearchUserViewModel.Factory(provider)
}