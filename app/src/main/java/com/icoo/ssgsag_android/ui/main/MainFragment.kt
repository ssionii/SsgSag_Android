package com.icoo.ssgsag_android.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.color.MaterialColors.getColor
import com.google.android.material.tabs.TabLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.FragmentMainBinding
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersFragment
import com.icoo.ssgsag_android.ui.main.allPosters.search.SearchActivity
import com.icoo.ssgsag_android.ui.main.calendar.CalendarFragment
import com.icoo.ssgsag_android.ui.main.feed.FeedFragment
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.ui.main.review.main.ReviewMainFragment
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagFragment
import com.icoo.ssgsag_android.ui.main.ssgSag.filter.SsgSagFilterActivity
import com.icoo.ssgsag_android.ui.main.subscribe.SubscribeActivity
import com.icoo.ssgsag_android.ui.signUp.SignupActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonSwipeViewPager
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColor
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_main
    override val viewModel: MainViewModel by viewModel()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setVp()
        setTabLayout()
        setButton()
    }

    private fun setVp(){

        viewDataBinding.fragMainVp.run {
            adapter = BasePagerAdapter(fragmentManager!!).apply {
                addFragment(AllPostersFragment())
                addFragment(SsgSagFragment())
                isSaveEnabled = false
            }
            currentItem = 1
            offscreenPageLimit = 1

            addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    when(position){
                        0 ->{
                            viewDataBinding.fragMainIvFilter.visibility = GONE
                            viewDataBinding.fragMainIvSearch.visibility = VISIBLE
                        }
                        else ->{
                            viewDataBinding.fragMainIvFilter.visibility = VISIBLE
                            viewDataBinding.fragMainIvSearch.visibility = GONE
                        }
                    }
                }
            })
        }

    }

    private fun setButton(){

        // 마이페이지
        viewDataBinding.fragMainIvMypage.setSafeOnClickListener {

            view!!.context.startActivity<MyPageActivity>()
            (view!!.context as Activity).overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_not_move
            )

        }

        viewDataBinding.fragMainIvFilter.setSafeOnClickListener {
            startActivity<SsgSagFilterActivity>()
        }


        viewDataBinding.fragMainIvSearch.setSafeOnClickListener {
            val intent = Intent(activity!!, SearchActivity::class.java)
            intent.putExtra("from", "main")
            startActivity(intent)
        }
    }

    private fun setTabLayout() {
        //TabLayout
        val bottomNavigationLayout: View =
            LayoutInflater.from(activity!!).inflate(R.layout.navigation_ssgsag, null)

        viewDataBinding.fragMainTl.run {
            setupWithViewPager(viewDataBinding.fragMainVp)
            getTabAt(0)!!.customView =
                bottomNavigationLayout.findViewById(R.id.navi_ssgsag_cl_all) as ConstraintLayout
            getTabAt(1)!!.customView =
                bottomNavigationLayout.findViewById(R.id.navi_ssgsag_cl_ssgsag) as ConstraintLayout
            setTabRippleColor(null)
        }
    }
}