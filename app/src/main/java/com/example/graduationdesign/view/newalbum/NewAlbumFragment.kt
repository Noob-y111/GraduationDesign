package com.example.graduationdesign.view.newalbum

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.NewAlbumFragmentBinding
import com.example.graduationdesign.view.newalbum.dapter.AlbumAdapter
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils

class NewAlbumFragment : Fragment() {

    companion object {
        fun newInstance() = NewAlbumFragment()
    }

    private lateinit var viewModel: NewAlbumViewModel
    private lateinit var binding: NewAlbumFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewAlbumFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewAlbumViewModel::class.java)
        viewModel.initInternetModel(requireContext())

        binding.progress.visibility = View.VISIBLE
        binding.newAlbumToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val adapter = AlbumAdapter(
            ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(
                requireContext(),
                60f
            )
        )
        binding.newAlbumRecyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val position = (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    viewModel.pagingLoading(position == adapter.currentList.size, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        viewModel.list.observe(viewLifecycleOwner, {
            if (it.size == 0) return@observe
            adapter.submitList(it) {
                binding.progress.visibility = View.INVISIBLE
                adapter.state = AlbumAdapter.FooterState.HIDE
                viewModel.isLoading = false
            }
        })

        viewModel.footerState.observe(viewLifecycleOwner, {
            adapter.state = it
        })

        if (viewModel.list.value == null){
            viewModel.firstRequest()
        }else{
            println("================viewModel.list.value?.size: ${viewModel.list.value?.size}")
        }
    }

}