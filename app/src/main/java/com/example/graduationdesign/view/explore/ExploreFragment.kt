package com.example.graduationdesign.view.explore

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.databinding.ExploreFragmentBinding

class ExploreFragment : BaseFragment() {

    private lateinit var binding: ExploreFragmentBinding

    private lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExploreFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
        searchBoxWidth(binding.exploreToolbar.tvSearch)
    }

    override fun getTitle(): String = getString(R.string.item_explore)

    override fun getToolbarView() = binding.exploreToolbar.toolbar

}