package com.icoo.ssgsag_android.ui.main.ssgSag.filter

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeFilter
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeRepository
import com.icoo.ssgsag_android.data.model.user.UserRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONArray
import org.json.JSONObject


class SsgSagFilterViewModel(
    private val repository: SubscribeRepository,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private var defaultFieldList = arrayListOf(
        Category(201, false, "#기획/아이디어"),
        Category(205, false, "#디자인"),
        Category(208, false, "#창업/스타트업"),
        Category(213, false, "#뷰티/패션"),
        Category(215, false, "#금융/경제"),
        Category(207, false, "#IT/SW"),
        Category(202, false, "#광고/마케팅"),
        Category(217, false, "#스포츠/레저"),
        Category(206, false, "#영상/컨텐츠"),
        Category(204, false, "#문학/시나리오"),
        Category(218, false, "#봉사활동"),
        Category(253, false, "#해외탐방")

    )

    private var defaultEnterpriseList = arrayListOf(
        Category(10000, false, "#대기업"),
        Category(60000, false, "#외국계기업"),
        Category(50000, false, "#공사/공기업"),
        Category(40000, false, "#스타트업"),
        Category(20000, false, "#중견기업"),
        Category(30000, false, "#중소기업")
    )

    private var defaultJobList = arrayListOf(
        Category(110, false, "#마케팅/광고"),
        Category(112, false, "#디자인"),
        Category(102, false, "#영업"),

        Category(109, false, "#엔지니어링/설계"),
        Category(101, false, "#경영/비즈니스"),
        Category(106, false, "#인사/교육"),

        Category(111, false, "#고객서비스/리테일"),
        Category(104, false, "#개발"),
        Category(103, false, "#제조/생산"),
        Category(107, false, "#미디어")
    )

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    private val _userInfo = MutableLiveData<ArrayList<Category>>()
    val userInfo : LiveData<ArrayList<Category>> get() =_userInfo

    var interest = arrayListOf<Int>()

    private val _fieldFilter = MutableLiveData<ArrayList<Category>>()
    val fieldFilter : LiveData<ArrayList<Category>> get() = _fieldFilter
    private val _enterpriseFilter = MutableLiveData<ArrayList<Category>>()
    val enterpriseFilter : LiveData<ArrayList<Category>> get() = _enterpriseFilter
    private val _jobFilter = MutableLiveData<ArrayList<Category>>()
    val jobFilter : LiveData<ArrayList<Category>> get() = _jobFilter

    init {
        getUserInfo()
        _fieldFilter.setValue(defaultFieldList)
        _enterpriseFilter.setValue(defaultEnterpriseList)
        _jobFilter.setValue(defaultJobList)
        getInterest()
    }

    fun getUserInfo(){
        addDisposable(userRepository.getUserInfo()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                val userInfo = arrayListOf(
                    Category(0, true, it.userUniv),
                    Category(0, true, it.userMajor)
                )
                _userInfo.postValue(userInfo)
            }, {
                it.printStackTrace()
            })
        )
    }

    fun getInterest() {
        addDisposable(repository.getInterest()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.interests?.run{
                    for(i in 0 until this.size){
                        if(this[i] > 200 && this[i] < 300){
                            defaultFieldList.forEach {
                                if(it.categoryIdx == this[i]){
                                    it.isChecked = true
                                }
                            }
                            _fieldFilter.setValue(defaultFieldList)
                        }else if(this[i] > 100 && this[i] < 200){
                            defaultJobList.forEach {
                                if(it.categoryIdx == this[i]){
                                    it.isChecked = true
                                }
                            }
                            _jobFilter.setValue(defaultJobList)
                        }else if(this[i] >= 10000 && this[i] <= 60000){
                            defaultEnterpriseList.forEach {
                                if(it.categoryIdx == this[i]){
                                    it.isChecked = true
                                }
                            }
                            _enterpriseFilter.setValue(defaultEnterpriseList)
                        }
                    }
                }
            }, {

            })
        )
    }


    // 확인 눌렀을 때
    fun combineInterest() : Boolean {

        var fieldFlag = false
        var enterpriseFlag = false
        var jobFlag = false


        for(i in 0 until fieldFilter.value!!.size){
            if(fieldFilter.value!![i].isChecked) {
                interest.add(fieldFilter.value!![i].categoryIdx)
                fieldFlag = true
            }
        }

        for(i in 0 until enterpriseFilter.value!!.size){
            if(enterpriseFilter.value!![i].isChecked) {
                interest.add(enterpriseFilter.value!![i].categoryIdx)
                enterpriseFlag = true
            }
        }

        for(i in 0 until jobFilter.value!!.size){
            if(jobFilter.value!![i].isChecked){
                interest.add(jobFilter.value!![i].categoryIdx)
                jobFlag = true
            }
        }


        if(!(jobFlag && fieldFlag && enterpriseFlag))
            return false
        else {
            reInterest()
            return true
        }
    }

    private fun reInterest() {

        val jsonObject = JSONObject()
        val jsonArray = JSONArray(interest)
        jsonObject.put("userInterest", jsonArray)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.reInterest(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                Log.e("reInterest status",it.toString())
            }, {
                it.printStackTrace()
            })
        )
    }

    fun clickInterest(position: Int, idx: Int) {
        if(idx > 200 && idx <300){
            defaultFieldList[position].isChecked = !defaultFieldList[position].isChecked
            _fieldFilter.setValue(defaultFieldList)
        }
        else if(idx >= 10000 && idx <= 60000){
            defaultEnterpriseList[position].isChecked = !defaultEnterpriseList[position].isChecked
            _enterpriseFilter.setValue(defaultEnterpriseList)
        }
        else if(idx > 100 && idx < 200){
            defaultJobList[position].isChecked = !defaultJobList[position].isChecked
            _jobFilter.setValue(defaultJobList)
        }else{
            Log.e("interest click", idx.toString())
        }
    }


    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "SsgSagFilterViewModel"
    }

}