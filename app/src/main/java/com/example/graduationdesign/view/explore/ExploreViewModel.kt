package com.example.graduationdesign.view.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class ExploreViewModel : ViewModel() {
    companion object {
        private var viewModel: ExploreViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): ExploreViewModel =
            synchronized(this) {
                return@synchronized if (viewModel != null) {
                    viewModel!!
                } else {
                    viewModel =
                        ViewModelProvider(lifecycleOwner).get(ExploreViewModel::class.java)
                    viewModel!!
                }
            }
    }

    private val mvType = ArrayList<String>().apply {
        add("内地")
        add("港台")
        add("欧美")
        add("韩国")
        add("日本")
    }

    fun getMvType(position: Int) = mvType[position]
    fun getMvTypeCount() = mvType.size
}