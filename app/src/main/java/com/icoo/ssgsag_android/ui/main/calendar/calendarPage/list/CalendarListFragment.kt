package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.databinding.FragmentCalendarListBinding
import com.icoo.ssgsag_android.ui.main.calendar.CalendarViewModel
import com.icoo.ssgsag_android.ui.main.review.ReviewListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarListFragment : BaseFragment<FragmentCalendarListBinding, CalendarViewModel>(){

    override val layoutResID: Int
        get() = R.layout.fragment_calendar_list
    override val viewModel: CalendarViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel

        setVp()
        setTabLayout()
    }

    private fun setTabLayout(){
        viewDataBinding.fragCalListTl.run{
            setupWithViewPager(viewDataBinding.fragCalListVp)
            getTabAt(0)!!.text = "전체"
            getTabAt(1)!!.text = "즐겨찾기"
            setTabRippleColor(null)

        }
    }

    private fun setVp(){
        val calendarAllListPageFragment = CalendarListPageFragment.newInstance(false)
        val calendarFavoriteListPageFragment = CalendarListPageFragment.newInstance(true)

        viewDataBinding.fragCalListVp.run {
            adapter = BasePagerAdapter(childFragmentManager).apply {
                addFragment(calendarAllListPageFragment)
                addFragment(calendarFavoriteListPageFragment)
            }
            currentItem = 0
            offscreenPageLimit = 1
        }
    }
}