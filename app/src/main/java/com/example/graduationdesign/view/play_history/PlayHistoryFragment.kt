package com.example.graduationdesign.view.play_history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.databinding.PlayHistoryFragmentBinding
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.view.dialog.content.ContentMenuAdapter
import com.example.graduationdesign.view.dialog.content.ContentMenuDialog
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.reuse.adapter.ReuseListAdapter

class PlayHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = PlayHistoryFragment()
    }

    private lateinit var viewModel: PlayHistoryViewModel
    private lateinit var binding: PlayHistoryFragmentBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayHistoryFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initBehavior() {
        binding.playHisToolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
    }

    private fun initRecyclerView() {
        val adapter = ReuseListAdapter(
            mainActivityViewModel,
            ReuseListAdapter.Type.INTERNET,
            object : ReuseListAdapter.LongPressListener {
                override fun longPress(indexOfData: Int, list: ArrayList<SongBean>): Boolean {
                    ContentMenuDialog(ArrayList<String>().also {
                        it.add("从播放历史中删除")
                        it.add("删除全部播放历史")
                    }, null)
                        .setContentMenuTitle("你可以做")
                        .setOnItemClickListener(object : ContentMenuAdapter.OnItemClickCallback {
                            override fun onItemClickListener(
                                view: View,
                                position: Int,
                                dialog: ContentMenuDialog?
                            ) {
                                viewModel.contentBehavior(position, indexOfData)
                                dialog?.dismiss()
                            }
                        }).show(requireActivity().supportFragmentManager, null)
                    return true
                }
            })

        binding.playHisRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.list.observe(viewLifecycleOwner, {
            adapter.submitList(it) {
                binding.playHisProgress.visibility = View.GONE
                if (it.size == 0) binding.playHisNoList.visibility = View.VISIBLE
                else binding.playHisNoList.visibility = View.GONE
            }
        })

        if (viewModel.list.value == null){
            viewModel.getPlayHisList()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PlayHistoryViewModel::class.java)
        mainActivityViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setModel(mainActivityViewModel.getDataModel())

        initBehavior()
        initRecyclerView()
    }

}