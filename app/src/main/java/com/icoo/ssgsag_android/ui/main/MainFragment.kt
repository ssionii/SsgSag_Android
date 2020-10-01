package com.icoo.ssgsag_android.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.databinding.FragmentMainBinding
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersFragment
import com.icoo.ssgsag_android.ui.main.allPosters.search.SearchActivity
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagFragment
import com.icoo.ssgsag_android.ui.main.ssgSag.filter.SsgSagFilterActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.support.v4.startActivity
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
            adapter = BasePagerAdapter(childFragmentManager).apply {
                val all = AllPostersFragment.newInstance()
                val ssgsag = SsgSagFragment.newInstance()

                addFragment(all)
                addFragment(ssgsag)
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