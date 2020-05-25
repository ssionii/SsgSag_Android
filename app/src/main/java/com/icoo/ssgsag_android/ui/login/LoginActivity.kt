package com.icoo.ssgsag_android.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.user.DeviceInfo
import com.icoo.ssgsag_android.databinding.ActivityLoginBinding
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(){
    override val layoutResID: Int
        get() = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModel()

    internal val disposable = CompositeDisposable()
    val realm = Realm.getDefaultInstance()

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    val RECORD_REQUEST_CODE = 1000

    // client 정보
    private val OAUTH_CLIENT_ID = "jsLE7cET_smOvsjkJYSc"
    private val OAUTH_CLIENT_SECRET = "uAnTlBnsd8"
    private val OAUTH_CLIENT_NAME = "네이버 아이디로 로그인"

    private var naverOAuthLoginInstance: OAuthLogin? = null
    private var naverContext: Context? = null

    private var kakaoCallback: SessionCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel
        navigator()

        Log.e("autoLoginFail", SharedPreferenceController.getAuthorization(this))


        var uuid = getUuid()
        if(uuid != "") { // 권한을 받은 경우
            val _uuid = uuid
            setDeviceInfoUuid(_uuid)
        }else{

//            toast("해당 앱을 이용할 수 없는 기기입니다.")
        }

        // naver
        naverContext = this
        initNaverData()
        setButton()
        this.title = "OAuthLoginSample Ver." + OAuthLogin.getVersion()

        // kakao
        Session.getCurrentSession().close()
        kakaoCallback = SessionCallback()
        Session.getCurrentSession().addCallback(kakaoCallback)
        Session.getCurrentSession().checkAndImplicitOpen()

    }

    // 카카오
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(kakaoCallback)
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            var keys = ArrayList<String>()
            keys.add("properties.nickname")
            keys.add("properties.profile_image")
            keys.add("kakao_account.adminEmail")

            UserManagement.getInstance().me(keys, object : MeV2ResponseCallback() {
                override fun onFailure(errorResult: ErrorResult) {
                    val message = "failed to get user info. msg=$errorResult"
                    Logger.d(message)
                }

                override fun onSessionClosed(errorResult: ErrorResult) {
                }

                override fun onSuccess(response: MeV2Response) {

                    setDeviceInfoToken(Session.getCurrentSession().accessToken)
                    setDeviceInfoLoginType(0)

                    val realmDeviceInfo = realm.where(DeviceInfo::class.java).equalTo("id", 1 as Int).findFirst()
                    viewModel.login(realmDeviceInfo!!.token, realmDeviceInfo!!.loginType, realmDeviceInfo!!.uuid)

                }
            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.e(exception)
            }
        }

    }


    // uuid 받아오기
    fun getUuid(): String {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@LoginActivity,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                RECORD_REQUEST_CODE
            )

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return ""
            } else{
                if (Build.VERSION.SDK_INT >= 29 || checkTabletDeviceWithScreenSize(this)) {
                    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
                }else{
                    return (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 29) {
            return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        }else{
            return (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
        }
    }

    private fun checkTableDevice() : Boolean{
        try{
            val ism = Runtime.getRuntime().exec("getprop ro.build.characteristics").getInputStream()
            val bts = byteArrayOf()
            ism.read(bts)
            ism.close()

            var isTablet = String(bts).toLowerCase().contains("tablet")
            return isTablet
        }catch(t : Throwable){
            t.printStackTrace()
            return false
        }
    }

    private fun checkTabletDeviceWithScreenSize(context: Context) : Boolean {
        val device_large = ((context.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)

        if (device_large) {
            val metrics = DisplayMetrics()
            val activity = context as Activity
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics)

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true
            }
        }
        return false
    }

    // 네이버
    private fun initNaverData() {
        naverOAuthLoginInstance = OAuthLogin.getInstance()

        naverOAuthLoginInstance!!.showDevelopersLog(true)
        naverOAuthLoginInstance!!.init(naverContext!!, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)
    }

    private val naverOAuthLoginHandler = @SuppressLint("HandlerLeak")
    object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                setDeviceInfoToken(naverOAuthLoginInstance!!.getAccessToken(naverContext))
                setDeviceInfoLoginType(1)

                val realmDeviceInfo = realm.where(DeviceInfo::class.java).equalTo("id", 1 as Int).findFirst()

                viewModel.login(realmDeviceInfo!!.token, realmDeviceInfo!!.loginType, realmDeviceInfo!!.uuid)
            } else {
                val errorCode = naverOAuthLoginInstance!!.getLastErrorCode(naverContext).code
                val errorDesc = naverOAuthLoginInstance!!.getLastErrorDesc(naverContext)

                Log.e("naver login error code", errorCode)
                Log.e("naver login error desc", errorDesc)

                Toast.makeText(this@LoginActivity, "네이버 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setButton(){
        viewDataBinding.actLoginCvNaver.setOnClickListener {
            viewDataBinding.actLoginBtnNaver.performClick()
        }

        viewDataBinding.actLoginBtnNaver.setOnClickListener{
            naverOAuthLoginInstance!!.startOauthLoginActivity(this ,naverOAuthLoginHandler)
        }

        viewDataBinding.actLoginCvKakao.setOnClickListener {
            viewDataBinding.actLoginBtnKakao.performClick()
        }
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(this, value.first.java)
            startActivity(intent)
            finishAffinity()
        })
    }

    fun setDeviceInfoToken(token: String){
        realm.beginTransaction()
        val realmDeviceInfo = realm.where(DeviceInfo::class.java).equalTo("id", 1 as Int).findFirst()
        if(realmDeviceInfo != null){
            realmDeviceInfo.token = token
        }else{
            Log.e("Realm setToken error","id가 1인 realmDeviceInfo가 없습니다.")
        }
        realm.commitTransaction()
    }

    fun setDeviceInfoLoginType(loginType: Int){
        realm.beginTransaction()
        val realmDeviceInfo = realm.where(DeviceInfo::class.java).equalTo("id", 1 as Int).findFirst()
        if(realmDeviceInfo != null){
            realmDeviceInfo.loginType = loginType
        }else{
            Log.e("Realm loginType error","id가 1인 realmDeviceInfo가 없습니다.")
        }
        realm.commitTransaction()
    }

    fun setDeviceInfoUuid(uuid : String){
        realm.beginTransaction()
        val realmDeviceInfo = realm.where(DeviceInfo::class.java).equalTo("id", 1 as Int).findFirst()
        if(realmDeviceInfo != null){
            realmDeviceInfo.uuid = uuid
        }else{
            val realmObject = realm.createObject(DeviceInfo::class.java)
            realmObject.apply{
                this.id = 1
                this.uuid = uuid
            }

            Log.e("Realm uuid","realm 객체 새로 생성")
        }
        realm.commitTransaction()
    }
}