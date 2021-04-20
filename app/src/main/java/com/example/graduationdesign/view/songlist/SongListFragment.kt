package com.example.graduationdesign.view.songlist

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.base.AppBarStateChangedListener
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.base.BlurBitmap
import com.example.graduationdesign.databinding.SongListFragmentBinding
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.songlist.adapter.SongListAdapter
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.blurry.Blurry
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class SongListFragment : BaseFragment() {

    companion object {
        fun newInstance() = SongListFragment()
    }

    private lateinit var viewModel: SongListViewModel
    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var binding: SongListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SongListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initToolbar() {
        binding.songListToolbar.title = resources.getString(R.string.daily_recommend)
        binding.songListToolbar.setNavigationIcon(R.drawable.back)
        binding.songListToolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.main_container).navigateUp()
        }
    }

    private fun initAppbarLayout() {
        binding.songListAppbar.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
            override fun onStateChanged(appbar: AppBarLayout?, state: State) {
            }

            override fun onStateChanged(appbar: AppBarLayout?, verticalOffset: Int) {
                val alpha = verticalOffset / appbar?.totalScrollRange!!.toFloat()
                binding.ivHeaderTop.alpha = 1 - alpha
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel = ViewModelProvider(this).get(SongListViewModel::class.java)
        viewModel.initDataModel(requireContext())
        requireActivity().window.statusBarColor = Color.TRANSPARENT
        initToolbar()
        initAppbarLayout()
        initRecyclerView()
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getToolbarView(): Toolbar? {
        return null
    }


    private fun initRecyclerView() {
        val songListAdapter = SongListAdapter(mainViewModel)
        binding.songListRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songListAdapter
        }
        mainViewModel.user.value?.let { viewModel.getRecommendDailySong(it) }

        viewModel.songList.observe(viewLifecycleOwner, {
            thread {
                val bitmap = Glide.with(this)
                    .asBitmap()
                    .load(it[0].album.picUrl)
                    .submit().get()
                MainScope().launch {
                    Blurry.with(requireContext())
                        .radius(10)
                        .sampling(10)
                        .async()
                        .from(bitmap)
                        .into(binding.ivHeaderBottom)
                    binding.ivHeaderTop.setImageBitmap(bitmap)
                }
            }
            songListAdapter.submitList(it) {
                binding.songListProgress.visibility = View.GONE
            }
        })
    }

}