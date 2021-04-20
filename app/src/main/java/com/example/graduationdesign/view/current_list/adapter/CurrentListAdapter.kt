package com.example.graduationdesign.view.current_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.service.POSITION
import com.example.graduationdesign.view.current_list.CurrentListFragment
import kotlinx.android.synthetic.main.layout_current_list_item.view.*

class CurrentListAdapter(
    val theme: CurrentListFragment.ColorTheme,
    private val block: (position: Int) -> Unit
) :
    ListAdapter<SongBean, CurrentListAdapter.Holder>(Compare) {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.iv_item_play
        val songText: TextView = itemView.tv_current_text
    }

    private var checkIndex = -1

    object Compare : DiffUtil.ItemCallback<SongBean>() {
        override fun areItemsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem.id == newItem.id
        }
    }

    fun changeCheckIndex(newIndex: Int) {
        val oldIndex = checkIndex
        checkIndex = newIndex
        notifyItemChanged(oldIndex)
        notifyItemChanged(newIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_current_list_item, parent, false)
        ).also { holder ->
            holder.itemView.setOnClickListener {
                if (holder.adapterPosition == checkIndex) return@setOnClickListener
                block(holder.adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder) {
            val item = currentList[position]

            var artists = ""
            item.artists.forEach {
                artists += it.name
                if (item.artists.lastIndex != item.artists.indexOf(it)) {
                    artists += "/"
                }
            }
            val text = item.name + " - " + artists
            songText.text = text

            if (checkIndex != position) {
                image.visibility = View.GONE
                val color = if (theme == CurrentListFragment.ColorTheme.DARK) {
                    R.color.white
                } else {
                    R.color.black
                }
                songText.setTextColor(ContextCompat.getColor(itemView.context, color))
            } else {
                image.visibility = View.VISIBLE
                songText.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.main_pink
                    )
                )
            }
        }
    }
}