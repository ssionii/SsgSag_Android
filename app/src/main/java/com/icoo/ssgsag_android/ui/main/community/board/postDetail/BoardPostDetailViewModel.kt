package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONObject

class BoardPostDetailViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private var _postDetail = MutableLiveData<BoardPostDetail>()
    val postDetail : LiveData<BoardPostDetail> = _postDetail

    private var _commentList = MutableLiveData<ArrayList<PostComment>>()
    val commentList : LiveData<ArrayList<PostComment>> = _commentList

    var isReply = MutableLiveData<Boolean>()
    var getPostDetailStatus = MutableLiveData<Int>()
    var deleteStatus = MutableLiveData<Int>()
    var bookmarkStatus = MutableLiveData<Int>()
    var likeStatus = MutableLiveData<Int>()
    var writeCommentStatus = MutableLiveData<Int>()
    var refreshedCommentPosition = MutableLiveData<Int>()
    lateinit var refreshedComment : PostComment

    var postIdx = 0

    init{
        isReply.value = false
    }


    fun getPostDetail(postIdx : Int){
        this.postIdx = postIdx
        addDisposable(repository.getBoardPostDetail(postIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("get post detail error", it.message)
            }
            .subscribe({
                getPostDetailStatus.value = it.status
                if(it.status == 200) {
                    _postDetail.value = it.data
                    _commentList.value = it.data.communityCommentList
                }

            }) {
                Log.e("get post detail error", it.message)
            })
    }

    fun refreshPostDetail(postIdx : Int){
        addDisposable(repository.refreshPostDetail(postIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("refresh detail error", it.message)
            }
            .subscribe({
                _postDetail.value = it
                _commentList.value = it.communityCommentList

            }) {
                Log.e("refresh detail error", it.message)
            })
    }

    fun deletePost(postIdx : Int){
        addDisposable(repository.deleteBoardPost(postIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("delete post error", it.message)
            }
            .subscribe({
                if(it.status == 200) {
                    Toast.makeText(context, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                deleteStatus.value = it.status
            }) {
                Log.e("delete post error", it.message)
            })
    }

    fun bookmarkPost(postIdx: Int){
        if(postDetail.value!!.save){
            addDisposable(repository.unbookmarkCommunityPost(postIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("bookmark post error", it.message)
                }
                .subscribe({
                    if(it.status == 200){
                        var tempPostDetail = postDetail.value!!
                        tempPostDetail.save = false
                        Toast.makeText(
                            context,
                            "[내 정보 > 스크랩한 글] 에 보관되었습니다.",Toast.LENGTH_SHORT).show()

                        _postDetail.value = tempPostDetail
                    }
                    bookmarkStatus.value = it.status
                }) {
                    Log.e("bookmark post error", it.message)
                })
        }else {
            addDisposable(repository.bookmarkCommunityPost(postIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("bookmark post error", it.message)
                }
                .subscribe({
                    if(it.status == 200){
                        var tempPostDetail = postDetail.value!!
                        tempPostDetail.save = true

                        _postDetail.value = tempPostDetail
                    }
                    bookmarkStatus.value = it.status
                }) {
                    Log.e("bookmark post error", it.message)
                })
        }
    }

    fun likePost(postIdx: Int){
        if(postDetail.value!!.like){
            addDisposable(repository.unlikeCommunityPost(postIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("like post error", it.message)
                }
                .subscribe({
                    if(it.status == 200){
                        var tempPostDetail = postDetail.value!!
                        tempPostDetail.like = false
                        tempPostDetail.community.likeNum = tempPostDetail.community.likeNum!! - 1

                        _postDetail.value = tempPostDetail
                    }
                    likeStatus.value = it.status
                }) {
                    Log.e("like post error", it.message)
                })
        }else {
            addDisposable(repository.likeCommunityPost(postIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnError {
                    Log.e("like post error", it.message)
                }
                .subscribe({
                    if(it.status == 200){
                        var tempPostDetail = postDetail.value!!
                        tempPostDetail.like = true
                        tempPostDetail.community.likeNum = tempPostDetail.community.likeNum!! + 1

                        _postDetail.value = tempPostDetail
                    }
                    likeStatus.value = it.status
                }) {
                    Log.e("like post error", it.message)
                })
        }
    }

    fun deleteComment(commentIdx : Int){
        addDisposable(repository.deletePostComment(commentIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("delete comment error", it.message)
            }
            .subscribe({
                Log.e("status", it.status.toString())
                deleteStatus.value = it.status
            }) {
                Log.e("delete comment error", it.message)
            })
    }

    fun deleteReply(ccommentIdx : Int){
        addDisposable(repository.deletePostReply(ccommentIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("delete reply error", it.message)
            }
            .subscribe({
                Log.e("status", it.status.toString())
                deleteStatus.value = it.status
            }) {
                Log.e("delete reply error", it.message)
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

    fun likeReply(postComment : PostComment, position: Int){

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
                        refreshedCommentPosition.value = position
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
                        refreshedCommentPosition.value = position
                    }

                }) {
                    Log.e("like reply", it.message)
                })
        }
    }


    fun writeComment(jsonObject: JSONObject){
        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.writePostComment(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("write comment error", it.message)
            }
            .subscribe({
                writeCommentStatus.value = it.status
                if(it.status == 200){
                    refreshPostDetail(postIdx)
                }
            }) {
                Log.e("get post detail error", it.message)
            })
    }

    fun writeReply(jsonObject: JSONObject){
        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.writePostReply(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("write reply error", it.message)
            }
            .subscribe({
                if(it.status == 200){
                    refreshPostDetail(postIdx)
                }
            }) {
                Log.e("get post detail error", it.message)
            })
    }

}