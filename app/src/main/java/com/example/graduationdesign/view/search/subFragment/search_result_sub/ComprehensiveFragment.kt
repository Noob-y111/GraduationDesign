package com.example.graduationdesign.view.search.subFragment.search_result_sub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.databinding.FragmentComprehensiveBinding
import com.example.graduationdesign.model.SearchType
import com.example.graduationdesign.view.search.SearchViewModel
import com.example.graduationdesign.view.search.adapter.SearchResultAdapter

class ComprehensiveFragment() : Fragment() {

    private lateinit var binding: FragmentComprehensiveBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComprehensiveBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initRecyclerViews(){
        val singleAdapter = SearchResultAdapter(SearchType.SINGLE)
        val artistAdapter = SearchResultAdapter(SearchType.ARTIST)
        val playlistAdapter = SearchResultAdapter(SearchType.PLAYLIST)
        val albumAdapter = SearchResultAdapter(SearchType.ALBUM)
        val videoAdapter = SearchResultAdapter(SearchType.VIDEO)

        binding.searchSingle.tvSearchTitle.text = "单曲"
        binding.searchSingle.searchResultRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = singleAdapter
        }

        binding.searchArtist.tvSearchTitle.text = "歌手"
        binding.searchArtist.searchResultRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = artistAdapter
        }

        binding.searchPlaylist.tvSearchTitle.text = "歌单"
        binding.searchPlaylist.searchResultRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
        }

        binding.searchAlbum.tvSearchTitle.text = "专辑"
        binding.searchAlbum.searchResultRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = albumAdapter
        }

        binding.searchVideo.tvSearchTitle.text = "视频"
        binding.searchVideo.searchResultRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = videoAdapter
        }

        viewModel.comprehensiveList.observe(viewLifecycleOwner, {
            singleAdapter.submitList(it["single"])
            artistAdapter.submitList(it["artist"])
            playlistAdapter.submitList(it["playlist"])
            albumAdapter.submitList(it["album"])
            videoAdapter.submitList(it["video"])
        })

        viewModel.keyword.observe(viewLifecycleOwner, {
            viewModel.getComprehensiveResultByKeyWord(SearchType.COMPREHENSIVE)
        })

        viewModel.viewHideAble.observe(viewLifecycleOwner, {
            if (it){
                binding.nsvComprehensive.visibility = View.INVISIBLE
                binding.comprehensiveProgress.visibility = View.VISIBLE
            }else{
                binding.nsvComprehensive.visibility = View.VISIBLE
                binding.comprehensiveProgress.visibility = View.GONE
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = SearchViewModel.newInstance(requireActivity())
        initRecyclerViews()
    }

}