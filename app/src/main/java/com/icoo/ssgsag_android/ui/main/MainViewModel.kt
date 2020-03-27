package com.icoo.ssgsag_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel

class MainViewModel: BaseViewModel() {

    private val _isSsgSagFragment = MutableLiveData<Boolean>()
    val isSsgSagFragment: LiveData<Boolean> get() = _isSsgSagFragment

    fun setIsSsgSagFragment(bool: Boolean){
        _isSsgSagFragment.postValue(bool)
    }

    companion object {
        private val TAG = "MainViewModel"
    }
}