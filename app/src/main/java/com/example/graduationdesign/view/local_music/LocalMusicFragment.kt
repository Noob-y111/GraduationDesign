package com.example.graduationdesign.view.local_music

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.databinding.LocalMusicFragmentBinding
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.reuse.adapter.ReuseListAdapter

class LocalMusicFragment : Fragment() {

    companion object {
        fun newInstance() = LocalMusicFragment()
    }

    private lateinit var viewModel: LocalMusicViewModel
    private lateinit var binding: LocalMusicFragmentBinding
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocalMusicFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initRecycler(){
        val adapter = ReuseListAdapter(mainViewModel, ReuseListAdapter.Type.LOCAL, null)
        binding.localMusicRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.localSongList.observe(viewLifecycleOwner, {
            adapter.submitList(it){
                binding.localProgress.visibility = View.GONE
            }
        })

        if (viewModel.localSongList.value == null){
            viewModel.getLocalList()
        }
    }

    private fun initBehavior(){
        binding.localMusicToolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocalMusicViewModel::class.java)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setModel(mainViewModel.getDataModel())
        initBehavior()
        initRecycler()
    }

}