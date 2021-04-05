package com.example.graduationdesign.view.play_page

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseDialogFragment
import com.example.graduationdesign.base.BlurBitmap
import com.example.graduationdesign.databinding.PlayerFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.blurry.Blurry
import kotlin.concurrent.thread

class PlayerDialogFragment : BaseDialogFragment() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: PlayerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = PlayerFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getAnimId(): Int {
        return R.style.player_dialog_fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        translateStatusBar()
//        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        viewModel = PlayerViewModel.newInstance(this)
        initFragmentContainer()

        viewModel.imageBitmap.observe(viewLifecycleOwner, Observer {
            Blurry.with(requireContext())
                .radius(10)
                .sampling(10)
                .async()
                .from(it)
                .into(binding.playerBg)
        })

        thread {
            val bitmap = Glide.with(this)
                .asBitmap()
                .load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1101422365,3707187101&fm=26&gp=0.jpg")
                .placeholder(R.drawable.player_bg)
                .submit().get()
            viewModel.changeImageBitmap(bitmap)
        }
    }

    private fun initFragmentContainer() {
        binding.playerContainer.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int) = when (position) {
                0 -> PlayFragment()
                else -> LyricFragment()
            }

        }

        TabLayoutMediator(binding.tabs, binding.playerContainer) { tab: TabLayout.Tab, i: Int ->
            when (i) {
                0 -> tab.text = "歌曲"
                1 -> tab.text = "歌词"
            }
        }.attach()
    }
}