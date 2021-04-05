package com.example.graduationdesign.view.newalbum.dapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationdesign.R
import com.example.graduationdesign.model.ListType
import com.example.graduationdesign.model.bean.new_album_bean.SingleMonthData
import kotlinx.android.synthetic.main.layout_load_footer.view.*
import kotlinx.android.synthetic.main.new_album_item.view.*

class AlbumAdapter(private val screenWidth: Int) :
    ListAdapter<SingleMonthData, RecyclerView.ViewHolder>(Compare) {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class Footer(itemView: View) : RecyclerView.ViewHolder(itemView)

    var state: FooterState = FooterState.HIDE

    enum class FooterState {
        LOADING, ERROR, COMPLETE, HIDE
    }

    companion object {
        private const val NORMAL = 0
        private const val FOOTER = 1
    }

    object Compare : DiffUtil.ItemCallback<SingleMonthData>() {
        override fun areItemsTheSame(oldItem: SingleMonthData, newItem: SingleMonthData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SingleMonthData,
            newItem: SingleMonthData
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun getItemCount(): Int {
        return if (currentList.size == 0) 0 else currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) FOOTER else NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == NORMAL) {
            LayoutInflater.from(parent.context).inflate(R.layout.new_album_item, parent, false)
                .also {
                    return AlbumAdapter.Holder(it)
                        .also { holder ->
                            holder.itemView.setOnClickListener { view ->
                                val bundle = Bundle().apply {
                                    val item = currentList[holder.adapterPosition]
                                    putParcelable("list_detail", item)
                                    putInt("type", ListType.ALBUM_LIST)
                                }
                                Navigation.findNavController(view)
                                    .navigate(
                                        R.id.action_newAlbumFragment_to_reuseListFragment,
                                        bundle
                                    )
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

    override fun submitList(list: MutableList<SingleMonthData>?, commitCallback: Runnable?) {
        super.submitList(list?.let { ArrayList(it) }, commitCallback)
    }

    private fun holderInit(holder: Holder, position: Int) {
        val borderWidth = (screenWidth.toFloat() / 2).toInt()
        val item = currentList[position]
        with(holder.itemView) {
            iv_album.layoutParams.width = borderWidth
            iv_album.layoutParams.height = borderWidth
            tv_album_name.layoutParams.width = borderWidth
            tv_album_artist.layoutParams.width = borderWidth

            Glide.with(this).load(item.imageUrl).placeholder(R.drawable.shimmer_bg)
                .error(R.drawable.shimmer_bg).into(iv_album)

            var artists = ""
            item.artists.forEach {
                artists += it.name
                if (item.artists.lastIndex != item.artists.indexOf(it)) {
                    artists += "，"
                }
            }
            tv_album_artist.text = artists
            tv_album_name.text = item.name
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
            holderInit(holder, position)
        } else {
            footerState(holder as Footer)
        }
    }


}