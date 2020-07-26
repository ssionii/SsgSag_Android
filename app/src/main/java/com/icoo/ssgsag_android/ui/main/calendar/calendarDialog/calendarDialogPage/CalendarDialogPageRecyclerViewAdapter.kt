package com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage

import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.ItemCalendarScheduleBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class CalendarDialogPageRecyclerViewAdapter(
    val items: ArrayList<Schedule>
) : RecyclerView.Adapter<CalendarDialogPageRecyclerViewAdapter.ViewHolder>() {

    private var listener: OnScheduleItemClickListener? = null

    fun setOnScheduleItemClickListener(listener: OnScheduleItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(items: ArrayList<Schedule>?) {
        items?.let {
            this.items.run {
                clear()
                addAll(it)
            }
        }
    }

    fun replace(item: Schedule, position: Int){
        items[position] = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemCalendarScheduleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_calendar_schedule, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.schedule = items[position]
        holder.dataBinding.root.setSafeOnClickListener {
            listener?.onItemClicked(items[position].posterIdx)
        }
        holder.dataBinding.root.setOnLongClickListener {
            listener?.onItemLongClicked(items[position].posterIdx, items[position].posterName)
            return@setOnLongClickListener true
        }
        holder.dataBinding.itemCalendarScheduleIvLike.setSafeOnClickListener {
            listener?.onBookmarkClicked(items[position].posterIdx, items[position].isFavorite , items[position].dday, position)
        }
        holder.dataBinding.itemCalendarScheduleIvSelect.setSafeOnClickListener {
            listener?.onSelectorClicked(items[position].posterIdx, items[position].posterName, position)
        }

    }

    override fun getItemId(position: Int): Long {
        return items[position].posterIdx.toLong()
    }

    inner class ViewHolder(val dataBinding: ItemCalendarScheduleBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnScheduleItemClickListener {
        fun onItemClicked(posterIdx: Int)
        fun onBookmarkClicked(posterIdx: Int, isFavorite: Int, dday : Int, position : Int)
        fun onItemLongClicked(posterIdx: Int, posterName: String)
        fun onSelectorClicked(posterIdx: Int, posterName: String, position: Int)
    }

    fun setSelectType(type: Int){
        for(item in items)
            item.selectType = type
    }

    fun getSelectType() : Boolean {
        for (item in items) {
            if (item.selectType == 2) {
                return true
            }
        }
        return false
    }
}