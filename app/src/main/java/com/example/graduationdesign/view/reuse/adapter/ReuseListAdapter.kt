package com.example.graduationdesign.view.reuse.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import kotlinx.android.synthetic.main.layout_song_image_item.view.*
import kotlinx.android.synthetic.main.layout_song_image_item.view.tv_song_artist
import kotlinx.android.synthetic.main.layout_song_image_item.view.tv_song_name
import kotlinx.android.synthetic.main.song_text_item.view.*

class ReuseListAdapter : ListAdapter<SongBean, ReuseListAdapter.Holder>(Compare) {

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
        LayoutInflater.from(parent.context).inflate(R.layout.song_text_item, parent, false)
            .also {
                return Holder(it).also { holder ->
                    holder.itemView.setOnClickListener {

                    }
                }
            }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            val item = currentList[position]
            tv_song_order.text = (position + 1).toString()
            try {
                tv_song_name.text = item.name
                var artists = ""
                item.artists.forEach {
                    artists += it.name
                    if (item.artists.lastIndex != item.artists.indexOf(it)) {
                        artists += "，"
                    }
                }
                artists += " - ${item.album.name} "
                tv_song_artist.text = artists
            } catch (e: Exception) {
                println("================e.message: ${e.message}")
            }

        }
    }
}