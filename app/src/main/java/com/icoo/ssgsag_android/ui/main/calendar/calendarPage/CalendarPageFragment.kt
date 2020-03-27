package com.icoo.ssgsag_android.ui.main.calendar.calendarPage

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.util.Log
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.databinding.FragmentCalendarPageBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import com.icoo.ssgsag_android.data.model.date.Date
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.ItemCalendarDateBinding
import com.icoo.ssgsag_android.ui.main.calendar.CalendarFragment
import com.icoo.ssgsag_android.ui.main.calendar.CalendarViewModel
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.CalendarDialogFragment
import com.icoo.ssgsag_android.util.DateUtil.dateFormat
import com.icoo.ssgsag_android.util.DateUtil.monthFormat
import com.icoo.ssgsag_android.util.DateUtil.yearFormat
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import org.jetbrains.anko.support.v4.toast

class CalendarPageFragment : BaseFragment<FragmentCalendarPageBinding, CalendarViewModel>(),
    BaseRecyclerViewAdapter.OnItemClickListener, CalendarDialogFragment.OnDialogDismissedListener {

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_page

    override val viewModel: CalendarViewModel by viewModel()


    lateinit var realm: Realm

    private var timeByMillis: Long = 0
    private val dataList: ArrayList<Date> by lazy { ArrayList<Date>() }

    // 선택된 categoryList
    private var categoryList : ArrayList<Int> = arrayListOf()
    private var showFavorite = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        Realm.init(context)
        realm = Realm.getDefaultInstance()

        setRecyclerView()
        setCalendar()

        viewModel.isUpdated.observe(this, androidx.lifecycle.Observer { value->
            if(value) {
                viewModel.getAllCalendar()
                viewModel.setIsUpdated(false)
            }
        })
    }

    override fun onDialogDismissed(dataChanged: Boolean) {
        categoryList.clear()
        viewModel.setIsUpdated(dataChanged)
    }

    override fun onItemClicked(item: Any?, position: Int?){

        if(viewModel.categorySort.value!![0].isChecked) {
            categoryList.addAll(arrayListOf(0,1,2,4,5,7,8))
            showFavorite = false
        }
        else if(viewModel.categorySort.value!![1].isChecked){
            showFavorite = true
        }
        else{
            showFavorite = false
            for(i in 2..viewModel.categorySort.value!!.size-1) {
                if (viewModel.categorySort.value!![i].isChecked) {
                    categoryList.add(viewModel.categorySort.value!![i].categoryIdx)
                }
            }
        }

        val dialogFragment = CalendarDialogFragment()
        val args = Bundle()
        args.putStringArrayList("Date", arrayListOf((item as Date).year, item.month, item.date))
        args.putIntegerArrayList("categoryList", categoryList)
        args.putBoolean("showFavorite",showFavorite)

        dialogFragment.arguments = args
        dialogFragment.setOnDialogDismissedListener(this)
        dialogFragment.setTargetFragment(this, 0)
        dialogFragment.show(fragmentManager!!, "frag_dialog_cal")

    }

    fun changePage(isPrev: Boolean) {
        if (isPrev)
            (parentFragment as CalendarFragment).viewDataBinding.fragCalendarVpPage.currentItem -= 1
        else
            (parentFragment as CalendarFragment).viewDataBinding.fragCalendarVpPage.currentItem += 1
    }

    private fun setCalendar() {

        val mCalendar = Calendar.getInstance()
        val mPrevCalendar = Calendar.getInstance()
        val mNextCalendar = Calendar.getInstance()
        val mInstanceCal = Calendar.getInstance()

        //날짜 셋팅
        val date = Date(timeByMillis)

        //현재 캘린더의 년, 월, 일
        val year = yearFormat.format(date)
        val month = monthFormat.format(date)
        val day = dateFormat.format(date)

        //캘린터 설정
        mCalendar.set(year.toInt(), month.toInt() - 1, 1)
        if (month.toInt() - 1 == 1) {
            mPrevCalendar.set(year.toInt() - 1, 12, 1)
            mNextCalendar.set(year.toInt(), month.toInt(), 1)
        } else if (month.toInt() - 1 == 12) {
            mPrevCalendar.set(year.toInt(), month.toInt() - 2, 1)
            mNextCalendar.set(year.toInt() + 1, 1, 1)
        } else {
            mPrevCalendar.set(year.toInt(), month.toInt() - 2, 1)
            mNextCalendar.set(year.toInt(), month.toInt(), 1)
        }

        //캘린더 줄 수 설정
        val lines: Int
        if (mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 28) {
            if (mCalendar.get(Calendar.DAY_OF_WEEK) == 1)
                lines = 4
            else
                lines = 5
        } else if (mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 30) {
            if (mCalendar.get(Calendar.DAY_OF_WEEK) == 7)
                lines = 6
            else
                lines = 5
        } else if (mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 31) {
            if (mCalendar.get(Calendar.DAY_OF_WEEK) == 6 || mCalendar.get(Calendar.DAY_OF_WEEK) == 7)
                lines = 6
            else
                lines = 5
        } else
            lines = 5


        // 캘린더에 뿌리기
        viewModel.categorySort.observe(this, androidx.lifecycle.Observer {
            makeCalendarSchedule(mCalendar, mPrevCalendar, mNextCalendar, mInstanceCal, date, lines)
        })

        viewModel.schedule.observe(this, androidx.lifecycle.Observer {
            makeCalendarSchedule(mCalendar, mPrevCalendar, mNextCalendar, mInstanceCal, date, lines)

            for(i in 0..it.size){
                //insertMyPoster(it[i])
            }
        })

    }

    private fun refreshCal() {
        (viewDataBinding.fragCalendarPageRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
            replaceAll(dataList)
            notifyDataSetChanged()
        }

    }

    private fun setRecyclerView() {

        viewDataBinding.fragCalendarPageRv.run {
            adapter = object : BaseRecyclerViewAdapter<Date, ItemCalendarDateBinding>() {
                override val layoutResID: Int
                    get() = R.layout.item_calendar_date
                override val bindingVariableId: Int
                    get() = BR.date
                override val listener: OnItemClickListener?
                    get() = this@CalendarPageFragment
            }
            layoutManager = NonScrollGridLayoutManager(activity!!, 7)
        }
    }


    private fun makeCalendarSchedule(mCalendar: Calendar, mPrevCalendar: Calendar, mNextCalendar: Calendar,
                             mInstanceCal:Calendar, date: java.util.Date, lines: Int){

        //현재 캘린더의 년, 월, 일
        val year = yearFormat.format(date)
        val month = monthFormat.format(date)
        val day = dateFormat.format(date)

        //오늘의 년, 월, 일
        val toMonth = monthFormat.format(System.currentTimeMillis())
        val toYear = yearFormat.format(System.currentTimeMillis())
        val toDay = dateFormat.format(System.currentTimeMillis())

        dataList.clear()
        val dayNum = mCalendar.get(Calendar.DAY_OF_WEEK)
        val nextDayNum = mNextCalendar.get(Calendar.DAY_OF_WEEK)
        var instanceDayNum: Int
        //저번달
        for (i in mPrevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - dayNum + 2 until mPrevCalendar.getActualMaximum(
            Calendar.DAY_OF_MONTH
        ) + 1) {

            val scheduleList = ArrayList<Schedule>()

            lateinit var instanceScheduleList : ArrayList<Schedule>
            if(!viewModel.categorySort.value!![1].isChecked)
                instanceScheduleList = viewModel.getCalendarByCategory(mPrevCalendar.get(Calendar.YEAR).toString(),
                    mPrevCalendar.get(Calendar.MONTH).toString())
            else
                instanceScheduleList = viewModel.getCalendarByBookMark(mPrevCalendar.get(Calendar.YEAR).toString(),
                    mPrevCalendar.get(Calendar.MONTH).toString())

            for (j in instanceScheduleList.indices) {
                if (instanceScheduleList[j].posterEndDate.substring(8, 10).toInt() == i + 1) {
                    scheduleList.add(instanceScheduleList[j])
                }
            }


            dataList.add(
                Date(
                    mPrevCalendar.get(Calendar.YEAR).toString(),
                    (mPrevCalendar.get(Calendar.MONTH)+1).toString(),
                    i.toString(),
                    0,
                    false,
                    false,
                    false,
                    scheduleList,
                    lines
                )
            )
        }
        //이번달
        for (i in 0 until mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {

            mInstanceCal.set(year.toInt(), month.toInt() - 1, i + 1)
            instanceDayNum = mInstanceCal.get(Calendar.DAY_OF_WEEK)
            val scheduleList = ArrayList<Schedule>()

            lateinit var instanceScheduleList : ArrayList<Schedule>
            if(!viewModel.categorySort.value!![1].isChecked)
                instanceScheduleList = viewModel.getCalendarByCategory(year, month)
            else
                instanceScheduleList = viewModel.getCalendarByBookMark(year, month)

            for (j in instanceScheduleList.indices) {
                if (instanceScheduleList[j].posterEndDate.substring(8, 10).toInt() == i + 1) {
                    scheduleList.add(instanceScheduleList[j])
                }
            }

            if (i + 1 == toDay.toInt() && month == toMonth && year == toYear) {
                dataList.add(
                    Date(
                        year.toString(),
                        month.toString(),
                        (i + 1).toString(),
                        instanceDayNum,
                        true,
                        true,
                        false,
                        scheduleList,
                        lines
                    )
                )
            } else {
                dataList.add(
                    Date(
                        year.toString(),
                        month.toString(),
                        (i + 1).toString(),
                        instanceDayNum,
                        false,
                        true,
                        false,
                        scheduleList,
                        lines
                    )
                )
            }
        }
        //다음달
        if (nextDayNum != 1) {
            for (i in 1 until 9 - nextDayNum) {

                val scheduleList = ArrayList<Schedule>()

                lateinit var instanceScheduleList : ArrayList<Schedule>
                if(!viewModel.categorySort.value!![1].isChecked)
                    instanceScheduleList = viewModel.getCalendarByCategory(mNextCalendar.get(Calendar.YEAR).toString(),
                        mPrevCalendar.get(Calendar.MONTH).toString())
                else
                    instanceScheduleList = viewModel.getCalendarByBookMark(mNextCalendar.get(Calendar.YEAR).toString(),
                        mPrevCalendar.get(Calendar.MONTH).toString())

                for (j in instanceScheduleList.indices) {
                    if (instanceScheduleList[j].posterEndDate.substring(8, 10).toInt() == i + 1) {
                        scheduleList.add(instanceScheduleList[j])
                    }
                }

                dataList.add(
                    Date(
                        mNextCalendar.get(Calendar.YEAR).toString(),
                        (mNextCalendar.get(Calendar.MONTH) + 1).toString(),
                        i.toString(),
                        0,
                        false,
                        false,
                        false,
                        scheduleList,
                        lines
                    )
                )
            }
        }

        //setRecyclerView()
        refreshCal()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


    fun setTimeByMillis(timeByMillis: Long) {
        this.timeByMillis = timeByMillis
    }

    companion object {

        private val TAG = "CalendarPageFragment"
        fun newInstance(position: Int): CalendarPageFragment {
            val frg = CalendarPageFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            frg.arguments = bundle
            return frg
        }
    }
}