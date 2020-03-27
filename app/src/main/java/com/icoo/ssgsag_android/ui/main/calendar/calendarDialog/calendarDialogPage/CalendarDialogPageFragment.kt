package com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.FragmentCalendarDialogPageBinding
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.CalendarDialogFragment
import com.icoo.ssgsag_android.util.DateUtil.dateFormat
import com.icoo.ssgsag_android.util.DateUtil.monthFormat
import com.icoo.ssgsag_android.util.DateUtil.yearFormat
import com.icoo.ssgsag_android.util.extensionFunction.getDayOfWeek
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import com.igaworks.v2.core.AdBrixRm
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarDialogPageFragment : BaseFragment<FragmentCalendarDialogPageBinding, CalendarDialogPageViewModel>() ,
     CalendarDialogPageDeleteDialogFragment.OnDialogDismissedListener{

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_dialog_page
    override val viewModel: CalendarDialogPageViewModel by viewModel()

    private var dialogFragment = CalendarDialogPageDeleteDialogFragment()

    private var timeByMillis: Long = 0
    private var categoryList = ArrayList<Int>()
    private var showFavorite = false
    private var year = ""
    private var month = ""
    private var day = ""
    private val calendar = Calendar.getInstance()

    private var deletePosterIdxList = arrayListOf<Int>()
    private var deletePosterNameList = arrayListOf<String>()

    private var isDeleted = false

    override fun onDialogDismissed(bool:Boolean, posterIdxList:ArrayList<Int>) {
        if(bool) {
            isDeleted = bool
            viewModel.deleteSchedule(posterIdxList, year, month, day, categoryList)
            CalendarDialogFragment.GetUpdate.isUpdated = true
        }

        (viewDataBinding.fragCalendarDialogPageRv
            .adapter as CalendarDialogPageRecyclerViewAdapter).apply {
            setSelectType(0)
            notifyDataSetChanged()
        }

        viewDataBinding.fragCalendarDialogPageTvDelete.visibility = INVISIBLE
        viewDataBinding.fragCalendarDialogPageIvEdit.visibility = VISIBLE
        viewDataBinding.fragCalendarDialogPageIvBack.visibility = INVISIBLE

        deletePosterIdxList.clear()
        deletePosterNameList.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setEditButton()

        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(activity, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        setSchedule()
        setScheduleRv()

        viewDataBinding.fragCalendarDialogPageIvEdit.visibility = VISIBLE
        viewDataBinding.fragCalendarDialogPageIvBack.visibility = INVISIBLE
        viewDataBinding.fragCalendarDialogPageTvDelete.visibility = INVISIBLE

    }

    fun setTimeByMillis(timeByMillis: Long) {
        this.timeByMillis = timeByMillis
    }

    fun setCategoryList(list: ArrayList<Int>){
        this.categoryList = list
    }
    fun setShowFavorite(bool: Boolean){
        this.showFavorite = bool
    }

    private fun setSchedule() {
        calendar.timeInMillis = timeByMillis

        val date = Date()
        date.time = timeByMillis

        year = yearFormat.format(date)
        month = monthFormat.format(date)
        day = dateFormat.format(date)

        var dayOfWeek = ""
        dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))

        viewDataBinding.fragCalendarDialogPageTvDate.text = "${month.toInt()}월 ${day.toInt()}일 ${dayOfWeek}요일"

        if(!showFavorite)
            viewModel.getAllSchedule(year, month, day, categoryList)
        else
            viewModel.getFavoriteSchedule(year, month, day)
    }

    private fun setScheduleRv() {
        viewDataBinding.fragCalendarDialogPageRv.apply {
            viewModel.schedule.observe(this@CalendarDialogPageFragment, Observer { value ->
                if (adapter != null) {
                    (this.adapter as CalendarDialogPageRecyclerViewAdapter).apply {
                        replaceAll(value)
                        //notifyDataSetChanged()
                        if(isDeleted){
                            notifyItemRangeRemoved(0, value.size+1)
                            notifyItemRangeInserted(0, value.size)
                            isDeleted = false
                        }else
                            notifyItemRangeChanged(0, value.size)
                    }
                } else {
                    adapter =
                        CalendarDialogPageRecyclerViewAdapter(value).apply {
                            setOnScheduleItemClickListener(onScheduleItemClickListener)
                        }
                    (this.itemAnimator as SimpleItemAnimator).run {
                        changeDuration = 0
                        supportsChangeAnimations = false
                    }
                }
            })

           layoutManager = WrapContentLinearLayoutManager()
        }
    }

    private val onScheduleItemClickListener =
        object : CalendarDialogPageRecyclerViewAdapter.OnScheduleItemClickListener {
            override fun onItemClicked(posterIdx: Int){
                viewModel.navigate(posterIdx)

                //adbrix
                AdBrixRm.event("touchUp_PosterDetail",
                    AdBrixRm.AttrModel().setAttrs("posterIdx",posterIdx.toLong()))
            }

            override fun onItemLongClicked(posterIdx: Int, posterName: String) {

                dialogFragment.setOnDialogDismissedListener(this@CalendarDialogPageFragment)
                dialogFragment.setInfo(posterIdx, posterName)
                dialogFragment.show(fragmentManager!!, "schedule delete dialog")
            }

            override fun onBookmarkClicked(posterIdx: Int, isFavorite: Int) {
                viewModel.bookmark(posterIdx, isFavorite, year, month, day, categoryList)
                CalendarDialogFragment.GetUpdate.isUpdated = true

            }

            override fun onSelectorClicked(posterIdx: Int, posterName: String, isSelected: Boolean) {

                (viewDataBinding.fragCalendarDialogPageRv
                    .adapter as CalendarDialogPageRecyclerViewAdapter).apply {
                    if(getSelectType()){
                        viewDataBinding.fragCalendarDialogPageTvDelete.visibility = VISIBLE
                        viewDataBinding.fragCalendarDialogPageIvEdit.visibility = INVISIBLE
                        viewDataBinding.fragCalendarDialogPageIvBack.visibility = INVISIBLE

                        if(isSelected){
                            deletePosterIdxList.add(posterIdx)
                            deletePosterNameList.add(posterName)
                        } else {
                            deletePosterIdxList.remove(posterIdx)
                            deletePosterNameList.remove(posterName)
                        }

                    }else{
                        viewDataBinding.fragCalendarDialogPageTvDelete.visibility = INVISIBLE
                        viewDataBinding.fragCalendarDialogPageIvEdit.visibility = INVISIBLE
                        viewDataBinding.fragCalendarDialogPageIvBack.visibility = VISIBLE

                        deletePosterNameList.clear()
                        deletePosterIdxList.clear()

                    }
                }
            }
        }

    private fun setEditButton(){
        viewDataBinding.fragCalendarDialogPageIvEdit.setSafeOnClickListener {

            viewDataBinding.fragCalendarDialogPageTvDelete.visibility = INVISIBLE
            viewDataBinding.fragCalendarDialogPageIvEdit.visibility = INVISIBLE
            viewDataBinding.fragCalendarDialogPageIvBack.visibility = VISIBLE

            // item들도 select할 수 있게 바꿈
            (viewDataBinding.fragCalendarDialogPageRv
                .adapter as CalendarDialogPageRecyclerViewAdapter).apply {
                setSelectType(1)
                notifyDataSetChanged()
            }
        }

        viewDataBinding.fragCalendarDialogPageIvBack.setSafeOnClickListener {

            viewDataBinding.fragCalendarDialogPageTvDelete.visibility = INVISIBLE
            viewDataBinding.fragCalendarDialogPageIvEdit.visibility = VISIBLE
            viewDataBinding.fragCalendarDialogPageIvBack.visibility = INVISIBLE

            (viewDataBinding.fragCalendarDialogPageRv
                .adapter as CalendarDialogPageRecyclerViewAdapter).apply {
                setSelectType(0)
                notifyDataSetChanged()
            }

            deletePosterIdxList.clear()
            deletePosterNameList.clear()
        }

        viewDataBinding.fragCalendarDialogPageTvDelete.setSafeOnClickListener {
            dialogFragment = CalendarDialogPageDeleteDialogFragment()

            dialogFragment.setOnDialogDismissedListener(this@CalendarDialogPageFragment)
            dialogFragment.setInfo(deletePosterIdxList, deletePosterNameList)
            dialogFragment.show(fragmentManager!!, "schedule delete dialog")
        }
    }

    /*private fun goToSsgSag(){
        viewDataBinding.fragCalendarDialogPageTvSsgsag.apply{
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                (activity as MainActivity).moveFragment(1)
            }
        }
    }*/


    companion object {
        private val TAG = "CalendarDialogPageFragment"

        fun newInstance(position: Int): CalendarDialogPageFragment {
            val frg = CalendarDialogPageFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            frg.arguments = bundle
            return frg
        }
    }
}