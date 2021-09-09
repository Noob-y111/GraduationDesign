package com.example.graduationdesign.view.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.model.room.search.SearchHistory
import kotlinx.android.synthetic.main.layout_history_item.view.*

class SearchHistoryAdapter(private val block: (item: SearchHistory) -> Unit): ListAdapter<SearchHistory,SearchHistoryAdapter.Holder >(CompareText) {
    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvHistory: TextView = itemView.tv_search_history
    }

    object CompareText: DiffUtil.ItemCallback<SearchHistory>(){
        override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
            return oldItem.keyWord == newItem.keyWord
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.layout_history_item, parent, false)).also {
            it.itemView.setOnClickListener { _ ->
                block(currentList[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvHistory.text = currentList[position].keyWord
    }
}