package com.icoo.ssgsag_android.ui.main.community.board

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.CommunityBoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.CounselBoardCategory
import com.icoo.ssgsag_android.databinding.ActivityCommunityBoardBinding
import com.icoo.ssgsag_android.databinding.ItemBoardBinding
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityBoardActivity : BaseActivity<ActivityCommunityBoardBinding, CommunityBoardViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_community_board

    override val viewModel: CommunityBoardViewModel by viewModel()

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

        when(intent.getIntExtra("type", CommunityBoardType.TALK)){
            CommunityBoardType.COUNSEL -> {

            }
            CommunityBoardType.TALK -> {

            }
        }

        setTab()
        setRv()

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
            adapter = object : BaseRecyclerViewAdapter<CommunityBoardPostDetail, ItemBoardBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_board
                override val bindingVariableId: Int
                    get() = BR.postDetail
                override val listener: OnItemClickListener?
                    get() = null
            }

            layoutManager = WrapContentLinearLayoutManager()
        }
    }

}

object CommunityBoardType{
    const val COUNSEL = 0
    const val TALK = 1
}