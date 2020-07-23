package com.example.firebasechatappkotlinmvvm.ui.main.dashboard

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.firebasechatappkotlinmvvm.BR
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.databinding.FragmentDashboardBinding
import com.example.firebasechatappkotlinmvvm.databinding.FragmentLoginBinding
import com.example.firebasechatappkotlinmvvm.ui.base.BaseFragment
import com.example.firebasechatappkotlinmvvm.ui.main.MainActivity
import com.example.firebasechatappkotlinmvvm.util.AppConstants
import kotlinx.android.synthetic.main.fragment_dashboard.*
import javax.inject.Inject

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {
    @Inject
    lateinit var mFactory: DashboardViewModel.Factory

    override fun getLayoutResId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun getVMBindingVarId(): Int {
        return BR.viewModel;
    }

    override fun getVM(): DashboardViewModel {
        return ViewModelProviders
            .of(this, mFactory)[DashboardViewModel::class.java]
    }

    override fun setupViews() {
        setupViewPager()
    }

    private fun setupViewPager() {
        mViewPager.adapter = DashboardFramentPagerAdapter(
            childFragmentManager, requireContext())
        mTabLayout.setupWithViewPager(mViewPager)
        mTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_chat_24px)
        mTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_explore_24px)
        mTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_profile_24px)
        mViewPager.offscreenPageLimit = 2
    }

    override fun observe() {
    }

}