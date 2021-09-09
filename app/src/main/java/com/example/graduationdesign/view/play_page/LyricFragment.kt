package com.example.graduationdesign.view.play_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.graduationdesign.databinding.FragmentLyricBinding
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.play_page.adapter.LyricAdapter
import com.example.imitationqqmusic.model.tools.ScreenUtils

class LyricFragment : Fragment() {

    private lateinit var binding: FragmentLyricBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLyricBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = PlayerViewModel.newInstance(requireActivity())
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val adapter = LyricAdapter{
            mainViewModel.getBinder()?.seekTo(it)
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.lyricRecycler.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        viewModel.lyricList.observe(viewLifecycleOwner, {
            adapter.submitList(it) {
                if (it.size != 0)
                    adapter.setHighLight(0)
            }
        })

        viewModel.hasLyric.observe(viewLifecycleOwner, {
            if (it) {
                binding.tvNoLyric.visibility = View.INVISIBLE
            } else {
                binding.tvNoLyric.visibility = View.VISIBLE
            }
        })

        viewModel.currentLyricTime.observe(viewLifecycleOwner, {
            viewModel.hasLyric.value?.let { hasLyric ->
                if (hasLyric){
                    adapter.changeIndexByTime(it) { index ->
                        if (layoutManager.findLastCompletelyVisibleItemPosition() >= adapter.itemCount - 1) return@changeIndexByTime
                        layoutManager.scrollToPositionWithOffset(
                            index,
                            binding.lyricRecycler.height / 2 - 100
                        )
                    }
                }
            }
        })
    }
}