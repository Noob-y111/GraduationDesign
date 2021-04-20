package com.example.graduationdesign.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.costume.CircleImageView
import com.example.graduationdesign.costume.MarqueeTextView
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.view.play_page.PlayerDialogFragment
import kotlinx.android.synthetic.main.layout_footer_item.view.*

class MainFooterAdapter (private val supportFragmentManager: FragmentManager): ListAdapter<SongBean, MainFooterAdapter.Holder>(Compare) {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<SongBean>() {
        override fun areItemsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SongBean, newItem: SongBean): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun submitList(list: MutableList<SongBean>?, commitCallback: Runnable?) {
        super.submitList(list?.let { ArrayList(it) }, commitCallback)
    }

    override fun submitList(list: MutableList<SongBean>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun getItemCount(): Int {
        return when (currentList.size) {
            0 -> 0
            1 -> 1
            else -> currentList.size + 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_footer_item, parent, false)
            .also {
                return Holder(it).also { holder ->
                    holder.itemView.setOnClickListener { view ->
                        PlayerDialogFragment().show(supportFragmentManager, null)
                    }
                }
            }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            val item = when (position) {
                0 -> {
                    currentList[currentList.size - 1]
                }

                itemCount - 1 -> {
                    currentList[0]
                }

                else -> {
                    currentList[position - 1]
                }
            }

            Glide.with(this)
                .load(item.album.picUrl)
                .error(R.drawable.shimmer_circle_bg)
                .placeholder(R.drawable.shimmer_circle_bg)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(iv_footer_album)
            tv_song_name.text = item.name

            var artists = ""
            item.artists.forEach {
                artists += it.name
                if (item.artists.lastIndex != item.artists.indexOf(it)) {
                    artists += "，"
                }
            }
            tv_song_artist.text = artists
        }
    }
}