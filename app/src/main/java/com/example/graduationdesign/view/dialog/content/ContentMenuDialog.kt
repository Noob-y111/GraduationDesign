package com.example.graduationdesign.view.dialog.content

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseDialogFragment
import com.example.graduationdesign.databinding.LayoutContentMenuBinding

class ContentMenuDialog(
    list: ArrayList<String>,
    drawableList: ArrayList<Drawable>?
) : BaseDialogFragment() {
    override fun getAnimId() = R.style.dialog_animation

    private lateinit var binding: LayoutContentMenuBinding
    private val contentMenuAdapter = ContentMenuAdapter(list, drawableList)
    private var title: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutContentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    fun setOnItemClickListener(listener: ContentMenuAdapter.OnItemClickCallback): ContentMenuDialog{
        contentMenuAdapter.setOnItemClickListener(listener, this)
        return this
    }

    fun setContentMenuTitle(title: String): ContentMenuDialog{
        this.title = title
        return this
    }

    private fun initRecyclerView() {
        binding.contentMenuRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = contentMenuAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialogBaseSetting()
        title?.let {
            binding.tvContentMenuTitle.text = it
        }
        initRecyclerView()
    }
}