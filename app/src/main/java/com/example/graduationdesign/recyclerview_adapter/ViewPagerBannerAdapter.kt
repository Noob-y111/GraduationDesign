package com.example.graduationdesign.recyclerview_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.ImageOfBanner
import kotlin.concurrent.thread

class ViewPagerBannerAdapter :
    ListAdapter<ImageOfBanner, ViewPagerBannerAdapter.ImageHolder>(DiffUtils) {

    object DiffUtils : DiffUtil.ItemCallback<ImageOfBanner>() {
        override fun areItemsTheSame(oldItem: ImageOfBanner, newItem: ImageOfBanner): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageOfBanner, newItem: ImageOfBanner): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_banner_item, parent, false)
            .also {
                val holder = ImageHolder(it)
                holder.itemView.setOnClickListener {

                }
                return holder
            }
    }

    override fun getItemCount(): Int {
        return if(currentList.size == 0 ) 0 else currentList.size + 2
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val image = holder.itemView.findViewById<ImageView>(R.id.image_banner)
        val index = when (position) {
            0 -> currentList.size - 1
            itemCount - 1 -> 0
            else -> position - 1
        }
        Glide.with(holder.itemView)
            .load(currentList[index].imageUrl)
            .placeholder(R.drawable.shimmer_bg).into(image)
//        thread {
//            val bitmap = Glide.with(holder.itemView)
//                .asBitmap()
//                .load(currentList[index].imageUrl)
//                .placeholder(R.drawable.shimmer_bg).submit().get()
//            println("================bitmap.height: ${bitmap.height}")
//            println("================bitmap.width: ${bitmap.width}")
//        }
    }
}