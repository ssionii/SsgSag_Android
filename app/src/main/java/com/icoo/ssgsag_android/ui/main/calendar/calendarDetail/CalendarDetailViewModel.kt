package com.icoo.ssgsag_android.ui.main.calendar.calendarDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.clickHistory.UserLogRepository
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.model.poster.posterDetail.analytics.Analytics
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepository
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass

class CalendarDetailViewModel(
    private val posterRepository: PosterRepository,
    private val scheduleRepository: ScheduleRepository,
    private val clickHistoryRepository: UserLogRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    private val _posterDetail = MutableLiveData<PosterDetail>()
    val posterDetail: LiveData<PosterDetail> get() = _posterDetail
    private val _analytics = MutableLiveData<Analytics>()
    val analytics: LiveData<Analytics> get() = _analytics
    private val _detailImage = MutableLiveData<String>()
    val detailImage : LiveData<String> get () = _detailImage
    private val _webUrl = MutableLiveData<String>()
    val webUrl : LiveData<String> get () = _webUrl

    var pushAlarmList = arrayListOf<Int>()

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart


    fun getPosterDetail(posterIdx: Int) {
        addDisposable(posterRepository.getPoster(posterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {

                    _posterDetail.setValue(this)
                    _webUrl.setValue(this.posterWebSite)
                    _analytics.postValue(this.analyticsJson)

                    if(this.photoUrl2.equals(null) || this.photoUrl2 == "") {
                        if(this.categoryIdx == 4){
                            _detailImage.postValue("intern")
                        }else
                            _detailImage.postValue(this.photoUrl)
                    }else{
                        _detailImage.postValue(this.photoUrl2)
                    }
                }
            }, {

            })
        )
    }

    fun getPosterDetailFromMain(posterIdx: Int, type: Int) {
        addDisposable(posterRepository.getPosterFromMain(posterIdx, type)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _posterDetail.setValue(this)
                    _webUrl.setValue(this.posterWebSite)
                    _analytics.postValue(this.analyticsJson)

                    if(this.photoUrl2.equals(null) || this.photoUrl2 == "") {
                        if(this.categoryIdx == 4){
                            _detailImage.postValue("intern")
                        }else
                            _detailImage.postValue(this.photoUrl)
                    }else{
                        _detailImage.postValue(this.photoUrl2)
                    }
                }
            }, {

            })
        )
    }


    fun recordClickHistory(posterIdx: Int) {
        val jsonObject = JSONObject()
        jsonObject.put("objectType", 1)
        jsonObject.put("objectIdx", posterIdx)
        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(clickHistoryRepository.posterDetailWebSiteClick(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                Log.d(TAG, it.toString())
            }, {
                it.printStackTrace()
            })
        )
    }

    //region comment
    fun writeComment(commentContent: String, posterIdx: Int) {
        val jsonObject = JSONObject()
        jsonObject.put("posterIdx", posterIdx)
        jsonObject.put("commentContent", commentContent)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(posterRepository.writeComment(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                getPosterDetail(posterIdx)
            }, {

            })
        )
    }

    fun editComment(commentContent: String, commentIdx: Int, posterIdx: Int) {
        val jsonObject = JSONObject()
        jsonObject.put("commentIdx", commentIdx)
        jsonObject.put("commentContent", commentContent)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(posterRepository.editComment(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                getPosterDetail(posterIdx)
            }, {

            })
        )
    }

    fun deleteComment(commentIdx: Int, posterIdx: Int) {
        addDisposable(posterRepository.deleteComment(commentIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                getPosterDetail(posterIdx)
            }, {

            })
        )
    }

    fun likeComment(commentIdx: Int, like: Int, posterIdx: Int) {
        addDisposable(posterRepository.likeComment(commentIdx, like)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                getPosterDetail(posterIdx)
            }, {

            })
        )
    }

    fun cautionComment(commentIdx: Int) {
        addDisposable(posterRepository.cautionComment(commentIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                Log.d(TAG, "caution: " + it.toString())
            }, {

            })
        )
    }


    fun bookmark(posterIdx: Int) {
        lateinit var response: Single<Int>

        if(posterDetail.value?.isFavorite == 0) {
            response = scheduleRepository.bookmarkSchedule(posterIdx)
            Toast.makeText(context, "즐겨찾기에 추가되었습니다.",Toast.LENGTH_SHORT).show();
        }
        else if(posterDetail.value?.isFavorite == 1) {
            response = scheduleRepository.unbookmarkSchedule(posterIdx)
            Toast.makeText(context, "즐겨찾기에서 삭제되었습니다.",Toast.LENGTH_SHORT).show();
        }
        addDisposable(response
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                getPosterDetail(posterIdx)
            }, {

            })
        )
    }

    fun unBookmarkWithAlarm(posterIdx: Int){
        addDisposable(posterRepository.deleteTodoPushAlarm(posterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                Log.e("unBookmark status", it.toString())
                getPosterDetail(posterIdx)
            }, {

            })
        )
    }


    fun managePoster(posterIdx: Int) {
        if(posterDetail.value?.isSave == 0) {
            addDisposable(
                posterRepository.saveAtPosterDetail(posterIdx)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.mainThread())
                    .subscribe({
                        getPosterDetail(posterIdx)
                        Toast.makeText(SsgSagApplication.getGlobalApplicationContext(),
                            "캘린더에 저장되었습니다.",Toast.LENGTH_SHORT).show()
                    }, {

                    })
            )

        }else if(posterDetail.value?.isSave == 1){
            var posterIdxList = arrayListOf(posterIdx)

            val jsonObject = JSONObject()
            val jsonArray = JSONArray(posterIdxList)
            jsonObject.put("posterIdxList", jsonArray)

            val body = JsonParser().parse(jsonObject.toString()) as JsonObject

            addDisposable(scheduleRepository.deleteSchedule(body)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    getPosterDetail(posterIdx)
                    Toast.makeText(SsgSagApplication.getGlobalApplicationContext(),
                        "캘린더에서 삭제되었습니다!",Toast.LENGTH_SHORT).show()
                }, {
                    it.printStackTrace()
                })
            )

        }else{
            Toast.makeText(SsgSagApplication.getGlobalApplicationContext(),
                "포스터 정보가 없습니다",Toast.LENGTH_SHORT).show();
        }
    }

    // 푸시 알림 관련 함수
    fun getPushAlarm(posterIdx: Int){
        addDisposable(posterRepository.getTodoPushAlarm(posterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                pushAlarmList = it
            }, {

            })
        )
    }

    fun navigatePhoto(activity: KClass<*>, photoUrl: String?) {
        val bundle = Bundle().apply { putString("photoUrl", photoUrl)}
        _activityToStart.postValue(Pair(activity, bundle))
    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "CalendarDetailViewModel"
    }
}