package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class BoardPostDetailViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private var _postDetail = MutableLiveData<BoardPostDetail>()
    val postDetail : LiveData<BoardPostDetail> = _postDetail

    private var _commentList = MutableLiveData<ArrayList<PostComment>>()
    val commentList : LiveData<ArrayList<PostComment>> = _commentList

    var refreshedCommentPosition = MutableLiveData<Int>()
    lateinit var refreshedComment : PostComment


    fun getPostDetail(postIdx : Int){
        addDisposable(repository.getBoardPostDetail(postIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("get post detail error", it.message)
            }
            .subscribe({
                _postDetail.value = it
                _commentList.value = it.communityCommentList

            }) {
                Log.e("get post detail error", it.message)
            })
    }

    fun likeComment(postComment : PostComment, position : Int){
        if(postComment.like) {
            addDisposable(repository.unlikeCommunityComment(postComment.commentIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("unlike comment error", it.message)
                }
                .subscribe({
                    if(it.status == 200) {

                        refreshedComment = postComment
                        if(refreshedComment.likeNum > 0)
                            refreshedComment.likeNum--
                        refreshedComment.like = false

                        refreshedCommentPosition.value = position
                    }
                }) {

                })
        }else {
            addDisposable(repository.likeCommunityComment(postComment.commentIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("like comment error", it.message)
                }
                .subscribe({
                    if(it.status == 200) {
                        refreshedComment = postComment
                        refreshedComment.likeNum++
                        refreshedComment.like = true

                        refreshedCommentPosition.value = position
                    }
                }) {

                })
        }
    }

}