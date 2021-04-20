package com.example.graduationdesign.view.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.databinding.MusicClubFragmentBinding
import com.example.graduationdesign.view.club.adapter.AdviceItemAdapter
import com.example.graduationdesign.view.club.adapter.AdviceSongItemAdapter
import com.example.graduationdesign.view.club.adapter.MusicClubRecyclerviewAdapter
import com.example.graduationdesign.view.club.adapter.ViewPagerBannerAdapter
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils

class MusicClubFragment : BaseFragment() {

    private lateinit var binding: MusicClubFragmentBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MusicClubFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setDataModel(requireContext())
        searchBoxWidth(binding.musicClubToolbar.tvSearch, R.id.action_musicClubFragment_to_searchFragment)
        initViewPager2()
        initRecyclerView()
        initSwipeRefreshLayout()
        initOnClick()
    }

    private fun navigate(action: Int) {
        Navigation.findNavController(requireActivity(), R.id.main_container)
            .navigate(action)
    }

    private fun initOnClick() {
        binding.linearLabelDaily.setOnClickListener {
            navigate(R.id.action_musicClubFragment_to_songListFragment)
        }

        binding.linearLabelRanking.setOnClickListener {
            navigate(R.id.action_musicClubFragment_to_rankingListFragment)
        }

        binding.linearLabelPlaylist.setOnClickListener {
            navigate(R.id.action_musicClubFragment_to_playlistFragment)
        }

        binding.linearLabelNewAlbum.setOnClickListener {
            navigate(R.id.action_musicClubFragment_to_newAlbumFragment)
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeLayout.isRefreshing = false
        binding.swipeLayout.isEnabled = false
    }

    private fun initRecyclerView() {
        val adviceItemAdapter = AdviceItemAdapter()
        val adviceItemAdapter2 = AdviceItemAdapter()
        val adviceSongItemAdapter = AdviceSongItemAdapter(ScreenUtils.getWidth(requireActivity()))


        binding.musicClubRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            adapter = MusicClubRecyclerviewAdapter(object :
                MusicClubRecyclerviewAdapter.MusicClubRecyclerviewAdapterCallBack {
                override fun initSubRecyclerViewCallBack(
                    recyclerview: RecyclerView,
                    position: Int
                ) {
                    when (position) {
                        0 -> {
                            initSubRecyclerView(recyclerview).adapter = adviceItemAdapter
                            if (viewModel.adviceImageList.value == null)
                                viewModel.getRecommendPlaylist()
                        }

                        1 -> {
                            initSubRecyclerView(recyclerview).adapter = adviceSongItemAdapter
                            if (viewModel.adviceNewSong.value == null)
                                viewModel.getRecommendNewSong()
                        }

                        2 -> {
                            initSubRecyclerView(recyclerview).adapter = adviceItemAdapter2
                            if (viewModel.adviceNewestAlbums.value == null)
                                viewModel.getNewestAlbum()
                        }
                    }
                }
            })
        }

        viewModel.adviceImageList.observe(viewLifecycleOwner, {
            println("=======================submitlist:")
            adviceItemAdapter.submitList(it)
        })

        viewModel.adviceNewestAlbums.observe(viewLifecycleOwner, {
            adviceItemAdapter2.submitList(it)
        })

        viewModel.adviceNewSong.observe(viewLifecycleOwner, {
            adviceSongItemAdapter.submitList(it)
        })
    }

    private fun initSubRecyclerView(
        recyclerView: RecyclerView
    ): RecyclerView {
        return recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL,
                false
            )
        }
    }

    private fun initViewPager2() {
        val adapter = ViewPagerBannerAdapter()
        binding.bannerViewpager2.adapter = adapter
        binding.bannerViewpager2.layoutParams.height =
            viewModel.getScaleHeight(
                ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(
                    requireContext(),
                    20f
                )
            ).toInt()

        println("=======================adapter set success:")

        binding.bannerViewpager2.registerOnPageChangeCallback(
            object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setPosition(position)
                    setIndicatorCheckedIndex(position - 1)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    viewModel.dragEnded(state)
                }
            })

        viewModel.bannerImageList.observe(viewLifecycleOwner, {
            if (it.size == 0) return@observe else {
                adapter.submitList(it) {
                    binding.bannerViewpager2.setCurrentItem(1, false)
                    addIndicator(it.size)
                    (binding.linearIndicatorContainer.getChildAt(0) as CheckBox).isChecked = true
                    viewModel.initTimer()
                }
            }
        })

        viewModel.currentBannerIndex.observe(viewLifecycleOwner, {
            binding.bannerViewpager2.setCurrentItem(it, false)
        })

        viewModel.currentBannerIndexWithAnimation.observe(viewLifecycleOwner, {
            binding.bannerViewpager2.setCurrentItem(it, true)
        })

        if (viewModel.bannerImageList.value?.size == 0)
            viewModel.getBannerList()
    }

    private fun setIndicatorCheckedIndex(index: Int) {
        val count = binding.linearIndicatorContainer.childCount
        if ((index == -1) or (index >= count)) return
        for (i in 0 until count) {
            (binding.linearIndicatorContainer[i] as CheckBox).isChecked = (i == index)
        }
    }

    private fun addIndicator(count: Int) {
        for (i in 0 until count) {
            val indicators = CheckBox(requireContext()).also {
                it.isEnabled = false
                it.buttonDrawable = null
                it.background = ContextCompat.getDrawable(requireContext(), R.drawable.indicator)
            }
            val layoutParameter =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)
                    .also {
                        it.height = DpPxUtils.dp2Px(requireContext(), 7f)
                        it.width = DpPxUtils.dp2Px(requireContext(), 7f)
                        it.weight = 1f
                        it.leftMargin = DpPxUtils.dp2Px(requireContext(), 3f)
                        it.rightMargin = DpPxUtils.dp2Px(requireContext(), 3f)
                    }
            binding.linearIndicatorContainer.addView(indicators, layoutParameter)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun getTitle(): String {
        return getString(R.string.item_music)
    }

    override fun getToolbarView(): Toolbar? {
        return binding.musicClubToolbar.toolbar
    }

}