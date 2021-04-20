package com.example.graduationdesign.view.userinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.base.AppBarStateChangedListener
import com.example.graduationdesign.databinding.UserInfoFragmentBinding
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.userinfo.adapter.UserInfoAdapter
import com.google.android.material.appbar.AppBarLayout

class UserInfoFragment : Fragment() {

    companion object {
        fun newInstance() = UserInfoFragment()
    }

    private lateinit var viewModel: UserInfoViewModel
    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var binding: UserInfoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserInfoFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initBehavior(){
        binding.appbar.addOnOffsetChangedListener(object : AppBarStateChangedListener(){
            override fun onStateChanged(appbar: AppBarLayout?, state: State) {}

            override fun onStateChanged(appbar: AppBarLayout?, verticalOffset: Int) {
                appbar?.let {
                    viewModel.changeAlpha(1 - (verticalOffset.toFloat() / it.totalScrollRange))
                }
            }
        })

        binding.userInfoLabel.btnUserEdit.setOnClickListener {

        }

        binding.userInfoLabel.btnUserChangeBackground.setOnClickListener {

        }

        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
    }

    private fun userInitial(){
        mainViewModel.user.observe(viewLifecycleOwner, {
            viewModel.getUserDetail(it)
            viewModel.getUserPlaylist(it)
        })

        viewModel.background.observe(viewLifecycleOwner, {
            Glide.with(this)
                .load(it)
                .error(R.drawable.shimmer_bg)
                .placeholder(R.drawable.shimmer_bg)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(binding.userInfoBackground)
        })

        viewModel.head.observe(viewLifecycleOwner, {
            Glide.with(this)
                .load(it)
                .error(R.drawable.shimmer_circle_bg)
                .placeholder(R.drawable.shimmer_circle_bg)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userInfoLabel.ivUserHead)
        })

        viewModel.name.observe(viewLifecycleOwner, {
            binding.userInfoLabel.tvUserInfoName.text = it
            binding.toolbar.title = it
        })

        viewModel.backgroundAlpha.observe(viewLifecycleOwner, {
            binding.userInfoBackground.alpha = it
            binding.userInfoLabel.userInfoLabelRoot.alpha = it
            if (it == 0.4f){
                binding.userInfoLabel.userInfoLabelRoot.visibility = View.INVISIBLE
            }else{
                binding.userInfoLabel.userInfoLabelRoot.visibility = View.VISIBLE
            }
        })
    }

    private fun initRecycler(){
        val adapter = UserInfoAdapter()
        binding.userInfoRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.playlist.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setModel(mainViewModel.getDataModel())
        initBehavior()
        userInitial()
        initRecycler()
    }

}