package com.example.graduationdesign.view.explore.subview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.FragmentMvBinding
import com.example.graduationdesign.view.explore.ExploreViewModel
import com.example.graduationdesign.view.explore.subview.mv.MvTypeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class MvFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMvBinding
    private lateinit var viewModel: ExploreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMvBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initViewPager2(){
        binding.mvViewpager2.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount() = viewModel.getMvTypeCount()

            override fun createFragment(position: Int): Fragment {
                return MvTypeFragment.newInstance(viewModel.getMvType(position))
            }
        }

        TabLayoutMediator(binding.mvTab, binding.mvViewpager2){ tab: TabLayout.Tab, i: Int ->
            tab.text = viewModel.getMvType(i)
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ExploreViewModel.newInstance(requireActivity())
        initViewPager2()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            MvFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}