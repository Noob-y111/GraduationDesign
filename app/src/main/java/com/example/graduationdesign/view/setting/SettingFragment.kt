package com.example.graduationdesign.view.setting

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.SettingFragmentBinding
import com.example.graduationdesign.tools.SharedPreferencesUtil
import com.example.graduationdesign.view.bridge.BridgeActivity
import com.example.graduationdesign.view.dialog.ConfirmDialog
import kotlin.system.exitProcess

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: SettingViewModel
    private lateinit var binding: SettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        initBehavior()
    }

    private fun initBehavior(){
        binding.tvLoginOut.setOnClickListener {
            ConfirmDialog().setContent("是否要退出？").setConfirmBehavior {
                SharedPreferencesUtil.removeUidAndPassword()
                requireActivity().finish()
            }.show(requireActivity().supportFragmentManager, null)
        }

        binding.settingToolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
    }

}