package com.icoo.ssgsag_android.ui.main.community.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.data.model.community.board.CommunityBoardPostDetail
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class CommunityBoardViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private var _postList = MutableLiveData<ArrayList<CommunityBoardPostDetail>>()
    val postList : LiveData<ArrayList<CommunityBoardPostDetail>> = _postList

    val dummuyAllCounselList = arrayListOf(
        CommunityBoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg" ),
        CommunityBoardPostDetail(0, "취업/진로", 2, "취업 진로 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까? 밍밍밍밍밍밍밍밍밍밍", 30, 10,"1시간 전", "http://cfile235.uf.daum.net/image/2536EB43596E4C5A0C28E1" ),
        CommunityBoardPostDetail(0, "공모전/대외활동", 1, "공모전대외활동 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null ),
        CommunityBoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 3", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg" ),
        CommunityBoardPostDetail(0, "취업/진로", 2, "취업 진로 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까? 밍밍밍밍밍밍밍밍밍밍", 30, 10,"1시간 전", "http://cfile235.uf.daum.net/image/2536EB43596E4C5A0C28E1" ),
        CommunityBoardPostDetail(0, "공모전/대외활동", 1, "공모전대외활동 관련 게시글 4", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null ))

    val dummyActList = arrayListOf(
        CommunityBoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg" ),
        CommunityBoardPostDetail(0, "공모전/대외활동", 1, "공모전대외활동 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null ),
        CommunityBoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 3", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "http://img.segye.com/content/image/2019/10/31/20191031513063.jpg" ))

    val dummyTalkList = arrayListOf(
        CommunityBoardPostDetail(0, "자유게시판", 1, "자유게시판 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg" ),
        CommunityBoardPostDetail(0, "자유게시판", 1, "자유게시판 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null ),
        CommunityBoardPostDetail(0, "자유게시판", 1, "자유게시판 관련 게시글 3", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg" ))


    fun getCounselList(type : Int){
        when(type){
            0 -> _postList.postValue(dummuyAllCounselList)
            1 -> _postList.postValue(dummyActList)
            2 ->  _postList.postValue(arrayListOf())
            else -> _postList.postValue(dummyActList)
        }
    }

    fun getTalkList(){
        _postList.postValue(dummyTalkList)
    }
}