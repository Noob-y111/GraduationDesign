package com.example.graduationdesign.view.dialog.content

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationdesign.R
import kotlinx.android.synthetic.main.layout_content_menu_item.view.*

class ContentMenuAdapter(
    private val list: ArrayList<String>,
    private val drawableList: ArrayList<Drawable>?
) : RecyclerView.Adapter<ContentMenuAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBehavior: TextView = itemView.tv_content_item_name
    }

    private var listener: OnItemClickCallback? = null
    private var dialog: ContentMenuDialog? = null

    interface OnItemClickCallback {
        fun onItemClickListener(view: View, position: Int, dialog: ContentMenuDialog?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_content_menu_item, parent, false)
        )
    }

    fun setOnItemClickListener(listener: OnItemClickCallback, contentMenuDialog: ContentMenuDialog) {
        this.listener = listener
        this.dialog = contentMenuDialog
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvBehavior.text = list[position]
        holder.itemView.setOnClickListener {
            listener?.onItemClickListener(it, position, dialog)
        }

        drawableList?.let {
            holder.tvBehavior.setCompoundDrawablesRelativeWithIntrinsicBounds(
                it[position],
                null,
                null,
                null
            )
        }
    }

    override fun getItemCount() = list.size
}