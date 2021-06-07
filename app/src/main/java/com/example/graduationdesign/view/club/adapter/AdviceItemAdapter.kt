package com.example.graduationdesign.view.club.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.model.ListType
import com.example.graduationdesign.model.bean.ImageAndText
import com.example.graduationdesign.model.bean.new_album_bean.SingleMonthData
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.view.main.MainActivityViewModel
import kotlinx.android.synthetic.main.image_and_text_item.view.*

class AdviceItemAdapter :
    ListAdapter<Any, AdviceItemAdapter.Holder>(DiffUtil) {

    object DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is Playlist -> oldItem.id == (newItem as Playlist).id
                else -> (oldItem as SingleMonthData).id == (newItem as SingleMonthData).id
            }
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_and_text_item, parent, false)
        ).also { holder ->
            holder.itemView.setOnClickListener {
                val bundle = when (val item = currentList[holder.adapterPosition]) {
                    is Playlist -> {
                        Bundle().also { bundle ->
                            bundle.putParcelable("list_detail", item)
                            bundle.putInt("type", ListType.PLAYLIST_LIST)
                        }
                    }

                    else -> {
                        Bundle().also { bundle ->
                            bundle.putParcelable("list_detail", item as SingleMonthData)
                            bundle.putInt("type", ListType.ALBUM_LIST)
                        }
                    }
                }
                it.findNavController()
                    .navigate(R.id.action_musicClubFragment_to_reuseListFragment, bundle)
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            if (currentList.size != 0) {
                when (val item = currentList[position]) {
                    is Playlist -> {
                        Glide.with(this)
                            .load(item.imageUrl)
                            .transition(DrawableTransitionOptions.withCrossFade(300))
                            .error(R.drawable.shimmer_bg)
                            .placeholder(R.drawable.shimmer_bg)
                            .into(image)
                        text.text = item.name
                    }

                    is SingleMonthData -> {
                        Glide.with(this)
                            .load(item.imageUrl)
                            .transition(DrawableTransitionOptions.withCrossFade(300))
                            .error(R.drawable.shimmer_bg)
                            .placeholder(R.drawable.shimmer_bg)
                            .into(image)
                        text.text = item.name
                    }
                }

            } else {
                Glide.with(this)
                    .load(R.drawable.shimmer_bg)
                    .into(image)
                text.text = this.context.resources.getString(R.string.default_user_name)
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}