package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.databinding.ActivityBoardPostDetailBinding
import com.icoo.ssgsag_android.ui.main.community.board.BoardPostDetailBottomSheet
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardType
import com.icoo.ssgsag_android.ui.main.photoEnlarge.PhotoExpandActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardPostDetailActivity : BaseActivity<ActivityBoardPostDetailBinding, BoardPostDetailViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_board_post_detail

    override val viewModel: BoardPostDetailViewModel by viewModel()

    lateinit var boardPostCommentRecyclerViewAdapter : BoardPostCommentRecyclerViewAdapter

    var type = 0
    var postIdx = 0

    private var viewBottom = 0

    private var commentIdx = 0
    var userNickname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel
        viewDataBinding.activity = this

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
        setPullToRefresh()
        setObserver()

        viewDataBinding.actBoardPostDetailClBack.setSafeOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPostDetail(postIdx)
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
            itemAnimator = null
        }

        viewModel.commentList.observe(this, Observer {
            val tempCommentList = arrayListOf<PostComment>()

            for(comment in it){
                comment.type = 0
                tempCommentList.add(comment)

                if(comment.communityCCommentList!!.size > 0){
                    for(reply in comment.communityCCommentList!!){
                        reply.type = 1
                        tempCommentList.add(reply)
                    }
                }
            }

            boardPostCommentRecyclerViewAdapter.run{
                replaceAll(tempCommentList)
                notifyDataSetChanged()
            }

        })
    }

    private fun refreshComment(){
        viewModel.refreshedCommentPosition.observe(this, Observer {
            boardPostCommentRecyclerViewAdapter.run{
                replaceItem(viewModel.refreshedComment, it)
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
            bottomSheet.setOnSheetDismissedListener(sheetDismissedListener)
            bottomSheet.show(supportFragmentManager, null)
        }

        override fun onReplyClick(idx: Int, name: String, position : Int) {
            showKeyboard(viewDataBinding.actBoardPostDetailEtComment)
            viewModel.isReply.value = true
            commentIdx = idx
            userNickname = name

            scroll(position)
        }

        override fun onReplyLikeClick(postComment: PostComment, position: Int) {
            viewModel.likeReply(postComment, position)
        }

        override fun onReplyMoreLikeClick(postComment: PostComment, position: Int) {
            val bottomSheet =
                BoardPostDetailBottomSheet(
                    postComment.ccommentIdx,
                    "reply",
                    type,
                    postComment.mine
                )

            bottomSheet.isCancelable = true
            bottomSheet.setOnSheetDismissedListener(sheetDismissedListener)
            bottomSheet.show(supportFragmentManager, null)
        }

        override fun onReplyReplyClick(idx: Int, name: String, position : Int) {
            showKeyboard(viewDataBinding.actBoardPostDetailEtComment)
            viewModel.isReply.value = true
            commentIdx = idx
            userNickname = name

            scroll(position)

        }

    }


    private fun setPullToRefresh(){
        viewDataBinding.actBoardPostDetailSrl.apply{
            setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                override fun onRefresh() {
                    // 새로고침 코드
                   viewModel.getPostDetail(postIdx)

                    viewDataBinding.actBoardPostDetailSrl.isRefreshing = false
                }
            })
        }
    }

    private fun setButton(){

        viewDataBinding.actBoardPostDetailIvReplyGuideCancel.setSafeOnClickListener {
            viewModel.isReply.value = false

            viewDataBinding.actBoardPostDetailEtComment.text.clear()
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
            bottomSheet.setOnSheetDismissedListener(sheetDismissedListener)
            bottomSheet.show(supportFragmentManager, null)
        }

        viewDataBinding.actBoardPostDetailIvPhoto.setSafeOnClickListener {

            var tempImgUrl = ""

            viewModel.postDetail.value?.community?.photoUrlList?.let {
                if (it.indexOf(',') > -1) {
                    tempImgUrl = it.substring(0, it.indexOf(','))
                } else {
                    tempImgUrl = it
                }
            }

            val intent = Intent(this, PhotoExpandActivity::class.java)
            intent.putExtra("photoUrl", tempImgUrl)
            startActivity(intent)
        }

        viewDataBinding.actBoardPostDetailIvBookmark.setSafeOnClickListener {
            viewModel.bookmarkPost(postIdx)
        }

        viewDataBinding.actBoardPostDetailIvLike.setSafeOnClickListener {
            viewModel.likePost(postIdx)
        }

        viewDataBinding.actBoardPostDetailIvWriteComment.setSafeOnClickListener {

            if(viewDataBinding.actBoardPostDetailEtComment.text.toString() != "") {
                if(viewModel.isReply.value!!){
                    val jsonObject = JSONObject()
                    jsonObject.put("content", viewDataBinding.actBoardPostDetailEtComment.text)
                    jsonObject.put("commentIdx", commentIdx)
                    jsonObject.put("commentName", userNickname)

                    viewModel.writeReply(jsonObject)
                    hideKeyboard(viewDataBinding.actBoardPostDetailEtComment)

                    viewDataBinding.actBoardPostDetailEtComment.text.clear()
                    viewModel.isReply.value = false

                }else{
                    val jsonObject = JSONObject()
                    jsonObject.put("content", viewDataBinding.actBoardPostDetailEtComment.text)
                    jsonObject.put("communityIdx", postIdx)

                    viewModel.writeComment(jsonObject)
                    hideKeyboard(viewDataBinding.actBoardPostDetailEtComment)

                    viewDataBinding.actBoardPostDetailEtComment.text.clear()
                }
            }else{
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setObserver(){
        viewModel.writeCommentStatus.observe(this, Observer {
            if(it == 200){
                viewModel.isReply.value = false
                scrollToViewBottom(viewDataBinding.actBoardPostDetailRvComment, viewDataBinding.actBoardPostDetailNsv)
            }
        })

        viewModel.isReply.observe(this, Observer {
            if(!it) boardPostCommentRecyclerViewAdapter.clearItemBg()
        })

        viewModel.bookmarkStatus.observe(this, Observer {
            if(it == 200){
                if(viewModel.postDetail.value!!.save){
                    viewDataBinding.actBoardPostDetailIvBookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_big_active))
                } else {
                    viewDataBinding.actBoardPostDetailIvBookmark.setImageDrawable(resources.getDrawable(R.drawable.ic_bookmark_big))
                }
            }
        })

        viewModel.likeStatus.observe(this, Observer {
            if(it == 200){
                if(viewModel.postDetail.value!!.like){
                    viewDataBinding.actBoardPostDetailIvLike.setImageDrawable(resources.getDrawable(R.drawable.ic_like_big_filled_active))
                } else {
                    viewDataBinding.actBoardPostDetailIvLike.setImageDrawable(resources.getDrawable(R.drawable.ic_like_big_outlined))
                }
            }
        })

    }

    val sheetDismissedListener = object : BoardPostDetailBottomSheet.OnSheetDismissedListener{

        override fun onPostEdited() {

            // 리스트에 변경사항 알려줌
            val result = Intent().apply {
                putExtra("type", "edit")
            }
            setResult(Activity.RESULT_OK, result)
        }

        override fun onPostDeleted() {

            // 리스트에 변경사항 알려줌
            val result = Intent().apply {
                putExtra("type", "delete")
            }
            setResult(Activity.RESULT_OK, result)
            finish()
        }

        override fun onCommentDeleted() {
            viewModel.refreshPostDetail(postIdx)
        }
    }

    fun scrollToViewBottom(view: View, scrollView: NestedScrollView){
        if(view != null && view != scrollView){
            viewBottom += view.bottom
            scrollToViewBottom((view.parent as View), scrollView)
        } else if(scrollView != null){
            Handler().postDelayed({ scrollView.smoothScrollTo(0, viewBottom) }, 100)
        }
    }

    fun scroll(position : Int){
        viewDataBinding.actBoardPostDetailRvComment.apply{
            post {
                val height : Float = y + getChildAt(position).y
                viewDataBinding.actBoardPostDetailNsv.smoothScrollTo(0, height.toInt())
            }
        }
    }

    fun hideKeyboard(et: EditText){

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0)

    }

    fun showKeyboard(et: EditText){

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        et.requestFocus()
        imm.showSoftInput(et, 0)

    }
}