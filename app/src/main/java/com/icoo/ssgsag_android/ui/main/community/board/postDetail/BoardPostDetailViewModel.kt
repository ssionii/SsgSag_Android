package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail

class BoardPostDetailViewModel(

) : BaseViewModel(){

    private var _postDetail = MutableLiveData<BoardPostDetail>()
    val postDetail : LiveData<BoardPostDetail> = _postDetail

    init {
        getPostDetail()
    }

    fun getPostDetail(){
    }

}