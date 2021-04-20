package com.example.graduationdesign.view.search.subFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.databinding.LayoutSearchHotFragmentBinding
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.search.SearchViewModel
import com.example.graduationdesign.view.search.adapter.HotListAdapter

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
        binding.tvSearchHistory.setCallbackOnClickDrawableEnd {
            ToastUtil.show(requireContext(), "我点了一下删除")
            println("=======================我点了一下删除:")
        }
    }

    private fun initHotList(){
        val adapter = HotListAdapter {
            viewModel.changeNavigateBehavior(it.searchWord)
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