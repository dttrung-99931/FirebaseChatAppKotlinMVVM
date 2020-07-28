package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.Chat
import com.example.firebasechatappkotlinmvvm.databinding.FragmentChatListBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.ui.chat.ChatActivity
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import kotlinx.android.synthetic.main.fragment_chat_list.*
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
        setChatListRecyclerView()
    }

    private val onChatItemClick: OnItemClickListener<Chat> =
        object : OnItemClickListener<Chat> {
            override fun onItemClicked(position: Int, itemData: Chat) {
                ChatActivity.open(requireContext(), itemData.chatUser)
            }
        }

    private val chatListAdapter = ChatListAdapter(onChatItemClick)

    private fun setChatListRecyclerView() {
        chatListAdapter.meUserId = vm.meUserId
        mRecyclerView.adapter = chatListAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun observe() {
        vm.chats.observe(this, Observer {
            chatListAdapter.chats = it.toMutableList()
            chatListAdapter.notifyDataSetChanged()
        })

        vm.changedChat.observe(this, Observer {
            chatListAdapter.updateChat(it)
            vm.onUpdateChangedChatComplete()
        })
    }

}