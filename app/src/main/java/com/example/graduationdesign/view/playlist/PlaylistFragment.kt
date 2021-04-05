package com.example.graduationdesign.view.playlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.databinding.PlaylistFragmentBinding
import com.example.graduationdesign.model.bean.playlist_bean.Tag
import com.example.graduationdesign.view.playlist.adapter.PlaylistAdapter
import com.example.graduationdesign.view.playlist.adapter.ViewPager2Adapter
import com.example.graduationdesign.view.playlist.subview.SubFragment
import com.example.imitationqqmusic.model.tools.ScreenUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PlaylistFragment : BaseFragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private lateinit var binding: PlaylistFragmentBinding
    private lateinit var viewModel: PlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaylistFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initToolbar() {
        binding.playlistToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
        viewModel.initInternetModel(requireContext())
        initToolbar()

        viewModel.tabItemNameList.observe(viewLifecycleOwner, {
            initViewPagerAndTab(it)
        })

        if (viewModel.tabItemNameList.value == null){
            viewModel.getTags()
        }
    }

    private fun initViewPagerAndTab(list: ArrayList<Tag>) {
        val viewpager2Adapter = object : FragmentStateAdapter(this){
            override fun getItemCount() = list.size

            override fun createFragment(position: Int): Fragment {
                return SubFragment.newInstance(list[position], viewModel)
            }
        }

        binding.playlistViewpager2.apply {
            adapter = viewpager2Adapter
            offscreenPageLimit = 2
        }

        TabLayoutMediator(
            binding.playlistTabLayout,
            binding.playlistViewpager2
        ) { tab: TabLayout.Tab, i: Int ->
            tab.text = list[i].name
        }.attach()
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getToolbarView(): Toolbar? {
        return null
    }
}