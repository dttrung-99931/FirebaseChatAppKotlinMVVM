package com.example.firebasechatappkotlinmvvm.ui.start

import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides


/**
 * Created by Trung on 7/10/2020
 */
@Module
public class StartActivityModule {

    @Provides
    fun provideStartViewModel(userRepo: UserRepo) = StartViewModel(userRepo)
}