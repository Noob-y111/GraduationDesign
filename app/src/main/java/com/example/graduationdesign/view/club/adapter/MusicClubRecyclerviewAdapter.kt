package com.example.graduationdesign.view.club.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import kotlinx.android.synthetic.main.layout_advice_item.view.*

class MusicClubRecyclerviewAdapter(private val callback: MusicClubRecyclerviewAdapterCallBack) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AdviceHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class SongHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val ADVICE_HOLDER = 0
        private const val SONG_HOLDER = 1
    }

    interface MusicClubRecyclerviewAdapterCallBack {
        fun initSubRecyclerViewCallBack(recyclerview: RecyclerView, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 2 -> {
                ADVICE_HOLDER
            }

            else -> {
                SONG_HOLDER
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADVICE_HOLDER -> {
                AdviceHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_advice_item, parent, false)
                )
            }

            else -> {
                SongHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_advice_item, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AdviceHolder -> {
                when (position) {
                    0 -> {
                        holder.apply {
                            itemView.advice_title.text =
                                holder.itemView.resources.getString(R.string.advice_title_recommend_playlist)
                            itemView.advice_button.apply {
                                text =
                                    holder.itemView.resources.getString(R.string.advice_button)
                                setOnClickListener {
                                    Navigation.findNavController(it).navigate(R.id.action_musicClubFragment_to_playlistFragment)
                                }
                            }
                            callback.initSubRecyclerViewCallBack(itemView.recycler_advice, position)
                        }
                    }

                    2 -> {
                        holder.apply {
                            itemView.advice_title.text =
                                holder.itemView.resources.getString(R.string.advice_title_newest_album)
                            itemView.advice_button.text =
                                holder.itemView.resources.getString(R.string.advice_button)
                            itemView.advice_button.setOnClickListener {

                            }
                            callback.initSubRecyclerViewCallBack(itemView.recycler_advice, position)
                        }
                    }
                }
            }

            is SongHolder -> {
                when (position) {
                    1 -> {
                        holder.apply {
                            itemView.advice_title.text =
                                holder.itemView.resources.getString(R.string.advice_title_new_song)
                            itemView.advice_button.text =
                                holder.itemView.resources.getString(R.string.advice_button_play)
                            callback.initSubRecyclerViewCallBack(itemView.recycler_advice, position)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}