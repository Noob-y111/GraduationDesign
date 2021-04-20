package com.example.graduationdesign.view.video.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.mv.MvBean
import kotlinx.android.synthetic.main.layout_search_result_item_video.view.*

class SimilarMvAdapter (private val block: (vid: String) -> Unit): ListAdapter<MvBean, SimilarMvAdapter.MvHolder>(CompareMv) {
    object CompareMv : DiffUtil.ItemCallback<MvBean>() {
        override fun areItemsTheSame(oldItem: MvBean, newItem: MvBean): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MvBean, newItem: MvBean): Boolean {
            return oldItem.vid == newItem.vid
        }

    }

    class MvHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mvImage: ImageView = itemView.iv_search_result_item_image
        val mvName: TextView = itemView.tv_search_result_item_name
        val mvInfo: TextView = itemView.tv_search_result_item_info
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MvHolder {
        return MvHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_search_result_item_video, parent, false)
        ).also { holder ->
            holder.itemView.setOnClickListener {
                block(currentList[holder.adapterPosition].vid)
            }
        }
    }

    override fun onBindViewHolder(holder: MvHolder, position: Int) {
        with(holder){
            val mvBean = currentList[position]
            Glide.with(itemView).load(mvBean.coverUrl)
                .error(R.drawable.shimmer_bg)
                .placeholder(R.drawable.shimmer_bg)
                .into(mvImage)
            mvName.text = mvBean.title

            var info = mvBean.playTime + "次播放 "
            mvBean.artists.forEach {
                info += it.name
                if (mvBean.artists.lastIndex != mvBean.artists.indexOf(it)) {
                    info += "/"
                }
            }
            mvInfo.text = info
        }
    }
}
