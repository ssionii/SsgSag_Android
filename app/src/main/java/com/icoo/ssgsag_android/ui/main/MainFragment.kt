package com.icoo.ssgsag_android.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast

import androidx.fragment.app.FragmentTransaction
import com.google.android.material.color.MaterialColors.getColor
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.FragmentMainBinding
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersFragment
import com.icoo.ssgsag_android.ui.main.allPosters.search.SearchActivity
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagFragment
import com.icoo.ssgsag_android.ui.main.ssgSag.filter.SsgSagFilterActivity
import com.icoo.ssgsag_android.ui.main.subscribe.SubscribeActivity
import com.icoo.ssgsag_android.ui.signUp.SignupActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
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

    object mainFragment {
        lateinit var ssgSagFragment: SsgSagFragment
        lateinit var allPostersFragment : AllPostersFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        changeFragment("ssgsag")
        viewModel.setIsSsgSagFragment(true)

        setButton()
    }

    private fun setButton(){

        // 전체
        viewDataBinding.fragMainTvAll.setSafeOnClickListener {
            changeFragment("all")
            viewModel.setIsSsgSagFragment(false)

        }

        // 추천
        viewDataBinding.fragMainTvRecommend.setSafeOnClickListener {
            changeFragment("ssgsag")
            viewModel.setIsSsgSagFragment(true)
        }

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

    private fun changeFragment(f: String){
        when(f){
            "ssgsag" -> {
                if(fragmentManager!!.findFragmentByTag(f) != null)
                    fragmentManager!!.beginTransaction().show(fragmentManager!!.findFragmentByTag(f)!!).commit()
                else {
                    mainFragment.ssgSagFragment = SsgSagFragment()
                    fragmentManager!!.beginTransaction().add(R.id.frag_main_fl, mainFragment.ssgSagFragment , f).commit()
                }
                if(fragmentManager!!.findFragmentByTag("all") != null)
                    fragmentManager!!.beginTransaction().hide(fragmentManager!!.findFragmentByTag("all")!!).commit()

                viewDataBinding.fragMainIvSearch.visibility = GONE
            }
            "all" ->{
                if(fragmentManager!!.findFragmentByTag(f) != null)
                    fragmentManager!!.beginTransaction().show(fragmentManager!!.findFragmentByTag(f)!!).commit()
                else {
                    mainFragment.allPostersFragment = AllPostersFragment()
                    fragmentManager!!.beginTransaction().add(R.id.frag_main_fl, mainFragment.allPostersFragment, f).commit()
                }
                if(fragmentManager!!.findFragmentByTag("ssgsag") != null)
                    fragmentManager!!.beginTransaction().hide(fragmentManager!!.findFragmentByTag("ssgsag")!!).commit()

                viewDataBinding.fragMainIvFilter.visibility = GONE
                viewDataBinding.fragMainIvSearch.visibility = VISIBLE
            }
        }
    }
}