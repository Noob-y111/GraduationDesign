package com.example.graduationdesign.view.current_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseDialogFragment
import com.example.graduationdesign.databinding.CurrentListFragmentBinding
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.service.POSITION
import com.example.graduationdesign.service.SONG_LIST
import com.example.graduationdesign.view.current_list.adapter.CurrentListAdapter
import com.example.graduationdesign.view.main.MainActivityViewModel

class CurrentListFragment(private val theme: ColorTheme) : BaseDialogFragment() {
    private lateinit var binding: CurrentListFragmentBinding
    private lateinit var mainViewModel: MainActivityViewModel

    enum class ColorTheme {
        LIGHT, DARK
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrentListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initView() {
        dialogWidthAndHeight(100, 70)
        var textColor: Int = ContextCompat.getColor(requireContext(), R.color.black)
        var backTheme: Int = R.drawable.white_current_list_shape
        if (theme == ColorTheme.DARK) {
            textColor = ContextCompat.getColor(requireContext(), R.color.white)
            backTheme = R.drawable.black_current_list_shape
        }
        binding.currentRoot.background = ContextCompat.getDrawable(requireContext(), backTheme)
        binding.currentToolbar.setTextColor(textColor)
    }

    private fun initRecycler() {
        val adapter = CurrentListAdapter(theme){
            mainViewModel.getBinder()?.changePosition(it)
        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.currentRecycler.apply {
            layoutManager = linearLayoutManager
            this.adapter = adapter
        }
        mainViewModel.getBinder()?.service?.currentSongListAndPosition?.observe(
            viewLifecycleOwner,
            {
                adapter.submitList(it[SONG_LIST] as ArrayList<SongBean>) {
                    val position = it[POSITION] as Int
                    adapter.changeCheckIndex(position)
                    linearLayoutManager.scrollToPositionWithOffset(position, 0)
                }
            })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        initView()
        initRecycler()
    }

    override fun getAnimId(): Int {
        return R.style.player_dialog_fragment
    }

}