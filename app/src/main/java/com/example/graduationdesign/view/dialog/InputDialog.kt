package com.example.graduationdesign.view.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseDialogFragment
import com.example.graduationdesign.databinding.LayoutConfirmDialogBinding
import com.example.graduationdesign.databinding.LayoutInputDialogBinding

class InputDialog : BaseDialogFragment() {
    override fun getAnimId() = R.style.dialog_animation

    private lateinit var binding: LayoutInputDialogBinding
    private var confirmBehavior: ((str: String) -> Unit)? = null
    private var content: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutInputDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialogBaseSetting(80, Gravity.CENTER)
        if (content == null) throw Exception("content is empty")
        binding.tvConfirmContent.text = content
        initBehavior()
    }

    fun setContent(content: String): InputDialog {
        this.content = content
        return this
    }

    fun setConfirmBehavior(behavior: (str: String) -> Unit): InputDialog {
        this.confirmBehavior = behavior
        return this
    }

    private fun initBehavior() {
        binding.tvBehaviorCancel.setOnClickListener {
            dismiss()
        }

        binding.tvBehaviorConfirm.setOnClickListener {
            confirmBehavior?.let { it1 ->
                val text = binding.inputEt.text.toString().trim()
                if (text.isNotEmpty()){
                    it1(text)
                }
            }
            dismiss()
        }
    }
}