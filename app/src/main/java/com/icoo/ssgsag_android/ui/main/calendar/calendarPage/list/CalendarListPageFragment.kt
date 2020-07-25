package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.FragmentCalendarListPageBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.TodoPushAlarmDialogPlusAdapter
import com.icoo.ssgsag_android.ui.main.calendar.posterBookmark.PosterBookmarkBottomSheet
import com.icoo.ssgsag_android.util.DateUtil
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import com.igaworks.v2.core.AdBrixRm
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.GridHolder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class CalendarListPageFragment : BaseFragment<FragmentCalendarListPageBinding, CalendarListViewModel>(),
   MainActivity.onKeyBackPressedListener {

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_list_page
    override val viewModel: CalendarListViewModel by viewModel()

    lateinit var favoriteDialog : DialogPlus

    var calendarListPageRecyclerViewAdapter = CalendarListPageRecyclerViewAdapter()

    private var dataList: ArrayList<Schedule> = arrayListOf()

    private var isFavorite = false
    private var filterClick = false
    var curPosition = 0

    lateinit private var alarmCheckList: ArrayList<Boolean>
    private var clickBookmarkPosterIdx = 0
    private var clickIsFavorite = 0
    private var clickDday = 0

    val date : Date = Calendar.getInstance().time
    val year = DateUtil.yearFormat.format(date)
    val month = DateUtil.monthFormat.format(date)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel
        isFavorite = arguments!!.getBoolean("isFavorite")

        setRecyclerView()
        setButton()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllCalendar()
        viewModel.getFavoriteSchedule()
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
                        }

                    }
                }

            })

        }

        makeScheduleList(year, month)

        viewModel.schedule.observe(this, Observer {
            if(it.size > 0) {
                makeScheduleList(year, month)
            }
        })

        viewModel.favoriteSchedule.observe(this, Observer {
            if(it.size > 0) {
                makeScheduleList(year, month)
            }
        })
    }

    private fun makeScheduleList(year: String, month: String){

        dataList.clear()
        dataList = viewModel.filterScheduleFromList(year, month, isFavorite)

        calendarListPageRecyclerViewAdapter.apply {
            setSelectType(0)
            replaceAll(dataList, viewModel.isLastSaveFilter.value!!)
            notifyDataSetChanged()

            if(dataList.size != 0) {
                viewDataBinding.fragCalendarListPageRv.visibility = View.VISIBLE
                if(filterClick){
                    viewDataBinding.fragCalendarListPageRv.scrollToPosition(0)
                    curPosition = 0
                    filterClick = false
                    notifyDataSetChanged()
                }

                viewDataBinding.fragCalendarListPageRv.visibility = VISIBLE
                viewDataBinding.fragCalListPageTvEmpty.visibility = GONE
            }else{
                viewDataBinding.fragCalendarListPageRv.visibility = GONE
                viewDataBinding.fragCalListPageTvEmpty.visibility = VISIBLE
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

            override fun onBookmarkClicked(posterIdx: Int, isFavorite: Int, dday : Int, position: Int) {

                val posterBookmarkBottomSheet =  PosterBookmarkBottomSheet(posterIdx, dday, isFavorite, "list") {
                    bookmarkToggle(position, isFavorite, it)
                }
                posterBookmarkBottomSheet.isCancelable = false
                posterBookmarkBottomSheet.show(childFragmentManager, null)
            }

            override fun onSelectorClicked(posterIdx: Int, posterName:String, isSelected: Boolean) {}
        }

    private fun bookmarkToggle(position : Int, isFavorite: Int, toggle: Int){
        calendarListPageRecyclerViewAdapter.itemList[position].isFavorite = toggle
        calendarListPageRecyclerViewAdapter.notifyItemChanged(position)

        when(toggle){
            0 -> Toast.makeText(activity, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            1 -> {
                if(isFavorite == 0) Toast.makeText(activity, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setButton(){

        viewDataBinding.fragCalListPageCvDeadline.setSafeOnClickListener {
            viewModel.isLastSaveFilter.value = false

            filterClick = true

            viewModel.getAllCalendar()
            viewModel.getFavoriteSchedule()
        }

        viewDataBinding.fragCalListPageCvLastSave.setSafeOnClickListener {
            viewModel.isLastSaveFilter.value = true

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