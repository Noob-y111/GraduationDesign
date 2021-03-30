package com.example.graduationdesign.view.play_page

import android.graphics.Bitmap
import androidx.lifecycle.*

class PlayerViewModel : ViewModel() {

    companion object{
        private var viewModel: PlayerViewModel? = null
        fun newInstance(lifecycleOwner: ViewModelStoreOwner): PlayerViewModel = synchronized(this){
            return@synchronized if (viewModel != null){
                viewModel!!
            }else{
                viewModel = ViewModelProvider(lifecycleOwner).get(PlayerViewModel::class.java)
                viewModel!!
            }
        }
    }

    private var _imageBitmap = MutableLiveData<Bitmap>()
    val imageBitmap: LiveData<Bitmap> = _imageBitmap

    fun changeImageBitmap(bitmap: Bitmap){
        _imageBitmap.postValue(bitmap)
    }

}
