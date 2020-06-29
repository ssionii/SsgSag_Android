package com.icoo.ssgsag_android.ui.main.calendar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.BuildConfig
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.FragmentCalendarBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarPage.grid.CalendarGridFragment
import com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list.CalendarListDeleteActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list.CalendarListFragment
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.verticalMargin
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>(){

    override val layoutResID: Int
        get() = R.layout.fragment_calendar
    override val viewModel: CalendarViewModel by viewModel()

    lateinit var calendarGridFragment: CalendarGridFragment
    lateinit var calendarListFragment: CalendarListFragment

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && (activity as MainActivity).isSsgSaged) {
            viewModel.getAllCalendar()
            (activity as MainActivity).isSsgSaged = false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel
        
        calendarGridFragment = CalendarGridFragment()
        replaceFragment(calendarGridFragment)
        calendarListFragment = CalendarListFragment()
        replaceFragment(calendarListFragment)

        setButton()
        if(!SharedPreferenceController.getCalendarCoachMark(activity!!))
            setCoachMark()

    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllCalendar()
    }


    private fun setButton() {

        viewDataBinding.fragCalIvDrawer.setSafeOnClickListener {
            view!!.context.startActivity<MyPageActivity>()
            (view!!.context as Activity).overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_not_move
            )
        }

        // 캘린더로 보기, 리스트로 보기 toggle
        viewDataBinding.fragCalendarCvToggle.setOnClickListener {
            if(viewDataBinding.fragCalendarTvToggle.text == "캘린더로 보기"){
                replaceFragment(calendarGridFragment)
                viewDataBinding.fragCalendarTvToggle.text = "리스트로 보기"

                viewDataBinding.fragCalTvDay.visibility = VISIBLE
                viewDataBinding.fragCalTvHeader.visibility = GONE

            }else{
                replaceFragment(calendarListFragment)
                viewDataBinding.fragCalendarTvToggle.text = "캘린더로 보기"

                viewDataBinding.fragCalTvDay.visibility = GONE
                viewDataBinding.fragCalTvHeader.visibility = VISIBLE
            }
        }

        viewDataBinding.fragCalIvModify.setSafeOnClickListener {

            val popup = PopupMenu(context!!, it)
            val inflater = popup.menuInflater
            val menu = popup.menu

            if(calendarListFragment.isHidden) {
                inflater.inflate(R.menu.menu_cal_grid, menu)

                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.menu_cal_list_share -> {
                                // 현재 화면 스크린샷
                                var container: View = viewDataBinding.fragCalClContainer
                                container.buildDrawingCache()
                                var captureView: Bitmap = container.getDrawingCache()

                                // 화면 저장
                                var storeAddress: String =
                                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/com.icoo.ssgsag_android/files" + "/capture.jpeg"
                                lateinit var fos: FileOutputStream

                                try {
                                    fos = FileOutputStream(storeAddress)
                                    captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }

                                // Intent로 공유
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.setType("image/*")
                                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                shareIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                                shareIntent.putExtra(
                                    Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                                        context!!,
                                        BuildConfig.APPLICATION_ID, File(storeAddress)
                                    )
                                )
                                shareIntent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "슥삭 다운로드 바로가기\nhttps://ssgsag.page.link/install"
                                )
                                startActivity(Intent.createChooser(shareIntent, "캘린더 공유"))
                            }
                        }
                        return false
                    }
                })

            }else{
                inflater.inflate(R.menu.menu_cal_list, menu)

                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.menu_cal_list_delete -> {
                                val intent = Intent(activity, CalendarListDeleteActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        return false
                    }
                })
            }
            popup.show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            if (fragment.isAdded) {
                when(fragment){
                    is CalendarListFragment -> {
                        fragment.viewModel.getAllCalendar()
                        fragment.viewModel.getFavoriteSchedule()
                    }
                    is CalendarGridFragment -> {
                        fragment.viewModel.getAllCalendar()
                        fragment.viewModel.getFavoriteSchedule()
                    }
                }
                show(fragment)
            } else {
                add(R.id.frag_calendar_fl_page_container, fragment)
            }

            childFragmentManager.fragments.forEach {
                if (it != fragment && it.isAdded) {
                    hide(it)
                }
            }
        }.commit()
    }

    private fun setCoachMark(){

        viewDataBinding.fragCalendarClCoachmarkContainer.visibility = VISIBLE

        SharedPreferenceController.setCalendarCoachMark(activity!!, true)

        val d = resources.displayMetrics.density
        val widthPx = MainActivity.GetWidth.windowWidth / 8 * 3

        val rightDpValue = widthPx / d - 84
        val bottomDpValue = 13

        val rightMargin = (rightDpValue * d).toInt()
        val bottomMargin = (bottomDpValue * d).toInt()

        (viewDataBinding.fragCalendarClCoachmark.layoutParams as ConstraintLayout.LayoutParams).apply{
            marginEnd = rightMargin
            verticalMargin = bottomMargin
        }

        viewDataBinding.fragCalendarClCoachmarkContainer.setOnTouchListener( object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })

        viewDataBinding.fragCalendarClCoachmark.setOnClickListener {
            viewDataBinding.fragCalendarClCoachmarkContainer.visibility = GONE

        }

    }
}