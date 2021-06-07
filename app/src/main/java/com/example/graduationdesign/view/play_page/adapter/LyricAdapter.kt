package com.example.graduationdesign.view.play_page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.lrc.LyricBean
import kotlinx.android.synthetic.main.layout_lyric_item.view.*

class LyricAdapter(private val block: (msc: Int) -> Unit) : ListAdapter<LyricBean, LyricAdapter.Holder>(Compare) {
    object Compare : DiffUtil.ItemCallback<LyricBean>() {

        override fun areItemsTheSame(oldItem: LyricBean, newItem: LyricBean): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LyricBean, newItem: LyricBean): Boolean {
            return oldItem.lrc == newItem.lrc
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLyric: TextView = itemView.tv_lyric
    }

    private var highlightLine = 0

    private fun changeHighlightLine(index: Int, block: (position: Int) -> Unit) {
        setHighLight(index)
        block(highlightLine)
    }

    fun setHighLight(index: Int){
        val oldIndex = highlightLine
        highlightLine = index
        notifyItemChanged(oldIndex)
        notifyItemChanged(highlightLine)
    }

    fun changeIndexByTime(time: Int, block: (position: Int) -> Unit) {
        if (highlightLine >= currentList.size - 1) return
        val item = currentList[highlightLine]
        if (!(time < item.end!! && time >= item.start)) {
            for (i in 0 until currentList.size) {
                val lyric = currentList[i]
                if (time < lyric.end!! && time >= lyric.start){
                    changeHighlightLine(i, block) //改变高亮行
                    break
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_lyric_item, parent, false)
        ).also {
            it.itemView.setOnClickListener { _ ->
                block(currentList[it.adapterPosition].start)
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (position != highlightLine) {
            holder.tvLyric.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.light_grey
                )
            )
        } else {
            holder.tvLyric.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
        holder.tvLyric.text = currentList[position].lrc
    }
}