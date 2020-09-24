package com.icoo.ssgsag_android.ui.main.community.review.event

import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.event.EventRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class ReviewEventViewModel(
    private val repository: EventRepository,
    private val schedulerProvider: SchedulerProvider
): BaseViewModel(){

    fun postEvent(body: JsonObject){
        addDisposable(repository.postEvent(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({

                Log.e("review Event", it.status.toString())

                Toast.makeText(context, "응모 되었습니다.", Toast.LENGTH_SHORT).show()
            }, {
                it.printStackTrace()
            })
        )
    }
}