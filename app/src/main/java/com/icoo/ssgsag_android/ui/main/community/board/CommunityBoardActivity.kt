package com.icoo.ssgsag_android.ui.main.community.board

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.CounselBoardCategory
import com.icoo.ssgsag_android.databinding.ActivityCommunityBoardBinding
import com.icoo.ssgsag_android.databinding.ItemBoardBinding
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailActivity
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.review.main.AutoScrollAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityBoardActivity : BaseActivity<ActivityCommunityBoardBinding, CommunityBoardViewModel>(), BaseRecyclerViewAdapter.OnItemClickListener{

    override val layoutResID: Int
        get() = R.layout.activity_community_board

    override val viewModel: CommunityBoardViewModel by viewModel()

    var communityBoardType = CommunityBoardType.TALK

    val counselBoardCategoryList = arrayListOf(
        CounselBoardCategory("전체", 0),
        CounselBoardCategory("공모전/대외활동", 1),
        CounselBoardCategory("취업/진로", 2),
        CounselBoardCategory("학교생활", 3),
        CounselBoardCategory("일상/연애", 4),
        CounselBoardCategory("기타", 5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        communityBoardType = intent.getIntExtra("type", CommunityBoardType.TALK)
        when(communityBoardType){
            CommunityBoardType.COUNSEL -> {
                viewDataBinding.actCommunityBoardTvTitle.text = this.resources.getString(R.string.counsel_title)
                setTab()
                viewModel.getCounselList(0)
            }
            CommunityBoardType.TALK -> {
                viewDataBinding.actCommunityBoardTvTitle.text = this.resources.getString(R.string.talk_title)
                viewDataBinding.actCommunityBoardTl.visibility = GONE
                viewModel.getTalkList()
            }
        }

        setTopBanner()
        setRv()
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
    private fun setTab(){

        viewDataBinding.actCommunityBoardTl.apply{
            for(category in counselBoardCategoryList){
                addTab(newTab().setText(category.name))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.getCounselList(counselBoardCategoryList[tab!!.position].type)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

            })
        }
    }

    private fun setRv(){
        viewDataBinding.actCommunityBoardRv.run{
            adapter = object : BaseRecyclerViewAdapter<BoardPostDetail, ItemBoardBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_board
                override val bindingVariableId: Int
                    get() = BR.postDetail
                override val listener: OnItemClickListener?
                    get() = this@CommunityBoardActivity
            }

            layoutManager = LinearLayoutManager(this@CommunityBoardActivity)
        }

        val emptyView = viewDataBinding.actCommunityBoardLlEmpty
        val recyclerView = viewDataBinding.actCommunityBoardRv

        viewModel.postList.observe(this, Observer {
            (viewDataBinding.actCommunityBoardRv.adapter as BaseRecyclerViewAdapter<BoardPostDetail, *>).run{
                replaceAll(it)
                notifyDataSetChanged()
                if(it.size > 0) {
                    emptyView.visibility = GONE
                    recyclerView.visibility = VISIBLE
                }else{
                    emptyView.visibility = VISIBLE
                    recyclerView.visibility = GONE
                }

            }
        })
    }

    override fun onItemClicked(item: Any?, position: Int?) {
        val intent = Intent(this, BoardPostDetailActivity::class.java)
        intent.putExtra("type", communityBoardType)
        startActivity(intent)
    }

    private fun setButton(){

        viewDataBinding.actCommunityBoardClBack.setSafeOnClickListener {
            finish()
        }
    }

}

object CommunityBoardType{
    const val COUNSEL = 0
    const val TALK = 1
}