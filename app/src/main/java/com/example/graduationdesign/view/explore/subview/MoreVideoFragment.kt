package com.example.graduationdesign.view.explore.subview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.FragmentMoreVideoBinding
import com.example.graduationdesign.view.explore.ExploreViewModel
import com.example.graduationdesign.view.explore.adapter.VideoListAdapter
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils

class MoreVideoFragment : Fragment() {


    private lateinit var binding: FragmentMoreVideoBinding
    private lateinit var viewModel: ExploreViewModel
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreVideoBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initRecyclerView() {
        val adapter = VideoListAdapter(ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(requireContext(), 20f))
        binding.moreRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.videoList.observe(requireActivity(), {
            adapter.submitList(it)
        })

        if (viewModel.videoList.value == null)
            viewModel.getVideoList(mainViewModel.user.value)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ExploreViewModel.newInstance(requireActivity())
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        initRecyclerView()
    }
}