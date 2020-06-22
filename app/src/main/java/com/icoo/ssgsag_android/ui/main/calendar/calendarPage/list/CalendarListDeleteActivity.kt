package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.ActivityCalendarListDeleteBinding
import com.icoo.ssgsag_android.databinding.ItemCalendarListBinding
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage.CalendarDialogPageDeleteDialogFragment
import com.icoo.ssgsag_android.util.DateUtil
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarListDeleteActivity : BaseActivity<ActivityCalendarListDeleteBinding, CalendarListDeleteViewModel>(),
    CalendarDialogPageDeleteDialogFragment.OnDialogDismissedListener {

    override val layoutResID: Int
        get() = R.layout.activity_calendar_list_delete

    override val viewModel: CalendarListDeleteViewModel by viewModel()

    lateinit private var dialogFragment : CalendarDialogPageDeleteDialogFragment

    var calendarListPageRecyclerViewAdapter = CalendarListPageRecyclerViewAdapter()
    private var dataList: ArrayList<Schedule> = arrayListOf()

    private var deletePosterNameList : ArrayList<String> = arrayListOf()
    private var deletePosterIdxList : ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        setRv()
        setButton()
    }

    override fun onDialogDismissed(bool:Boolean, posterIdxList:ArrayList<Int>) {
        if(bool) {
            viewModel.deleteSchedule(posterIdxList)
            deletePosterNameList.clear()
            deletePosterIdxList.clear()
            finish()
        }
    }

    private fun setRv(){

        viewDataBinding.actCalListDeleteRv.apply {
            adapter = calendarListPageRecyclerViewAdapter.apply {
                setOnScheduleItemClickListener(onScheduleItemClickListener)
            }

            (itemAnimator as SimpleItemAnimator).run {
                changeDuration = 0
                supportsChangeAnimations = false
            }

            layoutManager = WrapContentLinearLayoutManager()

            var curPosition = 0

            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if(newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {

                        var position = (layoutManager as WrapContentLinearLayoutManager).findFirstVisibleItemPosition()

                        calendarListPageRecyclerViewAdapter.apply{

                            if(itemList.size < curPosition) curPosition = position

                            if(itemList.size > 0 &&
                                this.getItemDate(curPosition) != this.getItemDate(position)){
                                curPosition = position
                            }
                        }

                    }
                }

            })

        }

        val date : Date = Calendar.getInstance().time
        val year = DateUtil.yearFormat.format(date)
        val month = DateUtil.monthFormat.format(date)

        viewModel.schedule.observe(this, androidx.lifecycle.Observer {
            makeScheduleList(year, month)
        })

    }

    private fun makeScheduleList(year: String, month: String){

        dataList.clear()
        dataList = viewModel.filterSchedule(year, month)

        calendarListPageRecyclerViewAdapter.apply {
            replaceAll(dataList)
            setSelectType(1)
            if(dataList.size != 0) {
                notifyItemRangeChanged(0, dataList.size)
            }else
                notifyDataSetChanged()
        }
    }

    private fun setButton(){

        viewDataBinding.actCalListDeleteClBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actCalListDeleteClDelete.setSafeOnClickListener {
            dialogFragment = CalendarDialogPageDeleteDialogFragment()

            dialogFragment.setOnDialogDismissedListener(this)
            dialogFragment.setInfo(deletePosterIdxList, deletePosterNameList)
            dialogFragment.show(supportFragmentManager, "schedule delete dialog")
        }
    }

    private val onScheduleItemClickListener =
        object :
            CalendarListPageRecyclerViewAdapter.OnScheduleItemClickListener {

            override fun onBookmarkClicked(posterIdx: Int, isFavorite: Int, position:Int) {}

            override fun onItemClicked(posterIdx: Int) {}

            override fun onSelectorClicked(
                posterIdx: Int,
                posterName: String,
                isSelected: Boolean
            ) {
                if(isSelected){
                    deletePosterNameList.add(posterName)
                    deletePosterIdxList.add(posterIdx)
                }
                else {
                    deletePosterNameList.remove(posterName)
                    deletePosterIdxList.remove(posterIdx)
                }

                if(deletePosterNameList.size != 0 ){
                    viewDataBinding.actCalListDeleteClDelete.setBackgroundColor(resources.getColor(R.color.ssgsag))
                }else{
                    viewDataBinding.actCalListDeleteClDelete.setBackgroundColor(resources.getColor(R.color.grey_2))
                }
            }
        }
}