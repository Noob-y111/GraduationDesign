package com.example.graduationdesign.view.play_page

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.FragmentPlayBinding
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.tools.TimeFormat
import com.example.graduationdesign.view.current_list.CurrentListFragment
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils
import jp.wasabeef.blurry.Blurry
import kotlin.concurrent.thread

class PlayFragment : Fragment() {

    private lateinit var binding: FragmentPlayBinding
    private lateinit var viewModel: PlayerViewModel
    private var rotationAnimator: ObjectAnimator? = null
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun imageContainerSize() {
        val width =
            ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(requireContext(), 40.0f)
        val imageSize = width / 3 * 2

        binding.imageContainer.layoutParams.height = width
        binding.ivDetailMusicImage.layoutParams.height = imageSize
        binding.ivDetailMusicImage.layoutParams.width = imageSize
    }

    private fun startAnimation() {
        rotationAnimator ?: kotlin.run {
            rotationAnimator = ObjectAnimator
                .ofFloat(
                    binding.ivDetailMusicImage,
                    "rotation",
                    360f
                )
            rotationAnimator?.let {
                it.repeatCount = Animation.INFINITE
                it.duration = 10000
                it.interpolator = LinearInterpolator()
                it.start()
            }
        }
    }

    private fun pause() {
        rotationAnimator?.let {
            if (!it.isPaused) {
                it.pause()
            }
        }
    }

    private fun resume() {
        rotationAnimator?.let {
            if (it.isPaused) {
                it.resume()
            }
        }
    }

    private fun cancel() {
        rotationAnimator?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private fun userBehavior() {
        binding.sbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mainViewModel.getBinder()?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.ivDetailList.setOnClickListener {
            CurrentListFragment(CurrentListFragment.ColorTheme.DARK).show(
                requireActivity().supportFragmentManager,
                null
            )
        }

        binding.ivPlayPause.setOnClickListener {
            mainViewModel.getBinder()?.playOrPause()
        }

        binding.ivNext.setOnClickListener {
            mainViewModel.getBinder()?.nextMusic()
        }

        binding.ivLast.setOnClickListener {
            mainViewModel.getBinder()?.lastMusic()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageContainerSize()
        viewModel = PlayerViewModel.newInstance(this)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())

        userBehavior()

        viewModel.imageBitmap.observe(viewLifecycleOwner, {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.shimmer_circle_bg)
                .error(R.drawable.shimmer_circle_bg)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivDetailMusicImage)
        })

        mainLiveDataObserve()
    }

    private fun mainLiveDataObserve() {
        mainViewModel.getBinder()?.service?.currentSongListAndPosition?.observe(
            viewLifecycleOwner,
            {
                val item = ((it["songList"] as ArrayList<*>)[(it["position"] as Int)]) as SongBean
                binding.mtvDetailName.text = item.name
                var artists = ""
                item.artists.forEach { bean ->
                    artists += bean.name
                    if (item.artists.lastIndex != item.artists.indexOf(bean)) {
                        artists += "，"
                    }
                }
                binding.tvDetailSinger.text = artists

                thread {
                    val bitmap = Glide.with(this)
                        .asBitmap()
                        .load(item.album.picUrl)
                        .error(R.drawable.shimmer_bg)
                        .submit().get()
                    viewModel.changeImageBitmap(bitmap)
                }
            })

        mainViewModel.getBinder()?.service?.stopOrResumeMediaPlayer?.observe(viewLifecycleOwner, {
            startAnimation()
            if (it) {
                binding.ivPlayPause.setImageResource(R.drawable.play_selector)
                pause()
            } else {
                binding.ivPlayPause.setImageResource(R.drawable.pause_selector)
                resume()
            }
        })

        mainViewModel.getBinder()?.service?.progressBarDuration?.observe(viewLifecycleOwner, {
            binding.sbProgress.max = it
            binding.tvDurationTime.text = TimeFormat.timeFormat(it)
        })

        mainViewModel.getBinder()?.service?.progressBarPosition?.observe(viewLifecycleOwner, {
            binding.sbProgress.progress = it
            binding.tvCurrentTime.text = TimeFormat.timeFormat(it)
        })

        mainViewModel.getBinder()?.service?.progressBarBuffer?.observe(viewLifecycleOwner, {
            binding.sbProgress.secondaryProgress = it
        })
    }


//    private fun startAnimation() {
//        if (rotationAnimation != null) {
//            binding.ivDetailMusicImage.clearAnimation()
//        } else {
//            rotationAnimation = RotateAnimation(
//                0f,
//                359f,
//                Animation.RELATIVE_TO_SELF,
//                0.5f,
//                Animation.RELATIVE_TO_SELF,
//                0.5f
//            )
//            with(rotationAnimation!!) {
//                repeatCount = Animation.INFINITE
//                fillAfter = true
//                duration = 10000
//                interpolator = LinearInterpolator()
//                binding.ivDetailMusicImage.animation = this
//                binding.ivDetailMusicImage.startAnimation(this)
//            }
//        }
//    }

}