package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.ui.main.community.board.BoardPostDetailBottomSheet
import com.icoo.ssgsag_android.databinding.ActivityBoardPostDetailBinding
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardType
import com.icoo.ssgsag_android.ui.main.photoEnlarge.PhotoExpandActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardPostDetailActivity : BaseActivity<ActivityBoardPostDetailBinding, BoardPostDetailViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_board_post_detail

    override val viewModel: BoardPostDetailViewModel by viewModel()

    lateinit var boardPostCommentRecyclerViewAdapter : BoardPostCommentRecyclerViewAdapter

    var type = 0
    var postIdx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        type = intent.getIntExtra("type", CommunityBoardType.TALK)
        postIdx = intent.getIntExtra("postIdx", 0)


        when(type){
            CommunityBoardType.COUNSEL -> viewDataBinding.actBoardPostDetailTvTitle.text = this.resources.getString(R.string.counsel_title)
            CommunityBoardType.TALK -> viewDataBinding.actBoardPostDetailTvTitle.text = this.resources.getString(R.string.talk_title)
        }

        viewModel.getPostDetail(postIdx)


        viewModel.postDetail.observe(this, Observer {
            setButton()
        })

        setCommentRv()
        refreshComment()
    }

    private fun setCommentRv(){

        boardPostCommentRecyclerViewAdapter = BoardPostCommentRecyclerViewAdapter()
        boardPostCommentRecyclerViewAdapter.apply {
            setOnCommentClickListener(commentClickListener)
            setHasStableIds(true)
        }

        viewDataBinding.actBoardPostDetailRvComment.run{
            adapter = boardPostCommentRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.commentList.observe(this, Observer {
            boardPostCommentRecyclerViewAdapter.run{
                replaceAll(it)
                notifyDataSetChanged()
            }

        })
    }

    private fun refreshComment(){
        viewModel.refreshedCommentPosition.observe(this, Observer {
            boardPostCommentRecyclerViewAdapter.run{
                replaceItem(viewModel.refreshedComment, viewModel.refreshedCommentPosition.value!!)
                notifyItemChanged(viewModel.refreshedCommentPosition.value!!)
            }
        })
    }

    val commentClickListener = object : BoardPostCommentRecyclerViewAdapter.OnCommentClickListener{
        override fun onLikeClick(postComment: PostComment, position: Int) {
            viewModel.likeComment(postComment, position)
        }

        override fun onMoreLikeClick(postComment: PostComment, position: Int) {
            val bottomSheet =
                BoardPostDetailBottomSheet(
                    postComment.commentIdx,
                    "comment",
                    type,
                    postComment.mine
                )

            bottomSheet.isCancelable = true
            bottomSheet.show(supportFragmentManager, null)
        }

        override fun onReplyLikeClick(commentIdx: Int) {
            TODO("Not yet implemented")
        }
    }

    private fun setButton(){

        viewDataBinding.actBoardPostDetailClBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actBoardPostDetailClMenu.setSafeOnClickListener {
            val bottomSheet =
                BoardPostDetailBottomSheet(
                    postIdx,
                    "post",
                    type,
                    viewModel.postDetail.value!!.mine
                )

            bottomSheet.isCancelable = true
            bottomSheet.show(supportFragmentManager, null)
        }

        viewDataBinding.actBoardPostDetailIvPhoto.setSafeOnClickListener {
            val intent = Intent(this, PhotoExpandActivity::class.java)
            intent.putExtra("photoUrl", viewModel.postDetail.value?.community?.photoUrlList)
            startActivity(intent)
        }
    }
}