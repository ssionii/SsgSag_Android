package com.icoo.ssgsag_android.base

import androidx.lifecycle.ViewModel
import com.icoo.ssgsag_android.SsgSagApplication
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel(){

    val context = SsgSagApplication.getGlobalApplicationContext()
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}