package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat

import androidx.lifecycle.ViewModelProviders
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.FragmentChatListBinding
import com.example.firebasechatappkotlinmvvm.databinding.FragmentDashboardBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import javax.inject.Inject

class ChatListFragment : BaseFragment<FragmentChatListBinding, ChatListViewModel>() {
    @Inject
    lateinit var mFactory: ChatListViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_chat_list
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): ChatListViewModel {
        return ViewModelProviders
            .of(this, mFactory)[ChatListViewModel::class.java]
    }

    override fun setupViews() {
    }

    override fun observe() {
    }

}