package com.example.graduationdesign.recyclerview_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import kotlinx.android.synthetic.main.layout_advice_item.view.*

class MusicClubRecyclerviewAdapter(private val callback: MusicClubRecyclerviewAdapterCallBack) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AdviceHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class SecondHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object{
        private const val ADVICE_HOLDER = 0
        private const val OTHER_HOLDER = 1
    }

    interface MusicClubRecyclerviewAdapterCallBack {
        fun initSubRecyclerViewCallBack(recyclerview: RecyclerView, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0, 1 -> {
                ADVICE_HOLDER
            }

            else -> {
                OTHER_HOLDER
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_advice_item, parent, false).also {
            return AdviceHolder(it)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(position){
            0 -> {
                (holder as AdviceHolder).apply {
                    itemView.advice_title.text = holder.itemView.resources.getString(R.string.advice_title_recommend_playlist)
                    itemView.advice_button.text = holder.itemView.resources.getString(R.string.advice_button)
                    callback.initSubRecyclerViewCallBack(itemView.recycler_advice, position)
                }
            }

            1 -> {
                (holder as AdviceHolder).apply {
                    itemView.advice_title.text = holder.itemView.resources.getString(R.string.advice_title_newest_album)
                    itemView.advice_button.text = holder.itemView.resources.getString(R.string.advice_button)
                    callback.initSubRecyclerViewCallBack(itemView.recycler_advice, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}