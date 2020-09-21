package com.icoo.ssgsag_android.ui.main.community.board.postDetail.write

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONObject

class BoardPostWriteViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    var status = MutableLiveData<Int>()

    fun writeBoardPost(jsonObject: JSONObject){

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.writeBoardPost(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                status.value = it.status
            }, {

            })
        )

    }
}