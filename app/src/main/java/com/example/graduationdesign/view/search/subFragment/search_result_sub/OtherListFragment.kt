package com.example.graduationdesign.view.search.subFragment.search_result_sub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.FragmentOtherListBinding
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.search.SearchViewModel
import com.example.graduationdesign.view.search.adapter.SearchResultAdapter

class OtherListFragment(private val type: Int) : Fragment() {

    private lateinit var binding: FragmentOtherListBinding
    private lateinit var viewModel: OtherListViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherListBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initRecyclerView() {
        val adapter = SearchResultAdapter(type, mainViewModel)
        binding.searchRecyclerOther.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val enable = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == (adapter.currentList.size - 1)
                    viewModel.pagingLoading(enable, newState, type, searchViewModel.keyword.value)
                }
            })
        }

        viewModel.otherList.observe(viewLifecycleOwner, {
            if (it.size == 0) return@observe
            adapter.submitList(it) {
                binding.otherProgress.visibility = View.GONE
            }
        })

        searchViewModel.keyword.observe(viewLifecycleOwner, {
            binding.otherProgress.visibility = View.VISIBLE
            viewModel.clearList()
            viewModel.getSearchResultByType(type, it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OtherListViewModel::class.java)
        searchViewModel = SearchViewModel.newInstance(this)
        viewModel.setInternetModel(searchViewModel.getInternetModel())
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        initRecyclerView()
    }
}