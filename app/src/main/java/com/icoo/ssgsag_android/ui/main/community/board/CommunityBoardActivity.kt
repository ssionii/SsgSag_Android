package com.icoo.ssgsag_android.ui.main.community.board

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.CounselBoardCategory
import com.icoo.ssgsag_android.databinding.ActivityCommunityBoardBinding
import com.icoo.ssgsag_android.databinding.ItemBoardBinding
import com.icoo.ssgsag_android.ui.main.MainFragment
import com.icoo.ssgsag_android.ui.main.calendar.CalendarFragment
import com.icoo.ssgsag_android.ui.main.community.CommunityFragment
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailActivity
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.write.BoardCounselPostWriteActivity
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.write.BoardTalkPostWriteActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityBoardActivity : BaseActivity<ActivityCommunityBoardBinding, CommunityBoardViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_community_board

    override val viewModel: CommunityBoardViewModel by viewModel()

    var communityBoardType = CommunityBoardType.TALK

    val counselBoardCategoryList = arrayListOf(
        CounselBoardCategory("전체", "ALL"),
        CounselBoardCategory("취업/진로", "CAREER"),
        CounselBoardCategory("대학생활", "UNIV"),
        CounselBoardCategory("기타", "THE_OTHERS")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        communityBoardType = intent.getIntExtra("type", CommunityBoardType.TALK)
        when(communityBoardType){
            CommunityBoardType.COUNSEL -> {
                viewDataBinding.actCommunityBoardTvTitle.text = this.resources.getString(R.string.counsel_title)
                setCounselVp()
                setTab()
            }
            CommunityBoardType.TALK -> {
                viewDataBinding.actCommunityBoardTvTitle.text = this.resources.getString(R.string.talk_title)
                viewDataBinding.actCommunityBoardTl.visibility = GONE
                viewModel.getTalkList()
            }
        }

        setTopBanner()
        setButton()

    }

    private fun setTopBanner(){

        val d = resources.displayMetrics.density

        // 화면 전체 사이즈
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x / d).toInt()

        viewDataBinding.actCommunityBoardIvTopBanner.layoutParams.height = (width * 0.32 * d).toInt()

    }

    private fun setCounselVp(){
        viewDataBinding.actCommunityBoardVp.apply{
            adapter = BasePagerAdapter(supportFragmentManager).apply {
                for(category in counselBoardCategoryList ){
                    addFragment(BoardListPageFragment.newInstance(category.category,communityBoardType))
                }
                isSaveEnabled = false
            }
            currentItem = 0
            offscreenPageLimit = 3
        }
    }

    private fun setTab(){

        viewDataBinding.actCommunityBoardTl.apply{
            setupWithViewPager(viewDataBinding.actCommunityBoardVp)
            for(i in 0 until counselBoardCategoryList.size){
                getTabAt(i)!!.text = counselBoardCategoryList[i].name
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

            })
        }
    }


    private fun setButton(){

        viewDataBinding.actCommunityBoardClBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actCommunityBoardCvWrite.setSafeOnClickListener {
            when(communityBoardType){
                CommunityBoardType.COUNSEL -> {
                    val intent = Intent(this, BoardCounselPostWriteActivity::class.java)
                    intent.putExtra("postWriteType", PostWriteType.WRITE)
                    startActivity(intent)
                }
                CommunityBoardType.TALK -> {
                    val intent = Intent(this, BoardTalkPostWriteActivity::class.java)
                    intent.putExtra("postWriteType", PostWriteType.WRITE)
                    startActivity(intent)
                }
            }
        }
    }
}

object CommunityBoardType{
    const val COUNSEL = 0
    const val TALK = 1
}

object PostWriteType {
    const val WRITE = 0
    const val EDIT = 1
}

