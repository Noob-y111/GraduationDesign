package com.example.graduationdesign.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.graduationdesign.databinding.FragmentEmailBinding

class EmailFragment : Fragment() {

    private lateinit var binding: FragmentEmailBinding
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = LoginActivityViewModel.newInstance(this)

        binding.etNumber.addTextChangedListener(textWatcher)
        binding.etCode.addTextChangedListener(textWatcher)

        binding.btnLogin.isEnabled = false
        binding.btnLogin.setOnClickListener {
            viewModel.showDialog(true)
            viewModel.loginByEmail(binding.etNumber.text.toString().trim(), binding.etCode.text.toString().trim())
        }

        //observe

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.btnLogin.isEnabled = binding.etNumber.text.toString().trim()
                .isNotEmpty() and binding.etCode.text.toString().trim().isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}

    }
}