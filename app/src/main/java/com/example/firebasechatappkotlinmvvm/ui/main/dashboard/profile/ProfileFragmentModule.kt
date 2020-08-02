package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile

import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class ProfileFragmentModule {
    @Provides
    fun provideProfileViewModel(userRepo: UserRepo) = ProfileViewModel(userRepo)

    @Provides
    fun provideFactory(provider: Provider<ProfileViewModel>)
        = ProfileViewModel.Factory(provider)
}