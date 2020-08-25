package com.icoo.ssgsag_android.ui.main.community.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class CommunityBoardViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private var _postList = MutableLiveData<ArrayList<BoardPostDetail>>()
    val postList : LiveData<ArrayList<BoardPostDetail>> = _postList

    private var _topBannerImage = MutableLiveData<String>()
    val topBannerImage : LiveData<String> = _topBannerImage

    val dummuyAllCounselList = arrayListOf(
        BoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg", 1, null, "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg", null),
        BoardPostDetail(0, "취업/진로", 2, "취업 진로 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까? 밍밍밍밍밍밍밍밍밍밍", 30, 10,"1시간 전", "http://cfile235.uf.daum.net/image/2536EB43596E4C5A0C28E1" ,1, null, null, null),
        BoardPostDetail(0, "공모전/대외활동", 1, "공모전대외활동 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null, 1, null, null, null),
        BoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 3", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg", 0, null, null, null ),
        BoardPostDetail(0, "취업/진로", 2, "취업 진로 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까? 밍밍밍밍밍밍밍밍밍밍", 30, 10,"1시간 전", "http://cfile235.uf.daum.net/image/2536EB43596E4C5A0C28E1", 0, null, "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg", null),
        BoardPostDetail(0, "공모전/대외활동", 1, "공모전대외활동 관련 게시글 4", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null , 0, null, "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg", null))

    val dummyActList = arrayListOf(
        BoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg" , 0, null, "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg", null),
        BoardPostDetail(0, "공모전/대외활동", 1, "공모전대외활동 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null , 0, null, null, null),
        BoardPostDetail(0, "공모전/대외활동", 1, "공모전 대외활동 관련 게시글 3", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "http://img.segye.com/content/image/2019/10/31/20191031513063.jpg" , 0, null, "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg", null))

    val dummyTalkList = arrayListOf(
        BoardPostDetail(0, "자유게시판", 1, "자유게시판 관련 게시글 1", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg" , 0, null, null, null),
        BoardPostDetail(0, "자유게시판", 1, "자유게시판 관련 게시글 2", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", null , 0, null, null, null),
        BoardPostDetail(0, "자유게시판", 1, "자유게시판 관련 게시글 3", "설명이다 와라랄랄라 과연 어떻게 끊길까?", 30, 10,"1시간 전", "https://img.theqoo.net/img/RVyXA.jpg" , 0, null, "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg", null))


    init{
        _topBannerImage.postValue("http://cfile235.uf.daum.net/image/2536EB43596E4C5A0C28E1")
    }

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