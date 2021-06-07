package com.example.graduationdesign.view.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseDialogFragment
import com.example.graduationdesign.databinding.LayoutConfirmDialogBinding

class ConfirmDialog: BaseDialogFragment() {
    override fun getAnimId() = R.style.dialog_animation

    private lateinit var binding: LayoutConfirmDialogBinding
    private var confirmBehavior: (() -> Unit)? = null
    private var content: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutConfirmDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialogBaseSetting(80, Gravity.CENTER)
        if (content == null) throw Exception("content is empty")
        binding.tvConfirmContent.text = content
        initBehavior()
    }

    fun setContent(content: String): ConfirmDialog{
        this.content = content
        return this
    }

    fun setConfirmBehavior(behavior: () -> Unit): ConfirmDialog{
        this.confirmBehavior = behavior
        return this
    }

    private fun initBehavior(){
        binding.tvBehaviorCancel.setOnClickListener {
            dismiss()
        }

        binding.tvBehaviorConfirm.setOnClickListener {
            confirmBehavior?.let { it1 -> it1() }
            dismiss()
        }
    }
}