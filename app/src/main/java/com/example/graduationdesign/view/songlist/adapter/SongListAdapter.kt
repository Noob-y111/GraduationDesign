package com.example.graduationdesign.view.songlist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.view.main.MainActivityViewModel
import kotlinx.android.synthetic.main.layout_song_image_item.view.*

class SongListAdapter(private val viewModel: MainActivityViewModel) : ListAdapter<SongBean, SongListAdapter.Holder>(Compare) {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<SongBean>() {
        override fun areItemsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_song_image_item, parent, false)
            .also {
                return Holder(it).also { holder ->
                    holder.itemView.setOnClickListener {
                        viewModel.changeSongAndSongList(
                            holder.adapterPosition,
                            ArrayList(currentList)
                        )
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            val item = currentList[position]
            try {
                Glide.with(this)
                    .load(item.album.picUrl)
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .placeholder(R.drawable.shimmer_bg)
                    .error(R.drawable.shimmer_bg)
                    .into(this.findViewById(R.id.iv_song_list_album))

                tv_song_name.text = item.name
                var artists = ""
                item.artists.forEach {
                    artists += it.name
                    if (item.artists.lastIndex != item.artists.indexOf(it)) {
                        artists += "，"
                    }
                }
                tv_song_artist.text = "$artists - ${item.album.name} "
            } catch (e: Exception) {
                println("================e.message: ${e.message}")
            }

        }
    }
}