package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore

import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class ExploreFragmentModule {
    @Provides
    fun provideDashboardViewModel(userRepo: UserRepo) = ExploreViewModel(userRepo)

    @Provides
    fun provideFactory(provider: Provider<ExploreViewModel>)
        = ExploreViewModel.Factory(provider)
}