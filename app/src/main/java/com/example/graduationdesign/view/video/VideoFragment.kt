package com.example.graduationdesign.view.video

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseDialogFragment
import com.example.graduationdesign.databinding.VideoFragmentBinding
import com.example.graduationdesign.model.bean.mv.MvDetailInfo
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.video.adapter.SimilarMvAdapter
import com.example.imitationqqmusic.model.tools.ScreenUtils

class VideoFragment(private val vid: String) : BaseDialogFragment() {

    companion object {
        fun newInstance(vid: String) = VideoFragment(vid)
    }

    private lateinit var viewModel: VideoViewModel
    private lateinit var binding: VideoFragmentBinding
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VideoFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun artistsText(list: ArrayList<ArtistBean>): String {
        var artists = ""
        list.forEach {
            artists += it.name
            if (list.lastIndex != list.indexOf(it)) {
                artists += "/"
            }
        }
        return artists
    }

    private fun initViewByMvInfo(it: MvDetailInfo) {
        viewModel.getVideoUrl(it.vid)
        binding.tvVideoName.text = it.title
        binding.videoToolbar.title = it.title
        val playCountText = it.playTime + "次观看"
        binding.tvVideoPlayCount.text = playCountText
        binding.tvVideoArtists.text = artistsText(it.artists)

        val description = "${it.publishTime}\n${it.description}"
        binding.tvVideoDescription.text = description
        binding.tvVideoCollectionCount.text = it.subCount.toString()
        binding.tvVideoShareCount.text = it.shareCount.toString()
        binding.tvVideoCommentsCount.text = it.commentCount.toString()
        println("================it: $it")
    }

    private fun initView() {
        viewModel.setOnVideoSizeChangedBlock { width, height ->
            binding.videoSurface.layoutParams.let { lp ->
                val screenWidth = ScreenUtils.getWidth(requireActivity())
                lp.width = screenWidth
                lp.height = ((screenWidth.toFloat() / width) * height).toInt()
            }
        }

        binding.videoSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        viewModel.progressEnable.observe(viewLifecycleOwner, {
            if (it) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        })

        viewModel.controllerImage.observe(viewLifecycleOwner, {
            binding.videoController.setImageResource(it)
        })

        viewModel.shouldToolbarShow.observe(viewLifecycleOwner, {
            binding.videoToolbar.visibility = it
            binding.videoController.visibility = it
        })

        viewModel.currentMvId.observe(viewLifecycleOwner, {
            viewModel.videoInfo(it)
            viewModel.getVideoUrl(it)
            viewModel.loadSimilarMvById(it)
        })

        viewModel.currentMv.observe(viewLifecycleOwner, {
            initViewByMvInfo(it)
        })

        viewModel.videoDuration.observe(viewLifecycleOwner, {
            binding.videoSeekBar.max = it
        })

        viewModel.videoPosition.observe(viewLifecycleOwner, {
            binding.videoSeekBar.progress = it
        })

        viewModel.bufferedPosition.observe(viewLifecycleOwner, {
            binding.videoSeekBar.secondaryProgress = it
        })

        viewModel.changeCurrentId(vid)
    }

    private fun initBehavior() {
        binding.videoToolbar.setNavigationOnClickListener {
            dismiss()
        }

        binding.videoSurface.setOnClickListener {
            viewModel.surfaceClick()
        }

        binding.videoController.setOnClickListener {
            viewModel.controllerShowTime = System.currentTimeMillis()
            viewModel.playerIsPlayOrPause()
        }

        binding.videoSurface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                viewModel.getPlayer().apply {
                    setDisplay(holder)
                    setScreenOnWhilePlaying(true)
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {}
        })
    }

    private fun initRecycler() {

        val adapter = SimilarMvAdapter {
            viewModel.changeCurrentId(it)
        }

        binding.videoRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.similarMvList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VideoViewModel::class.java)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setModel(mainViewModel.getDataModel())

        mainViewModel.getBinder()?.let {
            viewModel.mediaPlayerIsPlaying = it.playerIsPlaying()

            viewModel.setResumeOrPauseService { boolean ->
                if (!viewModel.mediaPlayerIsPlaying) return@setResumeOrPauseService
                if (boolean) it.playerPause()
                else it.playerPlay()
            }
        }

        viewModel.addLifecycleObservable {
            lifecycle.addObserver(it)
        }

        translateStatusBarBlackBackground()
        initBehavior()
        initRecycler()
        initView()
    }

    override fun getAnimId(): Int {
        return R.style.player_dialog_fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelTimer()
    }

}