package com.example.graduationdesign.view.explore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.mv.VideoData
import com.example.graduationdesign.model.bean.mv.VideoBean
import com.example.graduationdesign.tools.TimeFormat
import kotlinx.android.synthetic.main.layout_video_list_item.view.*

class VideoListAdapter(private val screenWidth: Int) :
    ListAdapter<VideoData, VideoListAdapter.Holder>(Compare) {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover: ImageView = itemView.video_cover_image
        val tvDuration: TextView = itemView.tv_play_duration
        val tvPlayCount: TextView = itemView.tv_explore_video_count
        val tvTitle: TextView = itemView.tv_video_title
        val tvCreatorName: TextView = itemView.tv_creater_name
        val ivCreatorHead: ImageView = itemView.iv_video_creater_head
    }

    object Compare : DiffUtil.ItemCallback<VideoData>() {
        override fun areItemsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
            return oldItem.video === newItem.video
        }

        override fun areContentsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
            return oldItem.video.vid == newItem.video.vid
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_video_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val videoItem: VideoBean = currentList[position].video
        with(holder) {
            Glide.with(itemView).load(videoItem.coverUrl).error(R.drawable.shimmer_bg)
                .placeholder(R.drawable.shimmer_bg).into(cover.also {
                    it.layoutParams.width = screenWidth
                    it.layoutParams.height = (9.toFloat() / 16 * screenWidth).toInt()
                })

            videoItem.creator?.avatarUrl?.let {
                Glide.with(itemView).load(it)
                    .error(R.drawable.shimmer_circle_bg)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .placeholder(R.drawable.shimmer_circle_bg).into(ivCreatorHead)
            }

            videoItem.creator?.avatarUrl ?: kotlin.run {
                Glide.with(itemView).load(R.drawable.shimmer_circle_bg)
                    .error(R.drawable.shimmer_circle_bg)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .placeholder(R.drawable.shimmer_circle_bg).into(ivCreatorHead)
            }

            tvTitle.text = videoItem.title
            tvCreatorName.text = videoItem.creator?.nickname
            tvDuration.text = TimeFormat.timeFormat(videoItem.durationms)
            tvPlayCount.text = videoItem.playTime
        }
    }
}