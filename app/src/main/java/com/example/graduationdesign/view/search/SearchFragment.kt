package com.example.graduationdesign.view.search

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.databinding.SearchFragmentBinding
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.search.adapter.AutoCompleteListAdapter
import com.example.graduationdesign.view.search.subFragment.SearchHotFragment
import com.example.graduationdesign.view.search.subFragment.SearchResultFragment
import com.example.graduationdesign.view.search.subFragment.search_result_sub.ComprehensiveFragment
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
        private const val HOT = "hot"
        private const val RESULT = "result"
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding

    private val hotFragment = SearchHotFragment()
    private val resultFragment = SearchResultFragment()

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            viewModel.getSearchSuggest(s.toString().trim())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initToolbar() {
        binding.searchToolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }

        binding.searchSearchView.layoutParams.width =
            ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(requireContext(), 60f)
    }

    private fun initSearchView() {
        binding.searchSearchView.setOnFocusChangeListener { _, hasFocus ->
            val softKeyboard =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus) {
                softKeyboard.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            } else {
                softKeyboard.hideSoftInputFromWindow(binding.searchSearchView.windowToken, 0)
            }
        }

        binding.searchSearchView.addTextChangedListener(watcher)
        val adapter = AutoCompleteListAdapter()
        binding.searchSearchView.setAdapter(adapter)

        binding.searchSearchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search(binding.searchSearchView.text.toString().trim())?.let {
                    viewModel.changeNavigateBehavior(it)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.searchSearchView.isFocusable = true
        binding.searchSearchView.requestFocus()
        binding.searchSearchView.dropDownWidth =
            ScreenUtils.getWidth(requireActivity())

        binding.searchSearchView.setOnItemClickListener { _, _, position: Int, _ ->
            viewModel.changeNavigateBehavior(adapter.getItem(position) as String)
        }

        viewModel.searchViewCompeteList.observe(viewLifecycleOwner, {
            adapter.submitList(binding.searchSearchView.text.toString().trim(), it)
        })
    }

    private fun initFragmentContainer() {
        if (!hotFragment.isAdded) {
            childFragmentManager.beginTransaction()
                .add(R.id.search_fragment_container, hotFragment)
                .commitAllowingStateLoss()
        }
    }

    private fun navigate(keyword: String, actionId: Int, enable: Boolean) {
        if (enable) {
//            Navigation.findNavController(requireActivity(), R.id.search_fragment_container)
//                .navigate(actionId)
            if (!resultFragment.isAdded) {
                childFragmentManager.beginTransaction()
                    .hide(hotFragment)
                    .add(R.id.search_fragment_container, resultFragment)
                    .addToBackStack(RESULT)
                    .commitAllowingStateLoss()
            }
        }

        viewModel.changeKeyword(keyword)
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            binding.searchSearchView.windowToken,
            0
        )

        binding.searchSearchView.dismissDropDown()
    }

    private fun observeData() {
        viewModel.searchDefault.observe(viewLifecycleOwner, {
            binding.searchSearchView.hint = it.showKeyword
        })

        viewModel.navigateBehavior.observe(viewLifecycleOwner, {
            navigate(
                it["keyword"] as String,
                R.id.action_searchHotFragment_to_searchResultFragment,
                it["enable"] as Boolean
            )
        })
    }

    private fun requestData() {
        if (viewModel.searchDefault.value == null) {
            viewModel.getDefaultKeyWord()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = SearchViewModel.newInstance(requireActivity())
        viewModel.setInterModel(requireContext())

        initToolbar()
        initSearchView()
        initFragmentContainer()

        observeData()
        requestData()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.searchSearchView.isFocusable = false
    }

}