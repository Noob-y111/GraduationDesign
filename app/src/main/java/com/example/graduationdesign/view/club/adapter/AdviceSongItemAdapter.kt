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
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.imitationqqmusic.model.tools.DpPxUtils
import kotlinx.android.synthetic.main.layout_song_artist_item.view.*

class AdviceSongItemAdapter(private val screenWidth: Int) :
    ListAdapter<SongBean, AdviceSongItemAdapter.Holder>(DiffUtil) {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object DiffUtil :
        androidx.recyclerview.widget.DiffUtil.ItemCallback<SongBean>() {
        override fun areItemsTheSame(
            oldItem: SongBean,
            newItem: SongBean
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SongBean,
            newItem: SongBean
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun getItemCount(): Int {
        return currentList.size / 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_song_artist_item, parent, false)
            .also {
                return Holder(it)
            }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            this.layoutParams.let { lp ->
                lp.width = screenWidth - DpPxUtils.dp2Px(context, 150f)
                holder.itemView.layoutParams = lp
            }

            val index = position * 3
            val item1 = currentList[index]
            val item2 = currentList[index + 1]
            val item3 = currentList[index + 2]

            tv_song_name1.text = item1.name
            Glide.with(this)
                .load(item1.album.picUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .error(R.drawable.shimmer_bg)
                .placeholder(R.drawable.shimmer_bg)
                .into(album_image1)

            tv_song_name2.text = item2.name
            Glide.with(this)
                .load(item2.album.picUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .error(R.drawable.shimmer_bg)
                .placeholder(R.drawable.shimmer_bg)
                .into(album_image2)

            tv_song_name3.text = item3.name
            Glide.with(this)
                .load(item3.album.picUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .error(R.drawable.shimmer_bg)
                .placeholder(R.drawable.shimmer_bg)
                .into(album_image3)
        }
    }
}