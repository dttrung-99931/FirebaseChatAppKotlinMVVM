package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat_list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.UserChat
import com.example.firebasechatappkotlinmvvm.databinding.FragmentChatListBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.ui.chat.ChatActivity
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.fragment_chat_list.*
import javax.inject.Inject

class ChatListFragment : BaseFragment<FragmentChatListBinding, ChatListViewModel>() {
    @Inject
    lateinit var mVMFactory: ChatListViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_chat_list
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): ChatListViewModel {
        return ViewModelProviders
            .of(this, mVMFactory)[ChatListViewModel::class.java]
    }

    override fun setupViews() {
        setChatListRecyclerView()
    }

    private val onUserChatItemClick: OnItemClickListener<UserChat> =
        object : OnItemClickListener<UserChat> {
            override fun onItemClicked(position: Int, itemData: UserChat) {
                ChatActivity.open(requireContext(), itemData.chatUser)
            }
        }

    private val chatListAdapter = ChatListAdapter(onUserChatItemClick)

    private fun setChatListRecyclerView() {
        chatListAdapter.meUserId = vm.meUserId
        mRecyclerView.adapter = chatListAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun observe() {
        vm.cachedUserChats.observe(this, Observer {
            if (it.isEmpty() && vm.loadedRefreshUserChats){
                if (parentFragment is DashboardFragment) {
                    (parentFragment as DashboardFragment).suggestExploreFriendWithDelay()
                }
            }
            else {
                chatListAdapter.userChats = it.toMutableList()
                chatListAdapter.notifyDataSetChanged()
            }
        })

        vm.changedOrAddedUserChat.observe(this, Observer {
            chatListAdapter.updateOrAddChat(it)
            vm.onUpdateUserChatComplete()
        })

        vm.changedAppUser.observe(this, Observer {
            chatListAdapter.updateUserStatusOrAddChat(it)
            vm.onUpdateChatUserComplete()
        })
    }
}