package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.profile.change_password

import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class ChangePasswordFragmentModule {
    @Provides
    fun provideChangePasswordViewModel(userRepo: UserRepo) = ChangePasswordViewModel(userRepo)

    @Provides
    fun provideFactory(provider: Provider<ChangePasswordViewModel>)
        = ChangePasswordViewModel.Factory(provider)
}