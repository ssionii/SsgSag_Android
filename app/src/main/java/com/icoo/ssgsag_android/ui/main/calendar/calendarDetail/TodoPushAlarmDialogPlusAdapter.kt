package com.icoo.ssgsag_android.ui.main.calendar.calendarDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.color.MaterialColors.getColor
import com.icoo.ssgsag_android.R
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColor

class TodoPushAlarmDialogPlusAdapter(
    private val context: Context,
    private val dday : String?,
    private val checkedList : ArrayList<Int>
) : BaseAdapter() {

    private var selectedField = 0
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

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
        val viewHolder: ViewHolder
        var view: View? = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_dialog_plus_push_alarm, parent, false)
            viewHolder = ViewHolder(
                view!!.findViewById(R.id.text_view),
                view.findViewById(R.id.checkbox)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        when(position){
            0-> viewHolder.textView.text = "알림 없음"
            1-> viewHolder.textView.text = "마감 당일 알림(오전 10시)"
            2-> viewHolder.textView.text = "마감 1일 전 알림"
            3-> viewHolder.textView.text = "마감 3일 전 알림"
            4-> viewHolder.textView.text = "마감 7일 전 알림"
        }

        // 이미 Check한 list 체크 표시로 뿌려주기
        checkedList.let{
            if(it.size == 0){
                when(position) {
                    0-> viewHolder.checkBox.backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_on_yellow)
                }
            }else{
                for(i in 0 until it.size){
                    when(it[i]){
                        0 -> if(position == 1) viewHolder.checkBox.backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_on_yellow)
                        1 -> if(position == 2) viewHolder.checkBox.backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_on_yellow)
                        3 -> if(position == 3) viewHolder.checkBox.backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_on_yellow)
                        7 -> if(position == 4) viewHolder.checkBox.backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_on_yellow)

                    }
                }
            }
        }


        dday?.let {
            if(it.toInt() > 7){
                viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.black_1))
            }else if(it.toInt() > 3){
                when(position) {
                    4 -> {
                        viewHolder.textView.setTextColor(com.icoo.ssgsag_android.ui.main.feed.context.getColor(R.color.grey_2))
                        viewHolder.checkBox.apply{
                            isClickable = false
                            backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_disabled)
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
                            backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_disabled)
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
                            backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_disabled)
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
                            backgroundDrawable = context.getDrawable(R.drawable.ic_checkbox_disabled)
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

    fun setSelectedField(select: Int){
        selectedField = select
    }

    data class ViewHolder(val textView: TextView, val checkBox: CheckBox)


}