package com.icoo.ssgsag_android.ui.main.calendar.posterBookmark

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class PosterBookmarkViewModel(
    private val posterRepository: PosterRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    var pushAlarmList = MutableLiveData<ArrayList<Int>>()


    fun getPushAlarm(posterIdx: Int){
        addDisposable(posterRepository.getTodoPushAlarm(posterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                pushAlarmList.value = it
            }, {

            })
        )
    }

    fun bookmarkWithAlarm(posterIdx: Int, ddayList : String){
        addDisposable(posterRepository.postTodoPushAlarm(posterIdx, ddayList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                Log.e("bookmark status", it.toString())
                if(it == 204){

                    Toast.makeText(context, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }, {

            })
        )
    }

    fun unBookmarkWithAlarm(posterIdx: Int){
        addDisposable(posterRepository.deleteTodoPushAlarm(posterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                Log.e("unbookmark status", it.toString())
                if(it == 204){
                    Toast.makeText(context, "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }, {

            })
        )
    }
}