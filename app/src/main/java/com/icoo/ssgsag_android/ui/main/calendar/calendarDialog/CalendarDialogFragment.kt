package com.icoo.ssgsag_android.ui.main.calendar.calendarDialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseDialogFragment
import com.icoo.ssgsag_android.databinding.DialogFragmentCalendarBinding
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage.CalendarDialogPageViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CalendarDialogFragment : BaseDialogFragment<DialogFragmentCalendarBinding, CalendarDialogPageViewModel>() {
    override val layoutResID: Int
        get() = R.layout.dialog_fragment_calendar
    override val viewModel: CalendarDialogPageViewModel by viewModel()

    object GetUpdate {
        var isUpdated = false
    }

    var position = 12

    var year = 0
    var month = 0
    var date = 0
    var showFavorite = false

    lateinit var calendarDialogPagerAdapter: CalendarDialogPagerAdapter
    lateinit var listener: OnDialogDismissedListener

    fun setOnDialogDismissedListener(listener: OnDialogDismissedListener) {
        this.listener = listener
    }

    interface OnDialogDismissedListener {
        fun onDialogDismissed(isUpdated: Boolean)
    }

    override fun dismiss() {
        listener.onDialogDismissed(GetUpdate.isUpdated)
        super.dismiss()

        GetUpdate.isUpdated = false

    }

    override fun onResume() {
        super.onResume()

        dialog!!.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(
                dialog: DialogInterface, keyCode: Int,
                event: KeyEvent
            ): Boolean {

                return if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //This is the filter
                    if (event.action != KeyEvent.ACTION_DOWN)
                        true
                    else {
                        listener.onDialogDismissed(GetUpdate.isUpdated)
                        dismiss()
                        true // pretend we've processed it
                    }
                } else
                    false // pass on to be processed as normal
            }
        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        //data 불러오기
        val mArgs = arguments
        val mDateValue = mArgs!!.getStringArrayList("Date")
        val mShowFavoriteValue = mArgs!!.getBoolean("showFavorite")

        year = mDateValue!![0].toInt()
        month = mDateValue[1].toInt() - 1
        date = mDateValue[2].toInt()
        showFavorite = mShowFavoriteValue

        //Schedule 설정
        val cal = Calendar.getInstance()
        val instanceCal = Calendar.getInstance()
        cal.set(year, month, date)

        var dayOfMonth = 0
        dayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)


        //영역 밖을 터치시 dialog dismiss
        viewDataBinding.dialogFragCalendarV.setSafeOnClickListener {
            if(dialog!!.isShowing)
                dismiss()

        }
        //PagerAdapter 설정
        calendarDialogPagerAdapter = CalendarDialogPagerAdapter(childFragmentManager).apply {
            setNumOfDay(cal)
            setShowFavorite(showFavorite)
        }

        //adapter 적용 및 position에 따른 addNext, addPrev 적용
        viewDataBinding.dialogFragCalendarVp.run {
            adapter = calendarDialogPagerAdapter
            if (date == 1) {
                month -= 1
                instanceCal.set(year, month, 1)
                dayOfMonth = instanceCal.getActualMaximum(Calendar.DAY_OF_MONTH)
                calendarDialogPagerAdapter.addPrev(instanceCal)
                setCurrentItem(dayOfMonth, false)
            } else if (date == dayOfMonth) {
                month += 1
                instanceCal.set(year, month, 1)
                calendarDialogPagerAdapter.addNext(instanceCal)
                setCurrentItem(dayOfMonth - 1, false)
            } else {
                currentItem = date - 1
            }

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }
                override fun onPageSelected(position: Int) {
                    if (position == 0) {
                        month -= 1
                        instanceCal.set(year, month, 1)
                        dayOfMonth = instanceCal.getActualMaximum(Calendar.DAY_OF_MONTH)
                        calendarDialogPagerAdapter.addPrev(instanceCal)
                        setCurrentItem(dayOfMonth, false)
                    } else if (position == calendarDialogPagerAdapter.count - 1) {
                        month += 1
                        instanceCal.set(year, month, 1)
                        dayOfMonth = instanceCal.getActualMaximum(Calendar.DAY_OF_MONTH)
                        calendarDialogPagerAdapter.addNext(cal)
                        setCurrentItem(calendarDialogPagerAdapter.count - (dayOfMonth + 1), false)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
    }


    companion object {
        private val TAG = "CalendarDialogFragment"
    }
}