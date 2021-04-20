package com.example.graduationdesign.view.search.subFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.FragmentSearchResultBinding
import com.example.graduationdesign.model.SearchType
import com.example.graduationdesign.view.search.SearchViewModel
import com.example.graduationdesign.view.search.subFragment.search_result_sub.ComprehensiveFragment
import com.example.graduationdesign.view.search.subFragment.search_result_sub.OtherListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initViewPager2() {
        binding.vp2SearchResult.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 6
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ComprehensiveFragment()
                    1 -> OtherListFragment(SearchType.SINGLE)
                    2 -> OtherListFragment(SearchType.VIDEO)
                    3 -> OtherListFragment(SearchType.ARTIST)
                    4 -> OtherListFragment(SearchType.PLAYLIST)
                    else -> OtherListFragment(SearchType.ALBUM)
                }
            }
        }

        TabLayoutMediator(binding.tabSearchResult, binding.vp2SearchResult) { tab, position ->
            tab.text = when (position) {
                0 -> "综合"
                1 -> "单曲"
                2 -> "视频"
                3 -> "歌手"
                4 -> "歌单"
                else -> "专辑"
            }
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = SearchViewModel.newInstance(this)
        viewModel.navigateEnable = false
        initViewPager2()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.navigateEnable = true
    }
}