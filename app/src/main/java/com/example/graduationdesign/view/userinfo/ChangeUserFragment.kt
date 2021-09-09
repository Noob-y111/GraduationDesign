package com.example.graduationdesign.view.userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.graduationdesign.databinding.FragmentChangeUserBinding
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.dialog.InputDialog
import com.example.graduationdesign.view.dialog.content.ContentMenuAdapter
import com.example.graduationdesign.view.dialog.content.ContentMenuDialog
import com.example.graduationdesign.view.main.MainActivityViewModel

class ChangeUserFragment : Fragment() {

    private lateinit var binding: FragmentChangeUserBinding
    private lateinit var viewModel: UserInfoViewModel
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeUserBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initBehavior(){
        binding.changeUserToolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }

        binding.lineNickname.setOnClickListener {
            InputDialog().setContent("修改昵称").setConfirmBehavior {
                viewModel.updateUserName(it, mainViewModel.user.value){
                    viewModel.showSuccess()
                    binding.userName.text = it
                }

            }.show(requireActivity().supportFragmentManager, null)
        }

        binding.lineGender.setOnClickListener {
            ContentMenuDialog(ArrayList<String>().also {
                it.add("保密")
                it.add("男")
                it.add("女")
            }, null).setContentMenuTitle("选择性别").setOnItemClickListener(object : ContentMenuAdapter.OnItemClickCallback{
                override fun onItemClickListener(
                    view: View,
                    position: Int,
                    dialog: ContentMenuDialog?
                ) {
                    viewModel.updateUserSex(position, mainViewModel.user.value){
                        viewModel.showSuccess()
                        binding.userGender.text = when(position){
                            0 -> "保密"
                            1 -> "男"
                            else -> "女"
                        }
                    }

                    dialog?.dismiss()
                }
            }).show(requireActivity().supportFragmentManager, null)
        }

        binding.lineSignature.setOnClickListener {
            InputDialog().setContent("修改签名").setConfirmBehavior {
                viewModel.updateUserSignature(it, mainViewModel.user.value){
                    viewModel.showSuccess()
                    binding.userSignature.text = it
                }
            }.show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun initView(){
        viewModel.info.observe(viewLifecycleOwner, {
            binding.userName.text = it.profile.nickname
            binding.userGender.text = when(it.profile.gender){
                0 -> "保密"
                1 -> "男"
                else -> "女"
            }
            binding.userSignature.text = it.profile.signature
        })

        viewModel.string.observe(viewLifecycleOwner, {
            ToastUtil.show(requireContext(), it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = UserInfoViewModel.newInstance(this)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        initView()
        initBehavior()
    }
}