package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.explore

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.data.repo.chat.ChatUser
import com.example.firebasechatappkotlinmvvm.data.repo.user.AppUser
import com.example.firebasechatappkotlinmvvm.databinding.FragmentExploreBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemClickListener
import com.example.firebasechatappkotlinmvvm.ui.base.OnItemWithPositionClickListener
import com.example.firebasechatappkotlinmvvm.ui.base.OptionBottomSheetDialogFragment
import com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat.ChatFragment
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.search_bar.*
import javax.inject.Inject

class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreViewModel>() {
    @Inject
    lateinit var mFactory: ExploreViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_explore
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): ExploreViewModel {
        return ViewModelProviders
            .of(this, mFactory)[ExploreViewModel::class.java]
    }

    override fun setupViews() {
        mEdtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.onSearchTextChanged()
            }
        })

        setupSearchResultRecyclerView()
    }

    private val onSearchUserClickedCallBack: OnItemClickListener<AppUser> =
        object : OnItemClickListener<AppUser> {
            override fun onItemClicked(position: Int, user: AppUser) {
                showUserOptionBottomSheetOnClickSearch(position, user)
            }
        }

    private fun showUserOptionBottomSheetOnClickSearch(
        position: Int,
        user: AppUser
    ) {
        OptionBottomSheetDialogFragment(
            AppConstants.STR_IDS_CLICK_SEARCH_USER_OPTION,
            object : OnItemWithPositionClickListener {
                override fun onItemWithPositionClicked(position: Int) {
                    when (position) {
                        0 -> navigateWithParcelableData(
                            R.id.action_dashboardFragment_to_chatFragment,
                            ChatUser.fromAppUser(user), ChatFragment.KEY_CHAT_USER)
                    }
                }
            }
        ).show(childFragmentManager, "")
    }

    private val mSearchUserAdapter = SearchUserResultAdapter(onSearchUserClickedCallBack)

    private fun setupSearchResultRecyclerView() {
        mRecyclerView.adapter = mSearchUserAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun observe() {
        vm.searchUsers.observe(this, Observer {
            mSearchUserAdapter.searchUsers = it
            mSearchUserAdapter.notifyDataSetChanged()
            CommonUtil.log("Search results: ${it.size}")
        })
    }
}