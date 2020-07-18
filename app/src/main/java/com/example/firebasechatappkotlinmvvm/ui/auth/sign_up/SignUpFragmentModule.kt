package com.example.firebasechatappkotlinmvvm.ui.auth.sign_up

import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class SignUpFragmentModule {
    @Provides
    fun provideSignUpViewModel(userRepo: UserRepo) = SignUpViewModel(userRepo)

    @Provides
    fun provideFactory(provider: Provider<SignUpViewModel>)
        = SignUpViewModel.Factory(provider)
}