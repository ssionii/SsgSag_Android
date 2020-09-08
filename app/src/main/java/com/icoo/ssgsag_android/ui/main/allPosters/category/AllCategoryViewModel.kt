package com.icoo.ssgsag_android.ui.main.allPosters.category

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass

class AllCategoryViewModel(
    private val repository: PosterRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    private var _posters = MutableLiveData<ArrayList<PosterDetail>>()
    val posters: LiveData<ArrayList<PosterDetail>> get() = _posters
    private val _empty = MutableLiveData<Boolean>()
    val empty : LiveData<Boolean> get() = _empty

    private var _refreshedPoster = MutableLiveData<PosterDetail>()
    val refreshedPoster : LiveData<PosterDetail> get() = _refreshedPoster

    var refreshedPosterPosition  = 0
    var refreshedPosterIdx = 0

    private val _sortType = MutableLiveData<Int>()
    val sortType: LiveData<Int> get() = _sortType
    private val _field = MutableLiveData<String>()
    val field: LiveData<String> get() = _field
    private val _subCategory = MutableLiveData<Int>()
    val subCategory: LiveData<Int> get() = _subCategory

    var category = 0
    private val _isUnivClub = MutableLiveData<Boolean>()
    val isUnivClub: LiveData<Boolean> get() = _isUnivClub

    var clickedFiledPositionLeft = 0
    var clickedFieldPositionRight = 0
    var isLeftHeaderClicked = true

    init {
        _sortType.setValue(2)
        _field.setValue("0")
        _empty.setValue(false)
    }


    fun getAllPosterCategory(curPage: Int){

        _field.postValue("0")
        addDisposable(repository.getAllPostersCategory(category, this.sortType.value!!, curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    if(this.size == 0){
                        _empty.setValue(true)
                    }else {
                        _empty.setValue(false)
                        _posters.setValue(this)
                    }
                }
            }, {
                _empty.postValue(false)
            })
        )
    }

    fun getAllPosterField(data:Int, curPage: Int, category: Int, isEnterprise: Boolean = true){

        var field = "0"
        if(category == 1){
            when(data){
                1 -> field = "10000, 251"
                2 -> field = "50000, 251"
                3 -> field = "252"
                4 -> field = "255"
                5 -> field = "254"
                6 -> field = "299"

            }
        }else if(category == 0){
            when(data){
                1 -> field = "201"
                2 -> field = "202"
                3 -> field = "206"
                4 -> field = "205"
                5 -> field = "207"
                6 -> field = "204"
                7 -> field = "208"
                8 -> field = "215"
                9 -> field = "299"
            }
        }else if(category == 4 && isEnterprise){
            when(data){
                1 -> field = "10000"
                2 -> field = "50000"
                3 -> field = "20000"
                4 -> field = "60000"
                5 -> field = "40000"
                6 -> field = "30000"
                7 -> field = "95000"
            }
        }else if(category == 4 && !isEnterprise){
            when(data){
                1 -> field = "110"
                2 -> field = "109"
                3 -> field = "103"
                4 -> field = "112"
                5 -> field = "101"
                6 -> field = "104"
                7 -> field = "107"
                8 -> field = "102"
                9 -> field = "106"
                10 -> field = "111"
                11 -> field = "199"
            }
        }else if(category == 2 || category == 6){
            field = (data + 400).toString()
        }

        if(data == 0)
            field = "0"

        _field.setValue(field)

        addDisposable(repository.getAllPostersField(category, field, this.sortType.value!!, curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _posters.setValue(this)
                    if(this.size == 0){
                        _empty.postValue(true)
                    }else
                        _empty.postValue(false)
                }
            }, {
                it.printStackTrace()
                _empty.postValue(false)
            })
        )
    }

    fun getRefreshedPoster(){
        addDisposable(repository.getPosterFromMain(refreshedPosterIdx, 3)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _refreshedPoster.postValue(this)
                }
            }, {

            })
        )
    }

    fun setSortType(num: Int){
        _sortType.postValue(num)
    }

    fun setCategoryType(num: Int){
        category = num
    }

    fun setSubCategoryType(num: Int){
        _subCategory.postValue(num)
    }

    fun setIsUnivClub(bool: Boolean){
        _isUnivClub.postValue(bool)
    }


    fun navigate(idx: Int, position: Int) {

        refreshedPosterPosition = position
        refreshedPosterIdx = idx

    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "AllCategoryViewModel"
    }

}