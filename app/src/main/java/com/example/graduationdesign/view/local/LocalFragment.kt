package com.example.graduationdesign.view.local

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.databinding.LocalFragmentBinding
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.userinfo.adapter.UserInfoAdapter
import kotlinx.coroutines.flow.combine

class LocalFragment : BaseFragment() {

    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var binding: LocalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocalFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        searchBoxWidth(binding.localToolbar.tvSearch, R.id.action_localFragment_to_searchFragment)
        initRecyclerView()
        initUserLabel()
        initBehavior()
    }

    private fun initRecyclerView(){
        val adapter = UserInfoAdapter(R.id.action_localFragment_to_reuseListFragment)

        binding.localRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        mainViewModel.localPlaylist.observe(requireActivity(), {
            adapter.submitList(it)
        })
    }

    private fun initBehavior(){
        binding.ivLocal.setOnClickListener {
            it.findNavController().navigate(R.id.action_localFragment_to_localMusicFragment)
        }

        binding.ivHistory.setOnClickListener {
            it.findNavController().navigate(R.id.action_localFragment_to_playHistoryFragment)
        }
    }

    private fun initUserLabel(){
        mainViewModel.user.observe(viewLifecycleOwner, {
            binding.tvUserName.text = it.nickName
            Glide.with(this)
                .load(it.avatarUrl)
                .into(binding.ivUserHead)

            if(mainViewModel.localPlaylist.value == null)
                mainViewModel.getUserPlaylist(it)
        })

        binding.linearUser.setOnClickListener {
            if (mainViewModel.user.value == null) return@setOnClickListener
            it.findNavController().navigate(R.id.action_localFragment_to_userInfoFragment)
        }
    }


    override fun toSetting() = R.id.action_localFragment_to_settingFragment
    override fun getTitle(): String {
        return getString(R.string.item_local)
    }

    override fun getToolbarView(): Toolbar {
        return binding.localToolbar.toolbar
    }

}