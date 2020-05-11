package com.icoo.ssgsag_android.ui.main.allPosters

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.Dimension.DP
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginRight
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.databinding.FragmentAllPosterBinding
import com.icoo.ssgsag_android.databinding.ItemCalSortBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.MainFragment
import com.icoo.ssgsag_android.ui.main.allPosters.category.AllCategoryFragment
import com.icoo.ssgsag_android.ui.main.calendar.CalendarFragment
import com.icoo.ssgsag_android.ui.main.feed.FeedFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllPostersFragment : BaseFragment<FragmentAllPosterBinding, AllPostersViewModel>(),
    CardViewPagerAdapter.OnItemClickListener{

    override val layoutResID: Int
        get() = R.layout.fragment_all_poster
    override val viewModel: AllPostersViewModel by viewModel()

    lateinit var categoryFragment: AllCategoryFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setViewPager()
        navigator()
    }

    override fun onResume() {
        super.onResume()

        setButton()

    }

    private fun setButton(){

        viewDataBinding.fragAllPosterLlClubMore.setSafeOnClickListener {
            categoryFragment = AllCategoryFragment()
            categoryFragment.setCategory(2)

            fragmentManager!!.beginTransaction().add(R.id.frag_main_fl_container, categoryFragment).commit()
            fragmentManager!!.beginTransaction().show(categoryFragment).commit()
        }

        viewDataBinding.fragAllPosterLlActMore.setSafeOnClickListener {
            categoryFragment = AllCategoryFragment()
            categoryFragment.setCategory(1)

            fragmentManager!!.beginTransaction().add(R.id.frag_main_fl_container, categoryFragment).commit()
            fragmentManager!!.beginTransaction().show(categoryFragment).commit()
        }

        viewDataBinding.fragAllPosterLlContestMore.setSafeOnClickListener {
            categoryFragment = AllCategoryFragment()
            categoryFragment.setCategory(0)

            fragmentManager!!.beginTransaction().add(R.id.frag_main_fl_container, categoryFragment).commit()
            fragmentManager!!.beginTransaction().show(categoryFragment).commit()
        }

        viewDataBinding.fragAllPosterLlInternMore.setSafeOnClickListener {
            categoryFragment = AllCategoryFragment()
            categoryFragment.setCategory(4)

            fragmentManager!!.beginTransaction().add(R.id.frag_main_fl_container, categoryFragment).commit()
            fragmentManager!!.beginTransaction().show(categoryFragment).commit()
        }

        viewDataBinding.fragAllPosterLlEducationMore.setSafeOnClickListener {
            categoryFragment = AllCategoryFragment()
            categoryFragment.setCategory(7)

            fragmentManager!!.beginTransaction().add(R.id.frag_main_fl_container, categoryFragment).commit()
            fragmentManager!!.beginTransaction().show(categoryFragment).commit()
        }


        viewDataBinding.fragAllPosterLlEtcMore.setSafeOnClickListener {
            categoryFragment = AllCategoryFragment()
            categoryFragment.setCategory(5)

            fragmentManager!!.beginTransaction().add(R.id.frag_main_fl_container, categoryFragment).commit()
            fragmentManager!!.beginTransaction().show(categoryFragment).commit()
        }


    }

    private fun setViewPager(){

        val d = resources.displayMetrics.density

        // 화면 전체 사이즈
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x / d).toInt()

        val contentDpValue = 180
        val leftDpValue = 26
        val middleDpValue = 15
        val rightDpValue = width - (leftDpValue + contentDpValue + middleDpValue)

        val leftMargin = (leftDpValue * d).toInt()
        val rightMargin = (rightDpValue * d).toInt()
        val middleMargin = (middleDpValue * d).toInt()

        viewModel.clubPosterList.observe(this, Observer {value ->

            var cardViewPagerAdapter = CardViewPagerAdapter(activity!!, value)
            cardViewPagerAdapter.setOnItemClickListener(this)

            if(value.size > 0) {
                viewDataBinding.fragAllPosterVpClub.run {

                    clipToPadding = false
                    setPadding(leftMargin, 20, rightMargin, 30)
                    pageMargin = middleMargin


                    adapter = cardViewPagerAdapter

                }
            }else {
                viewDataBinding.fragAllPosterLlClubContainer.visibility = GONE
                viewDataBinding.fragAllPosterVpClub.visibility = GONE
            }
        })


        viewModel.actPosterList.observe(this, Observer {value ->

            var cardViewPagerAdapter = CardViewPagerAdapter(activity!!, value)
            cardViewPagerAdapter.setOnItemClickListener(this)

            if(value.size > 0) {
                viewDataBinding.fragAllPosterVpAct.run {

                    clipToPadding = false
                    setPadding(leftMargin, 20, rightMargin, 30)
                    pageMargin = middleMargin


                    adapter = cardViewPagerAdapter

                }
            }else {
                viewDataBinding.fragAllPosterLlActContainer.visibility = GONE
                viewDataBinding.fragAllPosterVpAct.visibility = GONE
            }
        })

        viewModel.contestPosterList.observe(this, Observer {value ->

            var cardViewPagerAdapter = CardViewPagerAdapter(activity!!, value)
            cardViewPagerAdapter.setOnItemClickListener(this)

            if(value.size > 0) {
                viewDataBinding.fragAllPosterVpContest.run {
                    clipToPadding = false
                    setPadding(leftMargin, 20, rightMargin, 30)
                    pageMargin = middleMargin

                    adapter = cardViewPagerAdapter

                }
            }else {
                viewDataBinding.fragAllPosterLlContestContainer.visibility = GONE
                viewDataBinding.fragAllPosterVpContest.visibility = GONE
            }
        })

        viewModel.internPosterList.observe(this, Observer {value ->
            var cardViewPagerAdapter = CardViewPagerAdapter(activity!!, value)
            cardViewPagerAdapter.setOnItemClickListener(this)

            if(value.size > 0) {
                viewDataBinding.fragAllPosterVpIntern.run {
                    clipToPadding = false
                    setPadding(leftMargin, 20, rightMargin, 30)
                    pageMargin = middleMargin

                    adapter = cardViewPagerAdapter

                }
            }else {
                viewDataBinding.fragAllPosterLlInternContainer.visibility = GONE
                viewDataBinding.fragAllPosterVpIntern.visibility = GONE
            }
        })
        viewModel.educationPosterList.observe(this, Observer {value ->
            var cardViewPagerAdapter = CardViewPagerAdapter(activity!!, value)
            cardViewPagerAdapter.setOnItemClickListener(this)

            if(value.size > 0) {
                viewDataBinding.fragAllPosterVpEducation.run {
                    clipToPadding = false
                    setPadding(leftMargin, 20, rightMargin, 30)
                    pageMargin = middleMargin

                    adapter = cardViewPagerAdapter

                }
            }else {
                viewDataBinding.fragAllPosterLlEducationContainer.visibility = GONE
                viewDataBinding.fragAllPosterVpEducation.visibility = GONE
            }
        })

        viewModel.etcPosterList.observe(this, Observer {value ->
            var cardViewPagerAdapter = CardViewPagerAdapter(activity!!, value)
            cardViewPagerAdapter.setOnItemClickListener(this)
            if(value.size > 0) {
                viewDataBinding.fragAllPosterLlEtcContainer.visibility = VISIBLE
                viewDataBinding.fragAllPosterVpEtc.run {
                    clipToPadding = false
                    setPadding(leftMargin, 20, rightMargin, 30)
                    pageMargin = middleMargin

                    adapter = cardViewPagerAdapter

                }
            }else {
                viewDataBinding.fragAllPosterLlEtcContainer.visibility = GONE
                viewDataBinding.fragAllPosterVpEtc.visibility = GONE
            }
        })

    }

    override fun onItemClick(posterIdx: Int) {
        viewModel.navigate(posterIdx)
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(activity, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            view!!.context.startActivity(intent)

        })
    }
}