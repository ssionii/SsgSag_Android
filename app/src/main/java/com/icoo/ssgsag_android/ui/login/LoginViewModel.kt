package com.icoo.ssgsag_android.ui.login

import android.app.Application
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
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.login.LoginRepository
import com.icoo.ssgsag_android.data.model.user.DeviceInfo
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.signUp.SignupActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import com.igaworks.v2.core.AdBrixRm
import io.reactivex.Observable
import io.realm.Realm
import org.json.JSONObject
import java.lang.IllegalStateException
import kotlin.reflect.KClass

class LoginViewModel (
    private val loginRepository: LoginRepository,
    private val schedulerProvider: SchedulerProvider
):BaseViewModel(){
    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    // 0: 업데이트 x, 1: 업데이트 o, 2: 서버 업데이트 중 (종료)
    private val _isUpdated = MutableLiveData<Int>()
    val isUpdated: LiveData<Int> get() = _isUpdated

    val realm = Realm.getDefaultInstance()
    private val context = SsgSagApplication.getGlobalApplicationContext()


    fun login(accessToken: String, loginType: Int, uuid: String?){
        val jsonObject = JSONObject()
        jsonObject.put("accessToken", accessToken)
        jsonObject.put("loginType", loginType)
        jsonObject.put("osType", 0)
        if(uuid != null){
            jsonObject.put("uuid",uuid)
        }else{
            jsonObject.put("uuid","")
        }
        jsonObject.put("registrationCode", SharedPreferenceController.getFireBaseInstanceId(context))

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(loginRepository.login(body)
            .subscribeOn(schedulerProvider.io())
            .flatMap {
                if (it.status == 200) {
                    Observable.just(it.data.token)
                } else if(it.status == 404) {
                    _activityToStart.postValue(Pair(SignupActivity::class, null))
                    Observable.empty()
                } else {
                    Log.e("status", it.status.toString())
                    Log.e("message", it.message)
                    Observable.error(IllegalStateException("Invalid Token"))
                }
            }
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({responseToken ->
                SharedPreferenceController.setAuthorization(context, responseToken)
                SharedPreferenceController.setType(context, "user")
                AdBrixRm.login(responseToken)
                _activityToStart.postValue(Pair(MainActivity::class, null))
            }) {
                Toast.makeText(context, "서버 점검 중입니다.",Toast.LENGTH_SHORT).show()
            })
    }

    fun autoLogin(param: String?){
        addDisposable(loginRepository.autoLogin()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run{
                    if(this == 200){
                        AdBrixRm.login(SharedPreferenceController.getAuthorization(context))
                        if(param == null){
                            _activityToStart.postValue(Pair(MainActivity::class, null))
                        }else{
                            val bundle = Bundle().apply { putString("param", param) }
                            _activityToStart.postValue(Pair(MainActivity::class, bundle))
                        }
                    } else if(this == 404){
                        SharedPreferenceController.setAuthorization(context, "")
                        _activityToStart.postValue(Pair(LoginActivity::class, null))
                    }
                }

            }) {
                Toast.makeText(context, "네트워크 상태를 확인해주세요.",Toast.LENGTH_SHORT).show()
                Log.e("auto login error:", it.message)
            })

    }

    fun getUpdate(){
        addDisposable(loginRepository.getUpdate()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .doOnError {
                Toast.makeText(context, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                _isUpdated.setValue(2)
            }
            .subscribe({
                it.run{
                    if(this.status == 200 && (this.data == 0)){
                        _isUpdated.setValue(0)
                    }else if(this.data == 1){
                        _isUpdated.setValue(1)
                    }else if(this.data == 2) {
                        Toast.makeText(context, "업데이트 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show()
                        _isUpdated.setValue(2)
                    }
                }

            }) {
                Log.e("getUpdate fail", it.toString())
            })
    }


    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "LoginViewModel"
    }
}