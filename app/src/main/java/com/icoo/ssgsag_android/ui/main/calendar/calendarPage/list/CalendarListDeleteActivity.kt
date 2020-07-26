package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
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

            layoutManager = LinearLayoutManager(this@CalendarListDeleteActivity)
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

        if(dataList.size > 0) {

            calendarListPageRecyclerViewAdapter.apply {
                replaceAll(dataList, true)
                setSelectType(1)
                notifyDataSetChanged()
            }
        }else{
            viewDataBinding.actCalListDeleteRv.visibility = GONE
            viewDataBinding.actCalListDeleteLlEmpty.visibility = VISIBLE
        }
    }

    private fun setButton(){

        viewDataBinding.actCalListDeleteClBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actCalListDeleteClDelete.setSafeOnClickListener {
            if(deletePosterNameList.size > 0) {
                dialogFragment = CalendarDialogPageDeleteDialogFragment()

                dialogFragment.setOnDialogDismissedListener(this)
                dialogFragment.setInfo(deletePosterIdxList, deletePosterNameList)
                dialogFragment.show(supportFragmentManager, "schedule delete dialog")
            }
        }
    }

    private val onScheduleItemClickListener =
        object :
            CalendarListPageRecyclerViewAdapter.OnScheduleItemClickListener {

            override fun onBookmarkClicked(
                posterIdx: Int,
                isFavorite: Int,
                dday: Int,
                position: Int
            ) {}

            override fun onItemClicked(posterIdx: Int, position: Int) {
                val selectedItem = dataList[position]
                when(selectedItem.selectType){
                    1 -> {
                        dataList[position].selectType = 2
                        deletePosterNameList.add(selectedItem.posterName)
                        deletePosterIdxList.add(posterIdx)
                    }
                    2 -> {
                        dataList[position].selectType = 1
                        deletePosterNameList.remove(selectedItem.posterName)
                        deletePosterIdxList.remove(posterIdx)
                    }
                }

                if(deletePosterNameList.size != 0 ){
                    viewDataBinding.actCalListDeleteClDelete.apply{
                        setBackgroundColor(resources.getColor(R.color.ssgsag))
                        isClickable = true
                    }

                }else{
                    viewDataBinding.actCalListDeleteClDelete.apply{
                        setBackgroundColor(resources.getColor(R.color.grey_2))
                        isClickable = false
                    }
                }

                calendarListPageRecyclerViewAdapter.run{
                    replace(dataList[position], position)
                    notifyItemChanged(position)
                }
            }
        }
}