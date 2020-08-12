package com.icoo.ssgsag_android.ui.main.block

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.login.LoginRepository
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.ui.signUp.SignupActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import io.reactivex.Observable
import org.json.JSONObject
import java.lang.IllegalStateException
import kotlin.reflect.KClass

class MainBlockViewModel(
    private val repository: LoginRepository,
    private val schedulerProvider: SchedulerProvider
): BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    fun autoLogin(param: String?){
        addDisposable(repository.autoLogin()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .doOnError {
                Log.e("auto Login error", it.message)
            }
            .subscribe({
                it.run{
                    if(this == 200){
                        Toast.makeText(context, R.string.main_block_toast_success, Toast.LENGTH_SHORT).show()
                        if(param == null){
                            _activityToStart.postValue(Pair(MainActivity::class, null))
                        }else{
                            val bundle = Bundle().apply { putString("param", param) }
                            _activityToStart.postValue(Pair(MainActivity::class, bundle))
                        }
                    } else if(this == 202){
                        Toast.makeText(context, R.string.main_block_toast_pending, Toast.LENGTH_SHORT).show()
                    }else if(this == 404 || this == 401 || this == 600){
                        SharedPreferenceController.setAuthorization(context, "")
                        _activityToStart.postValue(Pair(LoginActivity::class, null))
                    }
                }

            }) {
                Toast.makeText(context, "네트워크 상태를 확인해주세요.",Toast.LENGTH_SHORT).show()
                Log.e("auto login error:", it.message)
            })

    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }
}