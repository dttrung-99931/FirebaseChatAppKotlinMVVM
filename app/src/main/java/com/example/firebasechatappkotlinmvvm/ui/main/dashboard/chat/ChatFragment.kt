package com.example.firebasechatappkotlinmvvm.ui.main.dashboard.chat

import androidx.lifecycle.ViewModelProviders
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.FragmentChatListBinding
import com.example.firebasechatappkotlinmvvm.databinding.FragmentExploreBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_explore.*
import javax.inject.Inject

class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreViewModel>() {
    @Inject
    lateinit var mFactory: ExploreViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_chat
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): ExploreViewModel {
        return ViewModelProviders
            .of(this, mFactory)[ExploreViewModel::class.java]
    }

    override fun setupViews() {
        mSearchBar.setOnClickListener {
            navigate(R.id.action_dashboardFragment_to_searchUserFragment)
        }
    }

    override fun observe() {
    }

}