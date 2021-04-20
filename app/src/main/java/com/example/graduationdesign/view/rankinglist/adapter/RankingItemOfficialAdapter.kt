package com.example.graduationdesign.view.rankinglist.adapter

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
import kotlinx.android.synthetic.main.ranking_item_long.view.*

class RankingItemOfficialAdapter(private val screenWidth: Int) :
    ListAdapter<ListDetail, RankingItemOfficialAdapter.OfficialRankingHolder>(Compare) {

    class OfficialRankingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<ListDetail>() {
        override fun areItemsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfficialRankingHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.ranking_item_long, parent, false)
            .also {
                return OfficialRankingHolder(it)
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

    override fun onBindViewHolder(holder: OfficialRankingHolder, position: Int) {
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
                .placeholder(R.drawable.shimmer_bg).into(iv_ranking_image_long)
            iv_ranking_image_long.layoutParams.width = borderWidth
            iv_ranking_image_long.layoutParams.height = borderWidth

            tv_update_text_long.text = item.updateFrequency
            val first = item.tracks[0].name + " - " + item.tracks[0].artist
            val second = item.tracks[1].name + " - " + item.tracks[1].artist
            val third = item.tracks[2].name + " - " + item.tracks[2].artist
            tv_first.text = first
            tv_second.text = second
            tv_third.text = third

        }
    }
}
