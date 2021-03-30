package com.example.graduationdesign.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseDialogFragment
import com.example.graduationdesign.databinding.DialogWaitingBinding

class WaitingDialog: BaseDialogFragment() {
    override fun getAnimId() = R.style.dialog_animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return DialogWaitingBinding.inflate(layoutInflater).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialogBaseSetting()
    }
}