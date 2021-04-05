package com.example.graduationdesign.view.playlist.subview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.databinding.PlaylistItemSubviewBinding
import com.example.graduationdesign.model.bean.playlist_bean.Tag
import com.example.graduationdesign.view.playlist.PlaylistViewModel
import com.example.graduationdesign.view.playlist.adapter.PlaylistAdapter
import com.example.imitationqqmusic.model.tools.ScreenUtils

class SubFragment(private val tag: Tag, private val playlistViewModel: PlaylistViewModel) :
    Fragment() {

    private lateinit var binding: PlaylistItemSubviewBinding
    private lateinit var viewModel: SubFragmentViewModel

    companion object {
        fun newInstance(tag: Tag, playlistViewModel: PlaylistViewModel) =
            SubFragment(tag, playlistViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaylistItemSubviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubFragmentViewModel::class.java)
        viewModel.getInternetModel(playlistViewModel.getInternetViewModel())

        val recyclerViewAdapter = PlaylistAdapter(ScreenUtils.getWidth(requireActivity()))
        binding.playlistItemSubRecyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = recyclerViewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val enable = (layoutManager as GridLayoutManager).findLastVisibleItemPosition() == (recyclerViewAdapter.currentList.size)
                    viewModel.pagingLoading(enable, newState)
                }
            })
        }

        viewModel.playlists.observe(viewLifecycleOwner, { list ->
            recyclerViewAdapter.submitList(list){
                viewModel.isLoading = false
                recyclerViewAdapter.state = PlaylistAdapter.FooterState.HIDE
            }
        })

        viewModel.footerState.observe(viewLifecycleOwner, {
            recyclerViewAdapter.state = it
        })

        if (viewModel.playlists.value == null) {
            viewModel.firstRequest(tag)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}