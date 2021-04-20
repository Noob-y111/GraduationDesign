package com.example.graduationdesign.view.explore.subview.mv

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.MvTypeFragmentBinding
import com.example.graduationdesign.model.bean.mv.MvBean
import com.example.graduationdesign.view.explore.adapter.MvListAdapter
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils

const val TYPE = "type"

class MvTypeFragment : Fragment() {

    private var type: String? = null

    companion object {
        fun newInstance(type: String) = MvTypeFragment().apply {
            arguments = Bundle().also {
                it.putString(TYPE, type)
            }
        }
    }

    private lateinit var viewModel: MvTypeViewModel
    private lateinit var binding: MvTypeFragmentBinding
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MvTypeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE)
        }
    }

    private fun initRecyclerView() {
        val adapter = MvListAdapter(
            ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(
                requireContext(),
                20f
            ),
           requireActivity().supportFragmentManager
        )
        binding.mvRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.mvListAndUpdateTime.observe(viewLifecycleOwner, {
            adapter.setUpdateTime(it["time"] as Long)
            adapter.submitList(it["list"] as ArrayList<MvBean>)
        })

        if (viewModel.mvListAndUpdateTime.value == null)
            viewModel.firstRequest(type)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel = ViewModelProvider(this).get(MvTypeViewModel::class.java)
        viewModel.setModel(mainViewModel.getDataModel())
        initRecyclerView()
    }

}