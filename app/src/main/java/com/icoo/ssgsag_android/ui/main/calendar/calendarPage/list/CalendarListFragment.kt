package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.tabs.TabLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.databinding.FragmentCalendarListBinding
import com.icoo.ssgsag_android.ui.main.calendar.CalendarViewModel
import com.icoo.ssgsag_android.ui.main.review.ReviewListFragment
import kotlinx.android.synthetic.main.activity_walkthrough.*
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

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.isFavorite.value = tab!!.position != 0
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

            })
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