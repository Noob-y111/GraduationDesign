package com.example.graduationdesign.view.reuse

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.base.AppBarStateChangedListener
import com.example.graduationdesign.base.BlurBitmap
import com.example.graduationdesign.databinding.ReuseListFragmentBinding
import com.example.graduationdesign.model.bean.ranking_list_bean.ListDetail
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.reuse.adapter.ReuseListAdapter
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.blurry.Blurry
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class ReuseListFragment : Fragment() {

    companion object {
        fun newInstance() = ReuseListFragment()
    }

    private lateinit var viewModel: ReuseListViewModel
    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var binding: ReuseListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReuseListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReuseListViewModel::class.java)
        viewModel.initInternetModel(requireContext())
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setUser(mainViewModel.user.value)

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        setAppbar()
        initView()
    }

    private fun setAppbar() {
        val newHeight =
            binding.toolbar.layoutParams.height + binding.reuseLabel.reuseImageTop.layoutParams.height * 2
        binding.reuseAppbar.layoutParams.also { lp ->
            lp.height = newHeight
            binding.reuseAppbar.layoutParams = lp
        }

        binding.reuseAppbar.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
            override fun onStateChanged(appbar: AppBarLayout?, state: State) {
                if (state == State.COLLAPSED) {
                    if (binding.reuseLabel.reuseLabelContainer.visibility == View.INVISIBLE) return
                    binding.reuseLabel.reuseLabelContainer.visibility = View.INVISIBLE
                } else {
                    if (binding.reuseLabel.reuseLabelContainer.visibility == View.VISIBLE) return
                    binding.reuseLabel.reuseLabelContainer.visibility = View.VISIBLE
                }
            }

            override fun onStateChanged(appbar: AppBarLayout?, verticalOffset: Int) {}
        })
    }

    private fun initView() {
        val adapter = ReuseListAdapter(mainViewModel, ReuseListAdapter.Type.INTERNET)
        binding.reuseRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.appbarImage.observe(viewLifecycleOwner, {
            thread {
                val bitmap = Glide.with(this)
                    .asBitmap()
                    .load(it)
                    .error(R.drawable.shimmer_bg)
                    .submit().get()

                MainScope().launch {
//                    Glide.with(this@ReuseListFragment)
//                        .load(bitmap)
//                        .transform(BlurBitmap(requireContext(), 25f))
//                        .into(binding.reuseImageBottom)
                    Blurry.with(requireContext())
                        .async()
                        .radius(10)
                        .sampling(10)
                        .from(bitmap)
                        .into(binding.reuseImageBottom)
                    binding.reuseLabel.reuseImageTop.setImageBitmap(bitmap)
                }
            }
        })

        viewModel.list.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.creator.observe(viewLifecycleOwner, {
            binding.reuseLabel.tvReuseArtist.text = it
        })

        viewModel.description.observe(viewLifecycleOwner, {
            binding.reuseLabel.tvReuseDescription.text = it
        })

        viewModel.labelTitle.observe(viewLifecycleOwner, {
            binding.reuseLabel.tvReuseTitle.text = it
        })

        viewModel.toolbarTitle.observe(viewLifecycleOwner, {
            binding.toolbar.title = it
        })

        viewModel.shouldHideProgress.observe(viewLifecycleOwner, {
            if (it) {
                binding.reuseProgressbar.visibility = View.GONE
            } else {
                binding.reuseProgressbar.visibility = View.VISIBLE
            }
        })

        arguments?.let { bundle ->
            println("=======================请求数据:")
            viewModel.howToLoadData(bundle)
        }
    }

}