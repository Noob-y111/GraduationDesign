package com.example.graduationdesign.view.playlist.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.model.ListType
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.view.newalbum.dapter.AlbumAdapter
import com.example.imitationqqmusic.model.tools.DpPxUtils
import kotlinx.android.synthetic.main.layout_load_footer.view.*
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistAdapter(private val screenWidth: Int) :
    ListAdapter<Playlist, RecyclerView.ViewHolder>(Compare) {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class Footer(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.name == newItem.name
        }
    }

    var state: FooterState = FooterState.HIDE

    enum class FooterState {
        LOADING, ERROR, COMPLETE, HIDE
    }

    companion object {
        private const val NORMAL = 0
        private const val FOOTER = 1
    }

    override fun getItemCount(): Int {
        return if (currentList.size == 0) 0 else currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) FOOTER else NORMAL
    }

    override fun submitList(list: MutableList<Playlist>?, commitCallback: Runnable?) {
        super.submitList(list?.let { ArrayList(it) }, commitCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == NORMAL) {
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
                .also {
                    return Holder(it).apply {
                        itemView.setOnClickListener { view ->
                            val item = currentList[this.adapterPosition]
                            val bundle = Bundle().also { bundle ->
                                bundle.putParcelable("list_detail", item)
                                bundle.putInt("type", ListType.PLAYLIST_LIST)
                            }
                            view.findNavController()
                                .navigate(R.id.action_playlistFragment_to_reuseListFragment, bundle)
                        }
                    }
                }
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.layout_load_footer, parent, false)
                .also {
                    return Footer(it)
                }
        }
    }

    private fun footerState(holder: Footer) {
        with(holder.itemView) {
            when (state) {
                FooterState.LOADING -> {
                    isClickable = false
                    load_text.text = resources.getString(R.string.info_loading)
                    footer_progress.visibility = View.VISIBLE
                }

                FooterState.COMPLETE -> {
                    isClickable = false
                    load_text.text = resources.getString(R.string.info_complete)
                    footer_progress.visibility = View.GONE
                }

                FooterState.ERROR -> {
                    isClickable = true
                    load_text.text = resources.getString(R.string.info_error)
                    footer_progress.visibility = View.GONE
                }

                else -> {
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            with(holder.itemView) {
                val playlist = currentList[position]
                val newBorder =
                    ((screenWidth - DpPxUtils.dp2Px(this.context, 30f)).toFloat() / 3).toInt()

                playlist_image.layoutParams.width = newBorder
                playlist_image.layoutParams.height = newBorder
                playlist_text.layoutParams.width = newBorder

                Glide.with(this).load(playlist.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .error(R.drawable.shimmer_bg)
                    .placeholder(R.drawable.shimmer_bg)
                    .into(playlist_image)
                tv_play_count.text = playlist.playCount
                playlist_text.text = playlist.name
            }
        } else {
            footerState(holder as Footer)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == NORMAL) 1 else manager.spanCount
                }
            }
        }
    }
}