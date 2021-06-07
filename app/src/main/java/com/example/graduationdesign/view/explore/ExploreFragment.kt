package com.example.graduationdesign.view.explore

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.databinding.ExploreFragmentBinding
import com.example.graduationdesign.view.explore.subview.MoreVideoFragment
import com.example.graduationdesign.view.explore.subview.MvFragment
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ExploreFragment : BaseFragment() {

    private lateinit var binding: ExploreFragmentBinding

    private lateinit var viewModel: ExploreViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExploreFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initViewPager2() {
//        binding.exploreViewpager2.isUserInputEnabled = false
        binding.exploreViewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int) = if (position == 0) {
                MvFragment.newInstance(null, null)
            } else {
                MoreVideoFragment()
            }
        }

        TabLayoutMediator(
            binding.exploreTab, binding.exploreViewpager2
        ) { tab, position ->
            when(position){
                0 -> tab.text = "MV"
                else -> tab.text = "视频"
            }
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ExploreViewModel.newInstance(requireActivity())
        mainActivityViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setModel(mainActivityViewModel.getDataModel())
        initViewPager2()
    }

    override fun toSetting(): Nothing? = null

    override fun getTitle(): String = getString(R.string.item_explore)

    override fun getToolbarView() = binding.exploreToolbar

}