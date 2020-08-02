package com.example.firebasechatappkotlinmvvm.ui.chat.chat

import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatRepo
import com.example.firebasechatappkotlinmvvm.data.repo.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Provider


/**
 * Created by Trung on 7/10/2020
 */
@Module
class ChatFragmentModule {
    @Provides
    fun provideChatViewModel(chatRepo: ChatRepo, userRepo: UserRepo)
            = ChatViewModel(chatRepo, userRepo)

    @Provides
    fun provideFactory(provider: Provider<ChatViewModel>)
        = ChatViewModel.Factory(provider)
}