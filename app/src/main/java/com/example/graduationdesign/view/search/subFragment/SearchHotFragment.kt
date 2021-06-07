package com.example.graduationdesign.view.search.subFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.databinding.LayoutSearchHotFragmentBinding
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.dialog.ConfirmDialog
import com.example.graduationdesign.view.search.SearchViewModel
import com.example.graduationdesign.view.search.adapter.HotListAdapter
import com.example.graduationdesign.view.search.adapter.SearchHistoryAdapter

class SearchHotFragment: Fragment() {

    private lateinit var binding: LayoutSearchHotFragmentBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSearchHotFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initTvHistory(){
        binding.ivSearchHotDelete.setOnClickListener {
            viewModel.deleteHistory()
            viewModel.getHistoryList()
        }

        val historyAdapter = SearchHistoryAdapter{
            it.keyWord.let { it1 -> viewModel.changeNavigateBehavior(it1) }
        }

        binding.searchHistoryRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = historyAdapter
        }

        viewModel.containerHide.observe(viewLifecycleOwner, {
            binding.searchContainer.visibility = it
            binding.searchHistoryRecyclerview.visibility = it
        })

        viewModel.searchHistoryList.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                viewModel.shouldHide(View.GONE)
                return@observe
            }
            historyAdapter.submitList(it)
            viewModel.shouldHide(View.VISIBLE)
        })

        if (viewModel.searchHotList.value == null){
            viewModel.getHistoryList()
        }
    }

    private fun initHotList(){
        val adapter = HotListAdapter {
            viewModel.changeNavigateBehavior(it.searchWord)
            viewModel.getHistoryList()
        }

        binding.searchHotRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.searchHotList.observe(viewLifecycleOwner, {
            adapter.submitList(it){
                binding.searchProgress.visibility = View.GONE
            }
        })

        if (viewModel.searchHotList.value == null){
            viewModel.getHotListDetail()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = SearchViewModel.newInstance(requireActivity())
        initTvHistory()
        initHotList()
    }
}