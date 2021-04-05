package com.example.graduationdesign.view.rankinglist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.databinding.RankingListFragmentBinding
import com.example.graduationdesign.view.rankinglist.adapter.RankingItemOfficialAdapter
import com.example.graduationdesign.view.rankinglist.adapter.RankingItemOtherAdapter
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils

class RankingListFragment : Fragment() {

    companion object {
        fun newInstance() = RankingListFragment()
    }

    private lateinit var viewModel: RankingListViewModel
    private lateinit var binding: RankingListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RankingListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RankingListViewModel::class.java)
        viewModel.initInternetModel(requireContext())

        binding.rankingNsvContainer.visibility = View.INVISIBLE
        binding.rankingListProgress.visibility = View.VISIBLE

        binding.rankingListToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val officialAdapter = RankingItemOfficialAdapter(
            ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(requireContext(), 50f)
        )

        val otherAdapter = RankingItemOtherAdapter(
            ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(requireContext(), 50f)
        )

        binding.rankingRecyclerOfficial.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = officialAdapter
        }

        binding.rankingRecyclerOther.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = otherAdapter
        }

        viewModel.rankingList.observe(viewLifecycleOwner, { map ->
            officialAdapter.submitList(map["official"])
            otherAdapter.submitList(map["other"])
            binding.rankingNsvContainer.visibility = View.VISIBLE
            binding.rankingListProgress.visibility = View.GONE
        })

        viewModel.getRankingList()
    }

}