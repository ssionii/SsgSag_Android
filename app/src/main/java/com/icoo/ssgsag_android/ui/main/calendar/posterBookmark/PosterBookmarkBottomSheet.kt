package com.icoo.ssgsag_android.ui.main.calendar.posterBookmark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.poster.Poster
import com.icoo.ssgsag_android.databinding.ActivityMainBlockBinding
import com.icoo.ssgsag_android.databinding.ActivityRegisterUnivBinding
import com.icoo.ssgsag_android.databinding.BottomSheetPosterBookmarkBinding
import com.icoo.ssgsag_android.ui.main.block.MainBlockActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailDeletePosterDialogFragment
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

// 필요한 거
// dday,이미 선택되어 있는거, posterIdx
class PosterBookmarkBottomSheet (
    private val posterIdx : Int,
    private val dday : Int,
    private val isFavorite : Int,
    private val from : String,
    val buttonClick: (Int) -> Unit
) : BottomSheetDialogFragment()
    , CalendarDetailDeletePosterDialogFragment.OnDialogDismissedListener
{

    lateinit var viewDataBinding: BottomSheetPosterBookmarkBinding
    val viewModel: PosterBookmarkViewModel by viewModel()

    lateinit var rvAdapter: PosterBookmarkRecyclerViewAdapter
    private var alarmList = arrayListOf<PosterAlarmData>()

    lateinit private var deleteDialogFragment : CalendarDetailDeletePosterDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_poster_bookmark, container, false)
        viewDataBinding.lifecycleOwner = this

        setAlarmList()
        setButton()

        return viewDataBinding.root
    }

    override fun onDialogDismissed(isDeleted:Boolean) {
        if(isDeleted) {
            viewModel.unBookmarkWithAlarm(posterIdx)
            buttonClick(0)
            dismiss()

        }
    }

    private fun setAlarmList() {

        viewModel.getPushAlarm(posterIdx)

        viewModel.pushAlarmList.observe(viewLifecycleOwner, Observer {

            alarmList = arrayListOf(
                PosterAlarmData(-1, "알림 없음", false, false),
                PosterAlarmData(0, "마감 당일 알림(오전 10시)", false, false),
                PosterAlarmData(1, "마감 1일 전 알림", false, false),
                PosterAlarmData(3, "마감 3일 전 알림", false, false),
                PosterAlarmData(7, "마감 7일 전 알림", false, false)
            )

            // 지난 날짜에 대한 처리
            for (alarm in alarmList) {
                if (dday <= alarm.day) alarm.isPast = true
            }

            // 즐겨찾기를 해놓은 상태라면
            if(isFavorite == 1){
                if(it.size == 0){ // 알림 없음으로 선택해놓음
                    alarmList[0].isChecked = true
                }else {
                    for (alarm in alarmList){
                        if (it.contains(alarm.day)) alarm.isChecked = true
                    }
                }
            }else{ // 즐겨찾기를 지금 하려는 상태라면
                if(dday <= 1) {
                    alarmList[0].isChecked = true
                }else{
                    alarmList[2].isChecked = true
                }
            }

            setRv()
        })
    }

    private fun setRv(){

        rvAdapter = PosterBookmarkRecyclerViewAdapter()
        rvAdapter.apply {
            replaceAll(alarmList)
            notifyDataSetChanged()
            setOnAlarmItemClickListener(onAlarmItemClickListener)
        }

        viewDataBinding.bottomSheetPosterBookmarkRv.run{
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    val onAlarmItemClickListener
            = object : PosterBookmarkRecyclerViewAdapter.onAlarmItemClickListener{
        override fun onItemClicked(idx: Int) {

            // 알림 없음 누르면 모두 선택 해제
            if(idx == 0){
                alarmList[0].isChecked = true
                for(i in 1..4){
                    if (!alarmList[i].isPast){
                        alarmList[i].isChecked = false
                    }
                }
            }else {
                alarmList[idx].isChecked = !alarmList[idx].isChecked
                alarmList[0].isChecked = false

                var isAllUnChecked = true
                for(i in 1..4){
                    if (!alarmList[i].isPast && alarmList[i].isChecked){
                        isAllUnChecked = false
                        break
                    }
                }

                // 모두 다 비활성화 시키면 알람 없음 체크
                if(isAllUnChecked){
                    alarmList[0].isChecked = true
                    for(i in 1..4){
                        if (!alarmList[i].isPast){
                            alarmList[i].isChecked = false
                        }
                    }
                }
            }
            rvAdapter.run{
                replaceAll(alarmList)
                notifyDataSetChanged()
            }
        }
    }

    private fun setButton(){
        viewDataBinding.bottomSheetPosterBookmarkCvCancel.setSafeOnClickListener {
            if(isFavorite == 0){
                if(from == "detail"){
                    dismiss()
                }else{
                    viewModel.unBookmarkWithAlarm(posterIdx)
                    buttonClick(2)
                    dismiss()
                }
            } else{
                deleteDialogFragment = CalendarDetailDeletePosterDialogFragment()
                deleteDialogFragment.setOnDialogDismissedListener(this)
                deleteDialogFragment.show(childFragmentManager, "poster delete dialog")
            }

        }

        viewDataBinding.bottomSheetPosterBookmarkCvConfirm.setSafeOnClickListener {

            var ddayList = ""

            var isAdded = false
            for(i in 1 until alarmList.size){
                if(alarmList[i].isChecked){
                    ddayList += alarmList[i].day
                    ddayList += ", "
                    isAdded = true
                }
            }

            if(isAdded) ddayList = ddayList.substring(0, ddayList.length - 2)

            viewModel.bookmarkWithAlarm(posterIdx, ddayList)
            buttonClick(1)
            dismiss()
        }
    }

    companion object {
        const val REQUEST_CODE = 2001
        const val FEATURES_KEY = "features"
    }
}

