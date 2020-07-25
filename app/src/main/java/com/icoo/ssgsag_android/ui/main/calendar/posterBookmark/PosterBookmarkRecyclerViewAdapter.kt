package com.icoo.ssgsag_android.ui.main.calendar.posterBookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.ItemCalendarListBinding
import com.icoo.ssgsag_android.databinding.ItemPosterBookmarkBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class PosterBookmarkRecyclerViewAdapter : RecyclerView.Adapter<PosterBookmarkRecyclerViewAdapter.ViewHolder>() {

    var itemList: ArrayList<PosterAlarmData> = arrayListOf()
    private var listener: onAlarmItemClickListener? = null

    fun setOnAlarmItemClickListener(listener: onAlarmItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<PosterAlarmData>?) {
        if(array?.size != null) {
            itemList.clear()
            itemList.addAll(array)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemPosterBookmarkBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_poster_bookmark, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.posterAlarm = itemList[position]

        val item = itemList[position]

        if(item.isPast){
            holder.dataBinding.itemPosterBookmarkIv.apply {
                isClickable = false

                if(item.isChecked) setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox_disabled_done))
                else setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox_disabled))
            }
        }else {
            holder.dataBinding.itemPosterBookmarkIv.apply {
                if(item.isChecked) setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox_on_yellow))
                else setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox_off))
            }
        }

        holder.dataBinding.root.setSafeOnClickListener {
            if(!item.isPast) listener?.onItemClicked(position)
        }
    }

    inner class ViewHolder(val dataBinding: ItemPosterBookmarkBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface onAlarmItemClickListener {
        fun onItemClicked(idx: Int)
    }

}

data class PosterAlarmData (
    val day : Int,
    val description : String,
    var isChecked : Boolean,
    var isPast : Boolean
)