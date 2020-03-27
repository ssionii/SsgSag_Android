package com.icoo.ssgsag_android.ui.main.myPage.notice

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.icoo.ssgsag_android.data.model.notice.Notice
import com.icoo.ssgsag_android.util.listener.INoticeItemClickListener
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class NoticeTitleRecyclerViewAdapter(val ctx: Context, val list: ArrayList<Notice>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<NoticeTitleRecyclerViewAdapter.Holder>() {

    private var listener: INoticeItemClickListener? = null

    fun setOnItemClickListener(listener: INoticeItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.rv_my_page_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = list[position].noticeName
        holder.layout.setSafeOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(list[position])
            }
        }
    }
    inner class Holder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.rv_my_page_item_tv_title) as TextView
        val layout : RelativeLayout = itemView.findViewById(R.id.rv_my_page_item_rl) as RelativeLayout
    }
}