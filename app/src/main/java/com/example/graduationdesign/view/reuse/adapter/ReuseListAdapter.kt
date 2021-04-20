package com.example.graduationdesign.view.reuse.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.view.main.MainActivityViewModel
import kotlinx.android.synthetic.main.layout_song_image_item.view.*
import kotlinx.android.synthetic.main.layout_song_image_item.view.tv_song_artist
import kotlinx.android.synthetic.main.layout_song_image_item.view.tv_song_name
import kotlinx.android.synthetic.main.song_text_item.view.*

class ReuseListAdapter(private val viewModel: MainActivityViewModel, private val type: Type) :
    ListAdapter<SongBean, ReuseListAdapter.Holder>(Compare) {

    class Holder(itemView: View) : ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<SongBean>() {
        override fun areItemsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem.id == newItem.id
        }

    }

    enum class Type {
        LOCAL, INTERNET, NONE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        LayoutInflater.from(parent.context).inflate(R.layout.song_text_item, parent, false)
            .also {
                return Holder(it).also { holder ->
                    when (type) {
                        Type.LOCAL -> {
                            holder.itemView.setOnClickListener {
                                viewModel.changeLocalSongAndSongList(
                                    holder.adapterPosition,
                                    ArrayList(currentList)
                                )
                            }
                        }

                        Type.INTERNET -> {
                            holder.itemView.setOnClickListener {
                                viewModel.changeSongAndSongList(
                                    holder.adapterPosition,
                                    ArrayList(currentList)
                                )
                            }
                        }
                        else -> {
                        }
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