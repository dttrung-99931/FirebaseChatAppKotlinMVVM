package com.example.firebasechatappkotlinmvvm.di.module

import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthService
import com.example.firebasechatappkotlinmvvm.data.remote.firebase_auth.FireBaseAuthServiceImpl
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreService
import com.example.firebasechatappkotlinmvvm.data.remote.firestore.FireStoreServiceImpl
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Trung on 7/10/2020
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFireBaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFireStoreService(fireStoreServiceImpl: FireStoreServiceImpl):
            FireStoreService = fireStoreServiceImpl

    @Singleton
    @Provides
    fun provideFireBaseAuthService(fireBaseAuthServiceImpl: FireBaseAuthServiceImpl):
            FireBaseAuthService = fireBaseAuthServiceImpl

    @Singleton
    @Provides
    fun provideUserRepo(userRepoImpl: UserRepoImpl): UserRepo = userRepoImpl

}