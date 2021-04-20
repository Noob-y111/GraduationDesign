package com.example.graduationdesign.view.club.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.ImageAndText
import com.example.imitationqqmusic.model.tools.DpPxUtils
import kotlinx.android.synthetic.main.layout_song_artist_item.view.*

class AdviceSongItemAdapter (private val screenWidth: Int):
    ListAdapter<ArrayList<ImageAndText>, AdviceSongItemAdapter.Holder>(DiffUtil) {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object DiffUtil :
        androidx.recyclerview.widget.DiffUtil.ItemCallback<ArrayList<ImageAndText>>() {
        override fun areItemsTheSame(
            oldItem: ArrayList<ImageAndText>,
            newItem: ArrayList<ImageAndText>
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ArrayList<ImageAndText>,
            newItem: ArrayList<ImageAndText>
        ): Boolean {
            return oldItem.size == newItem.size
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_song_artist_item, parent, false)
            .also {
                return Holder(it.apply {
                    setOnClickListener {

                    }
                })
            }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            this.layoutParams.let { lp ->
                lp.width = screenWidth - DpPxUtils.dp2Px(context, 150f)
                holder.itemView.layoutParams = lp
            }

            val list = currentList[position]
            list.forEachIndexed { index, item ->
                when (index) {
                    0 -> {
                        tv_song_name1.text = item.text
                        Glide.with(this)
                            .load(item.imageUrl)
                            .transition(DrawableTransitionOptions.withCrossFade(300))
                            .error(R.drawable.shimmer_bg)
                            .placeholder(R.drawable.shimmer_bg)
                            .into(album_image1)
                    }
                    1 -> {
                        tv_song_name2.text = item.text
                        Glide.with(this)
                            .load(item.imageUrl)
                            .transition(DrawableTransitionOptions.withCrossFade(300))
                            .error(R.drawable.shimmer_bg)
                            .placeholder(R.drawable.shimmer_bg)
                            .into(album_image2)
                    }
                    2 -> {
                        tv_song_name3.text = item.text
                        Glide.with(this)
                            .load(item.imageUrl)
                            .transition(DrawableTransitionOptions.withCrossFade(300))
                            .error(R.drawable.shimmer_bg)
                            .placeholder(R.drawable.shimmer_bg)
                            .into(album_image3)
                    }
                }
            }
        }
    }
}