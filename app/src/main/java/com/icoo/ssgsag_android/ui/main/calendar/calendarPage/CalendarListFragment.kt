package com.icoo.ssgsag_android.ui.main.calendar.calendarPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.FragmentCalendarListBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.calendar.CalendarViewModel
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage.CalendarDialogPageDeleteDialogFragment
import com.icoo.ssgsag_android.util.DateUtil
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import com.igaworks.v2.core.AdBrixRm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarListFragment : BaseFragment<FragmentCalendarListBinding, CalendarViewModel>(),
    CalendarDialogPageDeleteDialogFragment.OnDialogDismissedListener, MainActivity.onKeyBackPressedListener{

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_list
    override val viewModel: CalendarViewModel by viewModel()

    lateinit private var calendarListPageRecyclerViewAdapter : CalendarListPageRecyclerViewAdapter

    lateinit private var dialogFragment : CalendarDialogPageDeleteDialogFragment
    lateinit private var dataList: ArrayList<Schedule>

    private var deletePosterIdxList = arrayListOf<Int>()
    private var deletePosterNameList = arrayListOf<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel
        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        Log.e("onResume", "hello")

        viewModel.setDeleteType(0)
    }

    override fun onStart() {
        super.onStart()
        Log.e("onStart", "hello")
    }

    override fun onDialogDismissed(bool:Boolean, posterIdxList:ArrayList<Int>) {
        if(bool) {
            viewModel.deleteSchedule(posterIdxList)
        }

        viewModel.setDeleteType(0)
        deletePosterIdxList.clear()
        deletePosterNameList.clear()
        activity!!.supportFragmentManager.beginTransaction().remove(dialogFragment).commit()
    }

    private fun setRecyclerView() {

        val date : Date = Calendar.getInstance().time

        val year = DateUtil.yearFormat.format(date)
        val month = DateUtil.monthFormat.format(date)

        var curPosition = 0

        dataList = viewModel.filterScheduleFromList(year,month)
        calendarListPageRecyclerViewAdapter = CalendarListPageRecyclerViewAdapter(dataList)

        viewDataBinding.fragCalendarListPageRv.apply {
            adapter = calendarListPageRecyclerViewAdapter.apply {
                setOnScheduleItemClickListener(onScheduleItemClickListener)
            }

            (itemAnimator as SimpleItemAnimator).run {
                changeDuration = 0
                supportsChangeAnimations = false
            }

            layoutManager = WrapContentLinearLayoutManager()

            addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if(newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_IDLE) {
                        var position = (layoutManager as WrapContentLinearLayoutManager).findFirstVisibleItemPosition()

                        calendarListPageRecyclerViewAdapter.apply{

                            if(itemList.size < curPosition) curPosition = position

                            if(itemList.size > 0 &&
                                this.getItemDate(curPosition) != this.getItemDate(position)){
                                curPosition = position

                                val date = (adapter as CalendarListPageRecyclerViewAdapter).getItemDate(position)
                                val monthInt = date.substring(5, 7).toInt()
                                val headerDate = date.substring(0,4) + "년 " + monthInt.toString() + "월"
                                viewModel.setHeaderDate(headerDate)
                            }
                        }

                    }
                }

            })

        }

        makeScheduleList(year, month)

        viewModel.isFavorite.observe(this@CalendarListFragment, Observer {
            makeScheduleList(year, month)

            Log.e("item 개수", viewDataBinding.fragCalendarListPageRv.adapter!!.itemCount.toString())
        })

        viewModel.schedule.observe(this@CalendarListFragment, Observer {
            makeScheduleList(year, month)

        })

        viewModel.favoriteSchedule.observe(this@CalendarListFragment, Observer {
            makeScheduleList(year, month)
        })


        viewModel.deleteType.observe(this@CalendarListFragment, Observer { value ->
            calendarListPageRecyclerViewAdapter.apply {

                if(value == 0){
                    setSelectType(0)
                    notifyItemRangeChanged(0, dataList.size)
                }else if(value == 1) {
                    setSelectType(1)
                    notifyItemRangeChanged(0, dataList.size)
                } else if(value == 3){
                    dialogFragment = CalendarDialogPageDeleteDialogFragment()

                    dialogFragment.setOnDialogDismissedListener(this@CalendarListFragment)
                    dialogFragment.setInfo(deletePosterIdxList, deletePosterNameList)
                    dialogFragment.show(fragmentManager!!, "schedule delete dialog")
                }
            }
        })
    }

    private fun makeScheduleList(year: String, month: String){

        viewModel.setDeleteType(0)
        dataList = viewModel.filterScheduleFromList(year, month)

        calendarListPageRecyclerViewAdapter.apply {
            setSelectType(0);
            replaceAll(dataList)
            if(dataList.size != 0) {
                notifyItemRangeChanged(0, dataList.size)
            }else
                notifyDataSetChanged()

            if(itemList.size > 0) {
                val date = this.getItemDate(0)
                val monthInt = date.substring(5, 7).toInt()
                val headerDate = date.substring(0,4) + "년 " + monthInt.toString() + "월"
                viewModel.setHeaderDate(headerDate)
            }
        }
    }

    private val onScheduleItemClickListener =
        object : CalendarListPageRecyclerViewAdapter.OnScheduleItemClickListener {
            override fun onItemClicked(posterIdx: Int){

                val intent = Intent(context, CalendarDetailActivity::class.java)
                val bundle = Bundle().apply {
                    putInt("Idx", posterIdx)
                    putString("from","calendar")
                }

                bundle?.let {
                    intent.putExtras(it)
                }

                startActivity(intent)

                //adbrix
                AdBrixRm.event("touchUp_PosterDetail",
                    AdBrixRm.AttrModel().setAttrs("posterIdx",posterIdx.toLong()))
            }

            override fun onBookmarkClicked(posterIdx: Int, isFavorite: Int) {
                viewModel.bookmark(posterIdx, isFavorite)
            }

            override fun onSelectorClicked(posterIdx: Int, posterName:String, isSelected: Boolean) {
                calendarListPageRecyclerViewAdapter.apply {
                    if(getSelectType()){ // 선택된게 하나라도 있음

                        viewModel.setDeleteType(2)

                        if(isSelected) {
                            deletePosterIdxList.add(posterIdx)
                            deletePosterNameList.add(posterName)
                        }
                        else{
                            deletePosterIdxList.remove(posterIdx)
                            deletePosterNameList.remove(posterName)
                        }


                    }else{
                        // 1이면 뒤로가기 2면 삭제
                        viewModel.setDeleteType(1)
                        deletePosterIdxList.clear()
                        deletePosterNameList.clear()
                    }
                }
            }
        }

    override fun onStop() {
        super.onStop()

        Log.e("onStop", "hello")
    }


    override fun onBack() {
        val activity = activity as MainActivity
        activity.setOnKeyBackPressedListener(null)
        activity.onBackPressed()

        Log.e("onBack", "hello")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as MainActivity).setOnKeyBackPressedListener(this)
    }
}