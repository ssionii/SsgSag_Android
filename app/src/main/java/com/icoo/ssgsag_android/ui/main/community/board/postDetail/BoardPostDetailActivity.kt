package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityBoardPostDetailBinding
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardType
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardPostDetailActivity : BaseActivity<ActivityBoardPostDetailBinding, BoardPostDetailViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_board_post_detail

    override val viewModel: BoardPostDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        when(intent.getIntExtra("type", CommunityBoardType.TALK)){
            CommunityBoardType.COUNSEL -> viewDataBinding.actBoardPostDetailTvTitle.text = this.resources.getString(R.string.counsel_title)
            CommunityBoardType.TALK -> viewDataBinding.actBoardPostDetailTvTitle.text = this.resources.getString(R.string.talk_title)
        }


    }
}