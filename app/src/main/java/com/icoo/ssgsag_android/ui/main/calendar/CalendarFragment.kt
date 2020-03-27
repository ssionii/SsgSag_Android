package com.icoo.ssgsag_android.ui.main.calendar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.BuildConfig
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.databinding.FragmentCalendarBinding
import com.icoo.ssgsag_android.databinding.ItemCalSortBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarPage.CalendarListPageFragment
import com.icoo.ssgsag_android.ui.main.calendar.calendarPage.CalendarListPageRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.ui.splash.SplashActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>(),
    BaseRecyclerViewAdapter.OnItemClickListener {

    override val layoutResID: Int
        get() = R.layout.fragment_calendar
    override val viewModel: CalendarViewModel by viewModel()

    var position = COUNT_PAGE

    lateinit var calendarPagerAdapter: CalendarPagerAdapter
    var calendarListPageFragment = CalendarListPageFragment()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && (activity as MainActivity).isSsgSaged) {
            //setCalendarViewPager()
            viewModel.getAllCalendar()
            (activity as MainActivity).isSsgSaged = false
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        //ui
        setCalendarViewPager()
        setSortTab()
        setFirstButton()
        setButton()
        setHeaderDate()

        viewModel.categorySort.observe(this, androidx.lifecycle.Observer {
            viewDataBinding.fragCalendarRvSort.adapter!!.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCalendar()
    }


    private fun setSortTab() {
        viewDataBinding.fragCalendarRvSort.apply {
            adapter =
                object : BaseRecyclerViewAdapter<Category, ItemCalSortBinding>() {
                    override val layoutResID: Int
                        get() = R.layout.item_cal_sort
                    override val bindingVariableId: Int
                        get() = BR.category
                    override val listener: OnItemClickListener?
                        get() = this@CalendarFragment
                }
        }
    }

    override fun onItemClicked(item: Any?, position: Int?){
        viewModel.checkCate((item as Category).categoryIdx)
    }


    private fun setCalendarViewPager() {
        calendarPagerAdapter = CalendarPagerAdapter(childFragmentManager).apply { setNumOfMonth(COUNT_PAGE) }

        viewDataBinding.fragCalTvDay.text = calendarPagerAdapter.getMonthDisplayed(position)

        viewDataBinding.fragCalendarVpPage.run {
            adapter = calendarPagerAdapter
            currentItem = COUNT_PAGE
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {

                    viewDataBinding.fragCalTvDay.text =
                        calendarPagerAdapter.getMonthDisplayed(position)

                    if (position == 0) {
                        calendarPagerAdapter.addPrev()
                        setCurrentItem(COUNT_PAGE, false)
                    } else if (position == calendarPagerAdapter.count - 1) {
                        calendarPagerAdapter.addNext()
                        setCurrentItem(calendarPagerAdapter.count - (COUNT_PAGE + 1), false)
                    }
                    this@CalendarFragment.position = position
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
    }

    private fun setFirstButton(){
        viewDataBinding.fragCalIvCalendar.visibility = INVISIBLE
        viewDataBinding.fragCalIvList.visibility = VISIBLE
    }

    private fun setButton() {

        val scope = CoroutineScope(Dispatchers.Main)
        var tempDay : String = ""

        viewDataBinding.fragCalIvDrawer.setSafeOnClickListener {
            view!!.context.startActivity<MyPageActivity>()
            (view!!.context as Activity).overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_not_move
            )
        }


        viewDataBinding.fragCalIvList.setSafeOnClickListener {

            scope.launch {
                fragmentManager!!.beginTransaction().replace(R.id.frag_calendar_rl_page_container,calendarListPageFragment).commit()
            }

            //tempDay = viewDataBinding.fragCalTvDay.text.toString()

            viewDataBinding.fragCalendarVpPage.visibility = GONE
            viewDataBinding.fragCalendarLlDayOfWeek.visibility = GONE
            viewDataBinding.fragCalIvCalendar.visibility = VISIBLE
            viewDataBinding.fragCalIvList.visibility = INVISIBLE
        }

        viewDataBinding.fragCalIvCalendar.setSafeOnClickListener {
            scope.launch {
                fragmentManager!!.beginTransaction().remove(calendarListPageFragment).commit()
            }
                viewDataBinding.fragCalendarVpPage.visibility = VISIBLE
                viewDataBinding.fragCalendarLlDayOfWeek.visibility = VISIBLE
                viewDataBinding.fragCalendarVpPage.bringToFront()
                viewDataBinding.fragCalTvDay.text = calendarPagerAdapter.getMonthDisplayed(position)

                viewDataBinding.fragCalIvList.visibility = VISIBLE
                viewDataBinding.fragCalIvCalendar.visibility = INVISIBLE

        }

        viewDataBinding.fragCalIvDeleteBack.setSafeOnClickListener {
            viewModel.setDeleteType(0)
        }

        viewDataBinding.fragCalTvDelete.setSafeOnClickListener {
            viewModel.setDeleteType(3)
        }

        viewDataBinding.fragCalIvModify.setSafeOnClickListener {

            val popup = PopupMenu(context!!, it)
            val inflater = popup.menuInflater
            val menu = popup.menu

            inflater.inflate(R.menu.menu_cal_list, menu)

            if(viewDataBinding.fragCalIvList.visibility == VISIBLE) {
                menu.findItem(R.id.menu_cal_list_delete).setVisible(false)
                menu.findItem(R.id.menu_cal_list_share).setVisible(true)
            }else{
                menu.findItem(R.id.menu_cal_list_delete).setVisible(true)
                menu.findItem(R.id.menu_cal_list_share).setVisible(false)
            }

            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.menu_cal_list_delete -> {
                            viewModel.setDeleteType(1)
                        }
                        R.id.menu_cal_list_share ->{
                            // 현재 화면 스크린샷
                            var container: View = viewDataBinding.fragCalClContainer
                            container.buildDrawingCache()
                            var captureView : Bitmap = container.getDrawingCache()

                            // 화면 저장
                            var storeAddress : String = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/com.icoo.ssgsag_android/files" + "/capture.jpeg"
                            lateinit var fos : FileOutputStream

                            try{
                                fos = FileOutputStream(storeAddress)
                                captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            }catch (e : FileNotFoundException){
                                e.printStackTrace()
                            }

                            // Intent로 공유
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.setType("image/*")
                            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            shareIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context!!,
                                BuildConfig.APPLICATION_ID, File(storeAddress)))
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "슥삭 다운로드 바로가기\nhttps://ssgsag.page.link/install")
                            startActivity(Intent.createChooser(shareIntent, "캘린더 공유"))
                        }
                    }
                    return false
                }
            })
            popup.show()
        }

        viewModel.deleteType.observe(this@CalendarFragment, Observer {value ->
            when(value){
                0->{ // list, passive
                    viewDataBinding.fragCalIvDeleteBack.visibility = GONE
                    viewDataBinding.fragCalTvDelete.visibility = GONE
                    viewDataBinding.fragCalIvModify.visibility = VISIBLE
                    viewDataBinding.fragCalIvList.visibility = GONE
                    viewDataBinding.fragCalIvCalendar.visibility = VISIBLE
                }
                1->{ // list, 뒤로 버튼
                    viewDataBinding.fragCalIvDeleteBack.visibility = VISIBLE
                    viewDataBinding.fragCalTvDelete.visibility = GONE
                    viewDataBinding.fragCalIvModify.visibility = GONE
                    viewDataBinding.fragCalIvList.visibility = GONE
                    viewDataBinding.fragCalIvCalendar.visibility = GONE
                }
                2->{ // list, 삭제 버튼
                    viewDataBinding.fragCalIvDeleteBack.visibility = GONE
                    viewDataBinding.fragCalTvDelete.visibility = VISIBLE
                    viewDataBinding.fragCalIvModify.visibility = GONE
                    viewDataBinding.fragCalIvList.visibility = GONE
                }
                4->{ // calendar
                    viewDataBinding.fragCalIvModify.visibility = VISIBLE
                    viewDataBinding.fragCalIvList.visibility = VISIBLE
                    viewDataBinding.fragCalIvCalendar.visibility = GONE
                }
            }
        })
    }

    private fun setHeaderDate(){
        viewModel.listDate.observe(
            this@CalendarFragment, Observer { value ->
                value?.run{
                    var temp = this
                    if(value[6].equals('0')) {
                        temp = temp.substring(0,6) + temp.substring(7,9)
                    }
                    viewDataBinding.fragCalTvDay.text = temp
                }
        })
    }

    companion object {
        private val TAG = "CalendarFragment"
        private val COUNT_PAGE = 60

    }
}