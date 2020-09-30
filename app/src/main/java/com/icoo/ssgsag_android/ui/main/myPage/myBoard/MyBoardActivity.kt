package com.icoo.ssgsag_android.ui.main.myPage.myBoard

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.databinding.ActivityMyBoardBinding
import com.icoo.ssgsag_android.ui.main.MainFragment
import com.icoo.ssgsag_android.ui.main.calendar.CalendarFragment
import com.icoo.ssgsag_android.ui.main.community.CommunityFragment
import com.icoo.ssgsag_android.ui.main.myPage.MyPageFragment
import com.icoo.ssgsag_android.ui.main.myPage.MyPageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyBoardActivity : BaseActivity<ActivityMyBoardBinding, MyPageViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_my_board
    override val viewModel: MyPageViewModel by viewModel()

    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = intent.getStringExtra("type")

        if(type == "post"){
            viewDataBinding.actMyBoardToolbar.toolbarBackTvTitle.text = "내가 쓴 글/댓글"
            setPostVp()
            setPostTab()
        }else if(type == "bookmark"){
            viewDataBinding.actMyBoardToolbar.toolbarBackTvTitle.text = "스크랩한 글"
            setBookmarkVp()
            setBookmakrTl()
        }


        setButton()
    }


    private fun setPostVp(){
        val myComment = MyBoardPageFragment.newInstance(MyBoardPageFragment.MyBoardType.MY_POST)
        val myComment2 = MyBoardPageFragment.newInstance(MyBoardPageFragment.MyBoardType.MY_COMMENT)

        viewDataBinding.actMyBoardVp.run{
            adapter = BasePagerAdapter(supportFragmentManager).apply {

                addFragment(myComment)
                addFragment(myComment2)

            }
            currentItem = 0
            offscreenPageLimit = 1
        }
    }

    private fun setPostTab(){
        viewDataBinding.actMyBoardTl.run{
            setupWithViewPager(viewDataBinding.actMyBoardVp)

            getTabAt(0)!!.text = "내가 쓴 글"
            getTabAt(1)!!.text = "내가 쓴 댓글"

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

            })
        }
    }


    private fun setBookmarkVp(){
        val post = MyBoardPageFragment.newInstance(MyBoardPageFragment.MyBoardType.BOOKMARK_POST)
        val news = MyBoardPageFragment.newInstance(MyBoardPageFragment.MyBoardType.BOOKMARK_NEWS)

        viewDataBinding.actMyBoardVp.run{
            adapter = BasePagerAdapter(supportFragmentManager).apply {

                addFragment(post)
                addFragment(news)
            }
            currentItem = 0
            offscreenPageLimit = 1
        }
    }

    private fun setBookmakrTl(){
        viewDataBinding.actMyBoardTl.run{
            setupWithViewPager(viewDataBinding.actMyBoardVp)

            getTabAt(0)!!.text = "게시판"
            getTabAt(1)!!.text = "추천정보"

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

            })
        }
    }

    private fun setButton(){
        viewDataBinding.actMyBoardToolbar.toolbarBackClBack.setOnClickListener {
            finish()
        }
    }
}