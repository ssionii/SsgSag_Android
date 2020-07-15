package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
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
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailDeletePosterDialogFragment
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.TodoPushAlarmDialogPlusAdapter
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
   MainActivity.onKeyBackPressedListener, CalendarDetailDeletePosterDialogFragment.OnDialogDismissedListener {

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_list_page
    override val viewModel: CalendarListViewModel by viewModel()

    lateinit private var favoriteCancelDialogFragment : CalendarDetailDeletePosterDialogFragment

    private var favoriteDeleteClick = false
    lateinit var favoriteDialog : DialogPlus
    lateinit var favoriteDialogAdapter : TodoPushAlarmDialogPlusAdapter

    var calendarListPageRecyclerViewAdapter = CalendarListPageRecyclerViewAdapter()

    private var dataList: ArrayList<Schedule> = arrayListOf()

    private var alarmCheckList = arrayListOf<Boolean>(true, false, false, false, false)
    private var isFavorite = false
    private var filterClick = false
    var curPosition = 0

    private var bookmarkPosteridx = 0

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

    override fun onDialogDismissed(isDeleted:Boolean) {
        if(isDeleted) {
            if (favoriteDeleteClick) {
                viewModel.unBookmarkWithAlarm(bookmarkPosteridx)
                favoriteDialog.dismiss()
            }
        }
        favoriteDeleteClick = false
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
                viewModel.getPushAlarm(posterIdx)
                bookmarkPosteridx = posterIdx
                viewModel.pushAlarmList.observe(this@CalendarListPageFragment, Observer {
                    Log.e("pushAlarmList", it.toString())
                    showBookmarkDialog(posterIdx, isFavorite,dday, it)
                })

            }

            override fun onSelectorClicked(posterIdx: Int, posterName:String, isSelected: Boolean) {}
        }

    private fun showBookmarkDialog(posterIdx: Int, isFavorite: Int, dday : Int, pushAlarmList : ArrayList<Int>){

        // default 값 설정 필요
        if(isFavorite == 0) {

            if(dday != 0 && dday != 1){
                for(i in 0 until alarmCheckList.size){
                    alarmCheckList[i] = i == 2
                }
            }

        }else{
            if(pushAlarmList.contains(0)) alarmCheckList[1] = true
            if(pushAlarmList.contains(1)) alarmCheckList[2] = true
            if(pushAlarmList.contains(3)) alarmCheckList[3] = true
            if(pushAlarmList.contains(7)) alarmCheckList[4] = true

            for(i in 1 until alarmCheckList.size){
                if(alarmCheckList[i]) alarmCheckList[0] = false
            }
        }

        favoriteDialogAdapter = TodoPushAlarmDialogPlusAdapter(activity!!, dday.toString(), alarmCheckList)
        favoriteDialogAdapter.setItemClickListener(OnBookmarkItemClickListener)
        val builder =  DialogPlus.newDialog(activity!!)

        val holder = GridHolder(1)

        builder.apply {

            setContentHolder(holder)
            setHeader(R.layout.dialog_fragment_poster_detail_bookmark_header)
            setFooter(R.layout.dialog_fragment_poster_detail_bookmark_footer)
            setCancelable(false)
            setGravity(Gravity.BOTTOM)

            setOnClickListener { dialog, view ->

                // 취소, 확인
                if (view.id == R.id.dialog_frag_poster_detail_bookmark_cancel) {
                    if(isFavorite == 1) {
                        favoriteDeleteClick = true

                        favoriteCancelDialogFragment = CalendarDetailDeletePosterDialogFragment()
                        favoriteCancelDialogFragment.setOnDialogDismissedListener(this@CalendarListPageFragment)
                        favoriteCancelDialogFragment.setTextView("즐겨찾기를 취소하시겠어요?\n즐겨찾기 취소 시 알림도 취소됩니다.")
                        favoriteCancelDialogFragment.show(
                            childFragmentManager,
                            "poster delete dialog"
                        )
                    }else{
                        dialog.dismiss()
                    }

                }else if(view.id == R.id.dialog_frag_poster_detail_bookmark_ok) {

                    var ddayList = ""
                    var mapper = arrayListOf(0, 1, 3, 7)

                    var isAdded = false
                    for(i in 1 until alarmCheckList.size){
                        if(alarmCheckList[i]){
                            ddayList += mapper[i-1]
                            ddayList += ", "
                            isAdded = true
                        }
                    }

                    if(isAdded) ddayList = ddayList.substring(0, ddayList.length - 2)

                    viewModel.bookmarkWithAlarm(posterIdx, ddayList)
                    dialog.dismiss()
                }


            }

            setAdapter(favoriteDialogAdapter)
            setOverlayBackgroundResource(R.color.dialog_background)
            setContentBackgroundResource(R.drawable.header_dialog_plus_radius)

            val horizontalDpValue = 40
            val topDpValue = 32
            val bottomDpValue = 32
            val d = resources.displayMetrics.density
            val horizontalMargin = (horizontalDpValue * d).toInt()
            val topMargin = (topDpValue * d).toInt()
            val bottomMargin = (bottomDpValue * d).toInt()

            setPadding(horizontalMargin, 0, horizontalMargin, 0)

        }

        favoriteDialog = builder.create()
        favoriteDialog.show()


    }

    private val OnBookmarkItemClickListener
            = object : TodoPushAlarmDialogPlusAdapter.OnItemClickListener {

        override fun onItemClick(position: Int) {
            if(position != 0){
                alarmCheckList[position] = !alarmCheckList[position]
                alarmCheckList[0] = false
            }else{

                alarmCheckList[0] = true
                for(i in 1..4){
                    alarmCheckList[i] = false
                }
            }

            var isAllFalse = true
            for(i in 1 until alarmCheckList.size){
                if(alarmCheckList[i]){
                    isAllFalse = false
                    break
                }
            }

            if(isAllFalse) alarmCheckList[0] = true
            favoriteDialogAdapter.replace(alarmCheckList)
        }
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