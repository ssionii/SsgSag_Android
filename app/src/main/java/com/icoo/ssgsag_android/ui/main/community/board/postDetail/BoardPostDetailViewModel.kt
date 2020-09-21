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

    var status = MutableLiveData<Int>()
    var refreshedCommentPosition = MutableLiveData<Int>()
    var refreshedReplyPosition = MutableLiveData<Int>()
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

    fun likeReply(postComment : PostComment, commentPosition : Int, replyPosition : Int){

        Log.e("postComment", postComment.toString())

        Log.e("like comment", commentPosition.toString())
        Log.e("like reply", replyPosition.toString())
        Log.e("like reply idx", postComment.ccommentIdx.toString())
        if(postComment.like) {
            addDisposable(repository.unlikeCommunityReply(postComment.ccommentIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("unlike reply error", it.message)
                }
                .subscribe({
                    if(it.status == 200) {
                        refreshedComment = postComment
                        if(refreshedComment.likeNum > 0)
                            refreshedComment.likeNum--
                        refreshedComment.like = false
                        refreshedCommentPosition.value = commentPosition
                        refreshedReplyPosition.value = replyPosition

                    }

                }) {

                })
        }else {
            addDisposable(repository.likeCommunityReply(postComment.ccommentIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("like reply error", it.message)
                }
                .subscribe({
                    if(it.status == 200) {
                        refreshedComment = postComment
                        refreshedComment.likeNum++
                        refreshedComment.like = true
                        refreshedCommentPosition.value = commentPosition
                        refreshedReplyPosition.value = replyPosition
                    }

                    Log.e("status", it.status.toString())
                }) {
                    Log.e("like reply", it.message)
                })
        }
    }

    fun deletePost(postIdx : Int){
        addDisposable(repository.deleteBoardPost(postIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("get post detail error", it.message)
            }
            .subscribe({
                Log.e("status", it.status.toString())
                status.value = it.status
            }) {
                Log.e("get post detail error", it.message)
            })
    }

}