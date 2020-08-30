package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.content.Intent
import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetailBottomSheet
import com.icoo.ssgsag_android.databinding.ActivityBoardPostDetailBinding
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardType
import com.icoo.ssgsag_android.ui.main.photoEnlarge.PhotoExpandActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardPostDetailActivity : BaseActivity<ActivityBoardPostDetailBinding, BoardPostDetailViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_board_post_detail

    override val viewModel: BoardPostDetailViewModel by viewModel()

    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        type = intent.getIntExtra("type", CommunityBoardType.TALK)

        when(type){
            CommunityBoardType.COUNSEL -> viewDataBinding.actBoardPostDetailTvTitle.text = this.resources.getString(R.string.counsel_title)
            CommunityBoardType.TALK -> viewDataBinding.actBoardPostDetailTvTitle.text = this.resources.getString(R.string.talk_title)
        }

        setButton()

    }

    private fun setButton(){
        viewDataBinding.actBoardPostDetailClMenu.setSafeOnClickListener {
            val bottomSheet = BoardPostDetailBottomSheet(0, "post", type, true)

            bottomSheet.isCancelable = true
            bottomSheet.show(supportFragmentManager, null)
        }

        viewDataBinding.actBoardPostDetailIvPhoto.setSafeOnClickListener {
            val intent = Intent(this, PhotoExpandActivity::class.java)
            intent.putExtra("photoUrl", viewModel.postDetail.value?.photoUrl)
            startActivity(intent)
        }
    }
}