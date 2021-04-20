package com.example.graduationdesign.view.explore.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.mv.MvBean
import com.example.graduationdesign.view.video.VideoFragment
import kotlinx.android.synthetic.main.all_text_item.view.*
import kotlinx.android.synthetic.main.mv_item.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MvListAdapter(private val screenWidth: Int, private val fragmentManager: FragmentManager) :
    ListAdapter<MvBean, RecyclerView.ViewHolder>(CompareMv) {
    class MvHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMvCover: ImageView = itemView.iv_mv_cover
        val tvMvRank: TextView = itemView.tv_mv_rank
        val tvMvName: TextView = itemView.tv_mv_name
        val tvMvPlayCount: TextView = itemView.tv_mv_play_count
        val tvMvArtists: TextView = itemView.tv_mv_artist
        val tvMvOrder: TextView = itemView.tv_mv_order
    }

    class Header(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUpdate: TextView = itemView.tv_mv_update_time
    }

    object CompareMv : DiffUtil.ItemCallback<MvBean>() {
        override fun areItemsTheSame(oldItem: MvBean, newItem: MvBean): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MvBean, newItem: MvBean): Boolean {
            return oldItem.vid == newItem.vid
        }
    }

    private var updateTime: Long? = null

    fun setUpdateTime(time: Long) {
        updateTime = time
    }

    override fun getItemCount(): Int {
        return when {
            (currentList.size == 0) -> 0
            (updateTime == null) and (currentList.size != 0) -> currentList.size
            else -> currentList.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            MvHolder(LayoutInflater.from(parent.context).inflate(R.layout.mv_item, parent, false)).also { holder ->
                holder.itemView.setOnClickListener {
                    val mvItem: MvBean = if (updateTime == null) {
                        currentList[holder.adapterPosition]
                    } else {
                        currentList[holder.adapterPosition - 1]
                    }
                    VideoFragment(mvItem.vid).show(fragmentManager, null)
                }
            }
        } else {
            Header(
                LayoutInflater.from(parent.context).inflate(R.layout.all_text_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder) {
            when (this) {
                is MvHolder -> {
                    val realPosition: Int
                    val mvItem: MvBean = if (updateTime == null) {
                        realPosition = position + 1
                        currentList[position]
                    } else {
                        realPosition = position
                        currentList[position - 1]
                    }

                    Glide.with(this.itemView).asBitmap()
                        .load(mvItem.coverUrl)
                        .placeholder(R.drawable.shimmer_bg)
                        .error(R.drawable.shimmer_bg)
                        .into(ivMvCover.also {
                            it.layoutParams.width = screenWidth
                            it.layoutParams.height = (9.toFloat() / 16 * screenWidth).toInt()
                        })

                    tvMvName.text = mvItem.title
                    tvMvOrder.text = position.toString()
                    tvMvPlayCount.text = mvItem.playTime

                    mvItem.lastRank?.let {
                        val rankText = when {
                            it < realPosition -> "下降了：${realPosition - it}"
                            it > realPosition -> "上升了：${it - realPosition}"
                            else -> "无变化"
                        }
                        tvMvRank.text = rankText
                    }

                    var artists = ""
                    mvItem.artists.forEach {
                        artists += it.name
                        if (mvItem.artists.lastIndex != mvItem.artists.indexOf(it)) {
                            artists += "/"
                        }
                    }
                    tvMvArtists.text = artists
                }

                is Header -> {
                    val text = "更新时间：" + SimpleDateFormat("MM月dd日", Locale.getDefault()).format(
                        Date(updateTime!!)
                    )
                    tvUpdate.text = text
                }

                else -> {

                }
            }
        }
    }
}