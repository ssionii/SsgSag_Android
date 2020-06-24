package com.icoo.ssgsag_android.ui.main.allPosters

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass

class AllPostersViewModel(
    private val repository: PosterRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private var categorySet = arrayListOf(
        Category(0, true, ""),
        Category(1, false, ""),
        //Category(2, false),
        Category(4, false, ""),
        Category(7, false, ""),
        Category(5, false, "")
        //Category(8, false)
    )

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private var _categorySort = MutableLiveData<ArrayList<Category>>()
    val categorySort: LiveData<ArrayList<Category>> get() = _categorySort
    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart
    private var _posterList = MutableLiveData<ArrayList<ArrayList<PosterDetail>>>()
    val posterList: LiveData<ArrayList<ArrayList<PosterDetail>>> get() = _posterList

    private var _clubPosterList = MutableLiveData<ArrayList<PosterDetail>>()
    val clubPosterList: LiveData<ArrayList<PosterDetail>> get() = _clubPosterList
    private var _actPosterList = MutableLiveData<ArrayList<PosterDetail>>()
    val actPosterList: LiveData<ArrayList<PosterDetail>> get() = _actPosterList
    private var _contestPosterList = MutableLiveData<ArrayList<PosterDetail>>()
    val contestPosterList: LiveData<ArrayList<PosterDetail>> get() = _contestPosterList
    private var _internPosterList = MutableLiveData<ArrayList<PosterDetail>>()
    val internPosterList: LiveData<ArrayList<PosterDetail>> get() = _internPosterList
    private var _educationPosterList = MutableLiveData<ArrayList<PosterDetail>>()
    val educationPosterList: LiveData<ArrayList<PosterDetail>> get() = _educationPosterList
    private var _etcPosterList = MutableLiveData<ArrayList<PosterDetail>>()
    val etcPosterList: LiveData<ArrayList<PosterDetail>> get() = _etcPosterList



    private val _category = MutableLiveData<Int>()
    val category: LiveData<Int> get() = _category
    private val _sortType = MutableLiveData<Int>()
    val sortType: LiveData<Int> get() = _sortType

    init{
        _category.setValue(0)
        _sortType.setValue(0)
        _categorySort.setValue(categorySet)

        getClubPosters()
        getActPosters()
        getContestPosters()
        getInternPosters()
        getEducationPosters()
        getEtcPosters()
    }

    fun getClubPosters(){
        addDisposable(
            repository.getWhatPosters(2)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    it.run {
                        _clubPosterList.postValue(this)

                    }
                }, {
                    it.printStackTrace()
                })
        )
    }


    fun getActPosters(){

        addDisposable(
            repository.getWhatPosters(1)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    it.run {
                        _actPosterList.postValue(this)
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun getContestPosters(){
        addDisposable(
            repository.getWhatPosters(0)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    it.run {
                        _contestPosterList.postValue(this)
                    }
                }, {

                })
        )
    }

    fun getInternPosters(){
        addDisposable(
            repository.getWhatPosters(4)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    it.run {
                        _internPosterList.postValue(this)
                    }
                }, {

                })
        )
    }

    fun getEducationPosters(){
        addDisposable(
            repository.getWhatPosters(7)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    it.run {
                        _educationPosterList.postValue(this)
                    }
                }, {

                })
        )
    }

    fun getEtcPosters(){
        addDisposable(
            repository.getWhatPosters(5)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    it.run {
                        _etcPosterList.postValue(this)
                    }
                }, {

                })
        )
    }

    fun getPosters(){

        /*
        addDisposable(repository.getAllPostersCategory(this.categoryList.value!!, this.sortType.value!!, 0)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _posters.setValue(this)
                }
            }, {

            })
        )*/

        var index = 0
        var count = 0
        var tempList = ArrayList<ArrayList<PosterDetail>>()
        while(index > 7) {

            if(index == 2)
                index = 4
            else if(index == 6)
                index = 7

            addDisposable(
                repository.getWhatPosters(index)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.mainThread())
                    .subscribe({
                        it.run {
                            tempList[count++] = this
                        }
                    }, {

                    })
            )

            index++

        }

        _posterList.postValue(tempList)
    }


    fun checkCate(categoryIdx : Int){
        for(i in 0..categorySet.size -1)
            categorySet[i].isChecked = false

        if(categoryIdx < 3) {
            categorySet[categoryIdx].isChecked = true
        }else if(categoryIdx == 4){
            categorySet[2].isChecked = true
        }else if(categoryIdx == 7){
            categorySet[3].isChecked = true
        } else if(categoryIdx == 5){
            categorySet[categorySet.size -1 ].isChecked = true
        }

        _categorySort.postValue(categorySet)
        _category.setValue(categoryIdx)

        getPosters()

    }

    fun checkSort(sortType: Int){
        _sortType.setValue(sortType)

        getPosters()
    }

    fun navigate(idx: Int) {
        val bundle = Bundle().apply {
            putInt("Idx", idx)
            putString("from","main")
            putString("from","what")
        }
        _activityToStart.postValue(Pair(CalendarDetailActivity::class, bundle))
    }

}