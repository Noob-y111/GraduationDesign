package com.example.graduationdesign.view.userinfo.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.model.ListType
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import kotlinx.android.synthetic.main.layout_song_image_item.view.*

class UserInfoAdapter(private val actionId: Int) :
    ListAdapter<Playlist, RecyclerView.ViewHolder>(Compare) {

    object Compare : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class PlaylistHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class BaseInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == 0) {
        return PlaylistHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_song_image_item, parent, false)
        ).also {
            it.itemView.setOnClickListener { view ->
                val item = currentList[it.adapterPosition]
                val bundle = Bundle().also { bundle ->
                    bundle.putParcelable("list_detail", item)
                    bundle.putInt("type", ListType.PLAYLIST_LIST)
                }
                view.findNavController()
                    .navigate(actionId, bundle)
            }
        }
//        } else {
//            return BaseInfoHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_text_item, parent, false))
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlaylistHolder -> {
                val item = currentList[position]
                holder.itemView.apply {
                    Glide.with(this)
                        .load(item.imageUrl)
                        .error(R.drawable.shimmer_bg)
                        .placeholder(R.drawable.shimmer_bg)
                        .transition(DrawableTransitionOptions.withCrossFade(300))
                        .into(iv_song_list_album)

                    tv_song_name.text = item.name
                    val text = "播放${item.playCount}次"
                    tv_song_artist.text = text
                }
            }

            else -> {

            }
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return if (position != currentList.size) 0 else 1
//    }
//
//    override fun getItemCount() = currentList.size + 1
}