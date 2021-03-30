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
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.databinding.FragmentPlayBinding
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils
import jp.wasabeef.blurry.Blurry

class PlayFragment : Fragment() {

    private lateinit var binding: FragmentPlayBinding
    private lateinit var viewModel: PlayerViewModel
    private var rotationAnimator: ObjectAnimator? = null

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
        if (rotationAnimator != null) {
            if (rotationAnimator!!.isPaused) {
                rotationAnimator!!.resume()
            } else {
                rotationAnimator!!.pause()
            }
        } else {
            rotationAnimator = ObjectAnimator.ofFloat(binding.ivDetailMusicImage, "rotation", 360f)
            with(rotationAnimator!!) {
                repeatCount = Animation.INFINITE
                duration = 10000
                interpolator = LinearInterpolator()
                start()
            }
        }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageContainerSize()
        viewModel = PlayerViewModel.newInstance(this)

        binding.ivDetailMusicImage.setOnClickListener {
            startAnimation()
        }

        viewModel.imageBitmap.observe(viewLifecycleOwner, {
            Glide.with(this)
                .load(it)
                .into(binding.ivDetailMusicImage)
        })
    }

}