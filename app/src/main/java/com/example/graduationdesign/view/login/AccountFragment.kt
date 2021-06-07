package com.example.graduationdesign.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.graduationdesign.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = LoginActivityViewModel.newInstance(this)
        binding.btnLogin.isEnabled = false

        binding.etUser.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        binding.btnLogin.setOnClickListener {
//            viewModel.changeDialogIsCancelable(false)
            viewModel.showDialog(true)
            viewModel.loginByCellphone(binding.etUser.text.toString().trim(), binding.etPassword.text.toString().trim())
        }

        //observe
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.btnLogin.isEnabled = binding.etUser.text.toString().trim()
                .isNotEmpty() and binding.etPassword.text.toString().trim()
                .isNotEmpty() and (binding.etUser.text.toString().trim().length == 11)
        }
        override fun afterTextChanged(s: Editable?) {}
    }
}