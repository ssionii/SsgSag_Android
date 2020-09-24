package com.icoo.ssgsag_android.ui.main.community.board.postDetail.write

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONObject

class BoardPostWriteViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private var _postDetail = MutableLiveData<BoardPostDetail>()
    val postDetail : LiveData<BoardPostDetail> = _postDetail

    var writeStatus = MutableLiveData<Int>()
    var editStatus = MutableLiveData<Int>()

    fun getPostDetail(postIdx : Int){
        addDisposable(repository.getBoardPostDetail(postIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("get post detail error", it.message)
            }
            .subscribe({
                _postDetail.value = it

            }) {
                Log.e("get post detail error", it.message)
            })
    }

    fun writeBoardPost(jsonObject: JSONObject){

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.writeBoardPost(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                writeStatus.value = it.status
            }, {

            })
        )

    }

    fun editBoardPost(jsonObject: JSONObject){

        Log.e("수정!", "호로")
        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.editBoardPost(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                editStatus.value = it.status
            }, {

            })
        )

    }
}