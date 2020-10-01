package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.grid

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentCalendarGridBinding
import com.icoo.ssgsag_android.ui.main.calendar.CalendarPagerAdapter
import com.icoo.ssgsag_android.ui.main.calendar.CalendarViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarGridFragment :BaseFragment<FragmentCalendarGridBinding, CalendarViewModel>(){

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_grid
    override val viewModel: CalendarViewModel by viewModel()

    var position = COUNT_PAGE
    private lateinit var calendarPagerAdapter : CalendarPagerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel
        setCalendarViewPager()
        setTab()
    }


    private fun setCalendarViewPager() {

        calendarPagerAdapter = CalendarPagerAdapter(childFragmentManager).apply {
            setNumOfMonth(COUNT_PAGE)
        }

        setHeaderDate()

        viewDataBinding.fragCalGridVpPage.run {
            adapter = calendarPagerAdapter
            currentItem = COUNT_PAGE

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    viewModel.setHeaderDate(calendarPagerAdapter.getMonthDisplayed(position))

                    if (position == 0) {
                        calendarPagerAdapter.addPrev()
                        setCurrentItem(COUNT_PAGE, false)
                    } else if (position == calendarPagerAdapter.count - 1) {
                        calendarPagerAdapter.addNext()
                        setCurrentItem(calendarPagerAdapter.count - (COUNT_PAGE + 1), false)
                    }
                    this@CalendarGridFragment.position = position
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
    }

    fun setHeaderDate(){
        viewModel.setHeaderDate(calendarPagerAdapter.getMonthDisplayed(position))
    }

    private fun setTab(){
        viewDataBinding.fragCalGridLlAll.setSafeOnClickListener {
            viewModel.isFavoriteFromGrid.value = false

        }
        viewDataBinding.fragCalGridLlFavorite.setSafeOnClickListener {
            viewModel.isFavoriteFromGrid.value = true
        }
    }

    companion object {
        private val COUNT_PAGE = 60

    }
}