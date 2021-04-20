package com.example.graduationdesign.view.search.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.graduationdesign.R
import com.example.graduationdesign.model.SearchType
import com.example.graduationdesign.model.bean.playlist_bean.Playlist
import com.example.graduationdesign.model.bean.search_bean.CompleteAlbumBean
import com.example.graduationdesign.model.bean.search_bean.CompleteArtistBean
import com.example.graduationdesign.model.bean.search_bean.VideoBean
import com.example.graduationdesign.model.bean.song_list_bean.ArtistBean
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.tools.TimeFormat
import kotlinx.android.synthetic.main.layout_search_result_item_album.view.*
import kotlinx.android.synthetic.main.layout_search_result_item_artist.view.*
import kotlinx.android.synthetic.main.layout_search_result_item_playlist.view.*
import kotlinx.android.synthetic.main.layout_search_result_item_playlist.view.iv_search_result_item_image
import kotlinx.android.synthetic.main.layout_search_result_item_playlist.view.tv_search_result_item_name
import kotlinx.android.synthetic.main.layout_search_result_item_single.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchResultAdapter(private val type: Int) :
    ListAdapter<Any, RecyclerView.ViewHolder>(Compare) {
    class ArtistHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class SingleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class PlaylistHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class AlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is CompleteArtistBean -> {
                    oldItem.id == (newItem as CompleteArtistBean).id
                }

                is CompleteAlbumBean -> {
                    oldItem.id == (newItem as CompleteAlbumBean).id
                }

                is SongBean -> {
                    oldItem.id == (newItem as SongBean).id
                }

                is VideoBean -> {
                    oldItem.vid == (newItem as VideoBean).vid
                }

                else -> {
                    (oldItem as Playlist).id == (newItem as Playlist).id
                }
            }
        }
    }

    override fun submitList(list: MutableList<Any>?, commitCallback: Runnable?) {
        super.submitList(list?.let { ArrayList(it) }, commitCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            SearchType.ARTIST -> {
                ArtistHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_search_result_item_artist, parent, false)
                )
            }
            SearchType.SINGLE -> {
                SingleHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_search_result_item_single, parent, false).also {
                            it.setOnClickListener {

                            }
                        }
                )
            }

            SearchType.ALBUM -> {
                AlbumHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_search_result_item_album, parent, false)
                )
            }

            SearchType.VIDEO -> {
                VideoHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_search_result_item_video, parent, false)
                )
            }

            else -> {
                PlaylistHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_search_result_item_playlist, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArtistHolder -> {
                val item = currentList[position] as CompleteArtistBean
                with(holder.itemView) {
                    Glide.with(this)
                        .load(item.picUrl)
                        .error(R.drawable.shimmer_circle_bg)
                        .placeholder(R.drawable.shimmer_circle_bg)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_search_image)

                    tv_search_name.text = item.name
                }
            }

            is PlaylistHolder -> {
                val item = currentList[position] as Playlist
                with(holder.itemView) {
                    Glide.with(this)
                        .load(item.imageUrl)
                        .error(R.drawable.shimmer_bg)
                        .placeholder(R.drawable.shimmer_bg)
                        .into(iv_search_result_item_image)

                    tv_search_result_item_name.text = item.name
                    val playCount = "${item.playCount}次播放"
                    tv_search_result_item_play_count.text = playCount
                }
            }

            is SingleHolder -> {
                val item = currentList[position] as SongBean
                with(holder.itemView) {
                    tv_song_name.text = item.name

                    var artists = ""
                    item.artists.forEach {
                        artists += it.name
                        if (item.artists.lastIndex != item.artists.indexOf(it)) {
                            artists += "，"
                        }
                    }
                    artists += " - ${item.album.name} "
                    tv_song_artist.text = artists
                }
            }

            is AlbumHolder -> {
                val item = currentList[position] as CompleteAlbumBean
                with(holder.itemView) {
                    tv_search_result_item_name.text = item.name

                    Glide.with(this)
                        .load(item.picUrl)
                        .error(R.drawable.shimmer_bg)
                        .placeholder(R.drawable.shimmer_bg)
                        .into(iv_search_result_item_image)

                    var info = ""
                    item.artist.forEach {
                        info += it.name
                        if (item.artist.lastIndex != item.artist.indexOf(it)) {
                            info += "/"
                        }
                    }
                    info += " " + SimpleDateFormat("yyyy.mm.dd", Locale.getDefault()).format(
                        Date(
                            item.publishTime
                        )
                    )
                    tv_search_result_item_info.text = info
                }
            }

            is VideoHolder -> {
                val item = currentList[position] as VideoBean
                with(holder.itemView) {
                    tv_search_result_item_name.text = item.title

                    Glide.with(this)
                        .asBitmap()
                        .load(item.coverUrl)
                        .error(R.drawable.shimmer_bg)
                        .placeholder(R.drawable.shimmer_bg)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                iv_search_result_item_image.layoutParams.width =
                                    (resource.width / resource.height) * iv_search_result_item_image.layoutParams.height
                                iv_search_result_item_image.setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })

                    var info = ""
                    item.creator.forEach {
                        info += it.userName
                        if (item.creator.lastIndex != item.creator.indexOf(it)) {
                            info += "/"
                        }
                    }

                    info =
                        TimeFormat.timeFormat(item.durationms) + "  " + info + "  " + item.playTime + "次播放"
                    tv_search_result_item_info.text = info
                }
            }
        }
    }

}