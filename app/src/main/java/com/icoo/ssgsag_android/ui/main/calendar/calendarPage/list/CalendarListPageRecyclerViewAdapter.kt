package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.ItemCalendarListBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.image

class CalendarListPageRecyclerViewAdapter() : RecyclerView.Adapter<CalendarListPageRecyclerViewAdapter.ViewHolder>() {

    var itemList: ArrayList<Schedule> = arrayListOf<Schedule>()
    private var listener: OnScheduleItemClickListener? = null

    fun setOnScheduleItemClickListener(listener: OnScheduleItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<Schedule>?, isLastSaveFilter : Boolean?) {
        if(array?.size != null) {
            itemList.clear()
            itemList.addAll(array)

            if(isLastSaveFilter != null && !isLastSaveFilter){
                for (i in itemList.indices) {
                    itemList[i].isAlone = !(i > 0 &&
                            itemList[i].posterEndDate.substring(8, 10) == itemList[i - 1].posterEndDate.substring(8, 10))

                    itemList[i].isLast = !(i < itemList.size - 1 &&
                            itemList[i].posterEndDate.substring(8, 10) == itemList[i + 1].posterEndDate.substring(8, 10))
                }
            }else{
                for (i in itemList.indices) {
                    itemList[i].isAlone = false
                    itemList[i].isLast = true
                }
            }
        }

    }

    fun replace(item : Schedule, position : Int){
        itemList[position] = item
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemCalendarListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_calendar_list, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.schedule = itemList[position]
        holder.dataBinding.root.setSafeOnClickListener {

            listener?.onItemClicked(itemList[position].posterIdx, position)
        }

        holder.dataBinding.itemCalendarListIvFavorite.setSafeOnClickListener {
            listener?.onBookmarkClicked(itemList[position].posterIdx, itemList[position].isFavorite, itemList[position].dday, position)
        }
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].posterIdx.toLong()
    }

    inner class ViewHolder(val dataBinding: ItemCalendarListBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnScheduleItemClickListener {
        fun onItemClicked(posterIdx: Int, position: Int)
        fun onBookmarkClicked(posterIdx: Int, isFavorite: Int, dday : Int, position: Int)
    }

    fun setSelectType(type: Int){
        for(item in itemList){
            item.selectType = type
        }
    }

}