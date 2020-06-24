package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

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
import com.icoo.ssgsag_android.databinding.FragmentCalendarListPageBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.calendar.CalendarViewModel
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.DateUtil
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import com.igaworks.v2.core.AdBrixRm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarListPageFragment : BaseFragment<FragmentCalendarListPageBinding, CalendarViewModel>(),
   MainActivity.onKeyBackPressedListener{

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_list_page
    override val viewModel: CalendarViewModel by viewModel()

    var calendarListPageRecyclerViewAdapter = CalendarListPageRecyclerViewAdapter()

    private var dataList: ArrayList<Schedule> = arrayListOf()

    private var isFavorite = false
    private var filterClick = false
    var curPosition = 0

    val date : Date = Calendar.getInstance().time
    val year = DateUtil.yearFormat.format(date)
    val month = DateUtil.monthFormat.format(date)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel
        isFavorite = arguments!!.getBoolean("isFavorite")

        setRecyclerView()
        setButton()

//        viewModel.isFavorite.observe(this, Observer {
//            makeScheduleList(year, month)
//        })

    }

    private fun setRecyclerView() {

        viewDataBinding.fragCalendarListPageRv.apply {
            adapter = calendarListPageRecyclerViewAdapter.apply {
                setOnScheduleItemClickListener(onScheduleItemClickListener)
                setHasStableIds(true)
            }

            (itemAnimator as SimpleItemAnimator).run {
                changeDuration = 0
                supportsChangeAnimations = false
            }

            layoutManager = WrapContentLinearLayoutManager()

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_IDLE) {

                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        calendarListPageRecyclerViewAdapter.apply {

                            if (itemList.size < curPosition) curPosition = position

                            if (itemList.size > 0 &&
                                this.getItemDate(curPosition) != this.getItemDate(position)
                            ) {
                                curPosition = position

//                                val date =
//                                    (adapter as CalendarListPageRecyclerViewAdapter).getItemDate(
//                                        position
//                                    )
//                                val monthInt = date.substring(5, 7).toInt()
//                                val headerDate =
//                                    date.substring(0, 4) + "년 " + monthInt.toString() + "월"
//                                viewModel.setHeaderDate(headerDate)

                            }
                        }

                    }
                }

            })

        }

        makeScheduleList(year, month)

        viewModel.schedule.observe(this, Observer {
            makeScheduleList(year, month)
        })

        viewModel.favoriteSchedule.observe(this, Observer {
            makeScheduleList(year, month)
        })
    }

    private fun makeScheduleList(year: String, month: String){

        dataList.clear()
        dataList = viewModel.filterScheduleFromList(year, month, isFavorite)

        calendarListPageRecyclerViewAdapter.apply {
            setSelectType(0)
            replaceAll(dataList, viewModel.isLastSaveFilter.value!!)

            if(dataList.size != 0) {
                if(filterClick){
                    viewDataBinding.fragCalendarListPageRv.scrollToPosition(0)
                    curPosition = 0
                    filterClick = false
                    notifyDataSetChanged()
//                    notifyItemRangeChanged(0, itemCount)
                }else {
                    notifyItemChanged(viewModel.changedPosterPosition)
                }
            }
        }
    }

    private val onScheduleItemClickListener =
        object :
            CalendarListPageRecyclerViewAdapter.OnScheduleItemClickListener {
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

            override fun onBookmarkClicked(posterIdx: Int, isFavorite: Int, position: Int) {
                viewModel.bookmark(posterIdx, isFavorite, position)

            }

            override fun onSelectorClicked(posterIdx: Int, posterName:String, isSelected: Boolean) {}
        }

    private fun setButton(){
        viewDataBinding.fragCalListPageCvLastSave.setSafeOnClickListener {
            viewModel.isLastSaveFilter.value = true

            filterClick = true

            viewModel.getAllCalendar()
            viewModel.getFavoriteSchedule()
        }

        viewDataBinding.fragCalListPageCvDeadline.setSafeOnClickListener {
            viewModel.isLastSaveFilter.value = false

            filterClick = true

            viewModel.getAllCalendar()
            viewModel.getFavoriteSchedule()
        }
    }

    override fun onBack() {
        val activity = activity as MainActivity
        activity.setOnKeyBackPressedListener(null)
        activity.onBackPressed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as MainActivity).setOnKeyBackPressedListener(this)
    }

    companion object{
        fun newInstance(isFavorite: Boolean): CalendarListPageFragment {
            val args = Bundle()
            args.putBoolean("isFavorite", isFavorite)

            val fragment = CalendarListPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}