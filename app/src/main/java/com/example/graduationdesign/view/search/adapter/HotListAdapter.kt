package com.example.graduationdesign.view.search.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.search_bean.HotItem
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import kotlinx.android.synthetic.main.search_hot_item.view.*
import kotlinx.android.synthetic.main.song_text_item.view.*

class HotListAdapter (private val block: (item: HotItem) -> Unit): ListAdapter<HotItem, HotListAdapter.HotItemHolder>(Compare) {

    object Compare : DiffUtil.ItemCallback<HotItem>() {
        override fun areItemsTheSame(oldItem: HotItem, newItem: HotItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: HotItem, newItem: HotItem): Boolean {
            return oldItem.searchWord == newItem.searchWord
        }

    }

    class HotItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotItemHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.search_hot_item, parent, false).also {
            return HotItemHolder(it).also { holder ->
                holder.itemView.setOnClickListener {
                    block(currentList[holder.adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HotItemHolder, position: Int) {
        val item = currentList[position]
        with(holder.itemView) {
            if (position < 3) {
                tv_search_item_order.setTextColor(Color.RED)
                tv_search_item_word.paint.isFakeBoldText = true
            } else {
                tv_search_item_order.setTextColor(Color.BLACK)
                tv_search_item_word.paint.isFakeBoldText = false
            }

            tv_search_item_order.text = (position + 1).toString()
            tv_search_item_word.text = item.searchWord
            tv_search_item_content.text = item.content
            tv_search_item_score.text = item.score.toString()
            item.iconUrl?.let {
                Glide.with(this)
                    .load(item.iconUrl)
                    .into(iv_search_item_icon)
            }
        }
    }
}