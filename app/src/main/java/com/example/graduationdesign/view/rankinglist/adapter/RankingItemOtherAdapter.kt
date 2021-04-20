package com.example.graduationdesign.view.rankinglist.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.model.ListType
import com.example.graduationdesign.model.bean.ranking_list_bean.ListDetail
import kotlinx.android.synthetic.main.ranking_item_short.view.*

class RankingItemOtherAdapter(private val screenWidth: Int) :
    ListAdapter<ListDetail, RankingItemOtherAdapter.OtherRankingHolder>(Compare) {
    class OtherRankingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<ListDetail>() {
        override fun areItemsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherRankingHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.ranking_item_short, parent, false)
            .also {
                return OtherRankingHolder(it)
                    .also { holder ->
                        holder.itemView.setOnClickListener { view ->
                            val bundle = Bundle().apply {
                                val item = currentList[holder.adapterPosition]
                                putParcelable("list_detail", item)
                                putInt("type", ListType.RANKING_LIST)
                            }
                            Navigation.findNavController(view)
                                .navigate(
                                    R.id.action_rankingListFragment_to_reuseListFragment,
                                    bundle
                                )
                        }
                    }
            }
    }

    override fun onBindViewHolder(holder: OtherRankingHolder, position: Int) {
        val borderWidth = (screenWidth.toFloat() / 3).toInt()
        val item = currentList[position]

//        holder.itemView.setOnClickListener { view ->
//            val bundle = Bundle().apply {
//                putParcelable("list_detail", item)
//                putInt("type", ListType.RANKING_LIST)
//            }
//            Navigation.findNavController(view)
//                .navigate(R.id.action_rankingListFragment_to_reuseListFragment, bundle)
//        }

        with(holder.itemView) {
            Glide.with(this).load(item.coverImgUrl).error(R.drawable.shimmer_bg)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.shimmer_bg).into(iv_ranking_image_short)

            iv_ranking_image_short.layoutParams.width = borderWidth
            iv_ranking_image_short.layoutParams.height = borderWidth
            tv_ranking_name_short.layoutParams.width = borderWidth

            tv_update_text_short.text = item.updateFrequency
            tv_ranking_name_short.text = item.name
            tv_ranking_name_short.layoutParams.width = borderWidth
        }
    }
}

