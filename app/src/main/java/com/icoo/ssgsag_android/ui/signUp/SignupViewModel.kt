package com.icoo.ssgsag_android.ui.signUp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.login.LoginRepository
import com.icoo.ssgsag_android.data.model.login.LoginToken
import com.icoo.ssgsag_android.data.model.signUp.SignupRepository
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeRepository
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.block.MainBlockActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import io.reactivex.Observable
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.lang.IllegalStateException
import kotlin.reflect.KClass

class SignupViewModel(
    private val signupRepository: SignupRepository,
    private val loginRepository: LoginRepository,
    private val schedulerProvider: SchedulerProvider
    ): BaseViewModel() {

    private val _loginToken = MutableLiveData<String>()
    val loginToken: LiveData<String> get() = _loginToken
    private val _isPossibleNickname = MutableLiveData<Boolean>()
    val isPossivleNickname: LiveData<Boolean> get() = _isPossibleNickname

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart



    private val context = SsgSagApplication.getGlobalApplicationContext()

    fun signup(body: JsonObject){

        addDisposable(signupRepository.signup(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it.status == 201 || it.status == 202){

                    SharedPreferenceController.setAuthorization(context, it.data!!.token)
                    SharedPreferenceController.setType(context, "user")
                    _loginToken.postValue(it.data!!.token)
                } else {
                    Toast.makeText(context, "입력 정보를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    Log.e("signup status:" , it.status.toString())
                    Log.e("signup message:" , it.message.toString())
                }
            }) {
                Toast.makeText(context, "네트워크 상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            })
    }

//    fun login(accessToken: String, loginType: Int){
//        val jsonObject = JSONObject()
//        jsonObject.put("accessToken", accessToken)
//        jsonObject.put("loginType", loginType)
//
//        val body = JsonParser().parse(jsonObject.toString()) as JsonObject
//
//        addDisposable(loginRepository.login(body)
//            .subscribeOn(schedulerProvider.io())
//            .flatMap {
//                if (it.status == 200 || it.status == 202) {
//                    Observable.just(it.data.token)
//                } else if(it.status == 404) {
//                    Observable.empty()
//                } else {
//                    Observable.error(IllegalStateException("Invalid Token"))
//                }
//            }
//            .observeOn(schedulerProvider.mainThread())
//            .subscribe({responseToken ->
//                SharedPreferenceController.setAuthorization(context, responseToken)
//                SharedPreferenceController.setType(context, "user")
//                Toast.makeText(context, "로그인 성공." ,Toast.LENGTH_SHORT).show();
//                _activityToStart.postValue(Pair(MainActivity::class, null))
//            }) {
//                Toast.makeText(context, "네트워크 연결 상태를 확인해주세요." ,Toast.LENGTH_SHORT).show();
//                Log.e("login fail message:", it.message)
//            })
//    }

    fun autoLogin(token : String, param: String?){
        addDisposable(loginRepository.autoLoginFromSignup(token)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                Log.e("signup 뒤 autoLogin", it.toString())
                it.run{
                    if(this == 200){
                        if(param == null){
                            _activityToStart.postValue(Pair(MainActivity::class, null))
                        }else{
                            val bundle = Bundle().apply { putString("param", param) }
                            _activityToStart.postValue(Pair(MainActivity::class, bundle))
                        }
                    } else if(this == 202) {
                        _activityToStart.postValue(Pair(MainBlockActivity::class, null))
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

    fun validateUserNickname(nickname: String){

        addDisposable(signupRepository.validateNickname(nickname)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it.status == 200){
                    it.data?.apply{
                        if(it.data)
                            _isPossibleNickname.postValue(true)
                        else
                            _isPossibleNickname.postValue(false)
                    }
                } else {
                    Log.e("validateNickname error:" , it.message.toString())
                }
            }) {
                Toast.makeText(context, "네트워크 상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            })
    }

    companion object {
        private val TAG = "SignupViewModel"
    }
}