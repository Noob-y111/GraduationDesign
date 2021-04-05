package com.example.graduationdesign.view.playlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.playlist_bean.Tag
import kotlinx.android.synthetic.main.playlist_item_subview.view.*

class ViewPager2Adapter(private val callback: InitRecyclerView, private val screenWidth: Int) :
    ListAdapter<Tag, ViewPager2Adapter.Holder>(Compare) {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem.name == newItem.name
        }
    }

    interface InitRecyclerView {
        fun initRecyclerView(recyclerView: RecyclerView, tag: Tag, playlistAdapter: PlaylistAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_subview, parent, false)
            .also {
                return Holder(it)
            }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.apply {
            playlist_item_sub_recyclerview.also {
                it.layoutManager = GridLayoutManager(this.context, 3)
                it.adapter = PlaylistAdapter(screenWidth)
            }
        }
    }
}