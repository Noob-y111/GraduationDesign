package com.example.graduationdesign.view.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.graduationdesign.R
import com.example.graduationdesign.model.bean.search_bean.SingleSuggest

class AutoCompleteListAdapter : BaseAdapter(), Filterable {

    private var list: ArrayList<SingleSuggest> = ArrayList()
    private var keyword: String = ""

    class Holder(view: View) {
        var textView: TextView = view.findViewById(R.id.tv_auto_item)
    }

    fun submitList(keyword: String, list: ArrayList<SingleSuggest>?){
        list?.let {
            this.list.clear()
            this.list.addAll(list)
        }
        submitKeyWord(keyword)
        notifyDataSetChanged()
    }

    private fun submitKeyWord(keyword: String){
        this.keyword = keyword
    }

    override fun getCount(): Int {
        return list.size + 1
    }

    override fun getItem(position: Int): Any {
        return if (position == 0) keyword else list[position-1].keyword
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: Holder
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_auto_list_item, parent, false)
            holder = Holder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }

        when (position) {
            0 -> {
                holder.textView.setCompoundDrawables(null, null, null, null)
                val text = "搜索“${keyword}”"
                holder.textView.paint.isFakeBoldText = true
                holder.textView.text = text
            }

            else -> {
                if (parent != null) {
                    holder.textView.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(parent.context, R.drawable.ic_search_gray_24dp),
                        null,
                        null,
                        null
                    )
                }
                holder.textView.paint.isFakeBoldText = false
                holder.textView.text = list[position - 1].keyword
            }
        }

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }
    }
}