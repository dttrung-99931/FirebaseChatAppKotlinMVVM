package com.example.firebasechatappkotlinmvvm.ui.main.dashboard

import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class DashboardFragmentModule {
    @Provides
    fun provideDashboardViewModel(userRepo: UserRepo) = DashboardViewModel(userRepo)

    @Provides
    fun provideFactory(provider: Provider<DashboardViewModel>)
        = DashboardViewModel.Factory(provider)
}