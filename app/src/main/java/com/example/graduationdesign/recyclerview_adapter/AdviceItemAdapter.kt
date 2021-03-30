package com.example.graduationdesign.recyclerview_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.ImageAndText
import kotlinx.android.synthetic.main.image_and_text_item.view.*

class AdviceItemAdapter : ListAdapter<ImageAndText, AdviceItemAdapter.Holder>(DiffUtil) {

    object DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ImageAndText>() {
        override fun areItemsTheSame(oldItem: ImageAndText, newItem: ImageAndText): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageAndText, newItem: ImageAndText): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        LayoutInflater.from(parent.context).inflate(R.layout.image_and_text_item, parent, false)
            .also {
                val holder = Holder(it)
                holder.itemView.setOnClickListener {

                }
                return holder
            }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            if (currentList.size != 0) {
                val item = currentList[position]
                Glide.with(this)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.shimmer_bg)
                    .into(image)
                text.text = item.text
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