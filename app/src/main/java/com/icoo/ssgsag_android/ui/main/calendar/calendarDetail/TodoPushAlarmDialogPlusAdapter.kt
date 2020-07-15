package com.icoo.ssgsag_android.ui.main.calendar.calendarDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.getColor
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColor

class TodoPushAlarmDialogPlusAdapter(
    private val context: Context,
    private val dday : String?,
    private var isCheckList : ArrayList<Boolean>
) : BaseAdapter() {

    private var listener: OnItemClickListener? = null
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: PushAlarmView
        var view: View? = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_dialog_plus_push_alarm, parent, false)
            viewHolder = PushAlarmView(
                view!!.findViewById(R.id.text_view),
                view.findViewById(R.id.image_view)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as PushAlarmView
        }

        when(position){
            0-> viewHolder.textView.text = "알림 없음"
            1-> viewHolder.textView.text = "마감 당일 알림(오전 10시)"
            2-> viewHolder.textView.text = "마감 1일 전 알림"
            3-> viewHolder.textView.text = "마감 3일 전 알림"
            4-> viewHolder.textView.text = "마감 7일 전 알림"
        }


        if(isCheckList[position]){
            viewHolder.checkBox.setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_on_yellow))
        }else{
            viewHolder.checkBox.setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_off))
        }

        if(isCheckList[0]){
            when(position) {
                0 -> { viewHolder.checkBox.setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_on_yellow)) }
                1, 2, 3, 4 -> {   viewHolder.checkBox.setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_off)) }
            }
        }

        viewHolder.checkBox.setSafeOnClickListener {
            listener?.onItemClick(position)
        }

        // 지난 날짜 click disable 시키기
        dday?.let {
            if(it.toInt() > 7){
                viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.black_1))
            }else if(it.toInt() > 3){
                when(position) {
                    4 -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.grey_2))
                        viewHolder.checkBox.apply{
                            isClickable = false
                            setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_disabled))
                        }
                    }
                    else -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.black_1))
                    }
                }
            }else if(it.toInt() > 1){
                when(position) {
                    3, 4 -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.grey_2))
                        viewHolder.checkBox.apply{
                            isClickable = false
                            setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_disabled))
                        }
                    }
                    else -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.black_1))
                    }
                }
            }else if(it.toInt() == 1){
                when(position) {
                    2, 3, 4 -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.grey_2))
                        viewHolder.checkBox.apply{
                            isClickable = false
                            setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_disabled))
                        }
                    }
                    else -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.black_1))
                    }
                }
            }else {
                when(position) {
                    1, 2, 3, 4 -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.grey_2))
                        viewHolder.checkBox.apply{
                            isClickable = false
                            setImageDrawable(context.getDrawable(R.drawable.ic_checkbox_disabled))
                        }
                    }
                    else -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.black_1))
                    }
                }
            }
        }

        return view
    }

    data class PushAlarmView(val textView: TextView, val checkBox: ImageView)


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun replace(list : ArrayList<Boolean>){
        this.isCheckList = list

        notifyDataSetChanged()

    }

}