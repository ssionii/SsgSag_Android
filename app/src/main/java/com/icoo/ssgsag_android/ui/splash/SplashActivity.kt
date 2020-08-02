package com.icoo.ssgsag_android.ui.splash

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import org.jetbrains.anko.startActivity
import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.kakao.util.helper.Utility.getPackageInfo
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.view.Gravity
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.ui.walkthrough.WalkthroughActivity
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.databinding.ActivitySplashBinding
import com.icoo.ssgsag_android.ui.login.LoginViewModel
import com.icoo.ssgsag_android.util.DialogPlusAdapter
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference


class SplashActivity : BaseActivity<ActivitySplashBinding, LoginViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_splash
    override val viewModel: LoginViewModel by viewModel()

    lateinit var mAdapter: DialogPlusAdapter
    private val thisActivity = this as Context
    var storeVersion: String? = null
    var deviceVersion: String? = null
    private var mBackgroundThread: BackgroundThread? = null

    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        if(SharedPreferenceController.getFireBaseInstanceId(this) == ""){
            getFirebaseInstanceId()
        }else{
            Log.e("fire token", SharedPreferenceController.getFireBaseInstanceId(this))
        }

//        setLottie()
        // logoutFirstTimeVersion231()
        getUpdateResponse()
        navigator()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBackgroundThread?.interrupt()
    }

    inner class BackgroundThread : Thread() {
        override fun run() {

                // 패키지 네임 전달
                //storeVersion = MarketVersionChecker.getMarketVersion(packageName)

                // 디바이스 버전 가져옴
                try {
                    if(!(mBackgroundThread!!.isInterrupted())) {
                        deviceVersion = packageManager.getPackageInfo(packageName, 0).versionName
                    }
                } catch (e: InterruptedException){
                    mBackgroundThread!!.interrupt()
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()

                } finally {

                }


            deviceVersionCheckHandler.sendMessage(deviceVersionCheckHandler.obtainMessage())
            // 핸들러로 메세지 전달
            }

    }

    private val deviceVersionCheckHandler =
        DeviceVersionCheckHandler(this)

    // 핸들러 객체 만들기
    private class DeviceVersionCheckHandler(splashActivity: SplashActivity) : Handler() {
        private val mainActivityWeakReference: WeakReference<SplashActivity>

        init {
            mainActivityWeakReference = WeakReference<SplashActivity>(splashActivity)
        }

        override fun handleMessage(msg: Message) {
            val activity = mainActivityWeakReference.get()
            if (activity != null) {
                activity.handleMessage(msg)
                // 핸들메세지로 결과값 전달
            }
        }
    }

    private fun handleMessage(msg: Message) {
        //핸들러에서 넘어온 값 체크

        /*
        if (storeVersion!!.compareTo(deviceVersion!!) > 0) {
            // 업데이트 필요
           update()

        } else {*/
            // 앱 처음 설치
            if(SharedPreferenceController.getWalkthroughs(thisActivity)== "false"){
                startActivity<WalkthroughActivity>()
                finish()
            } // 자동 로그인
            else if (SharedPreferenceController.getAuthorization(thisActivity)!=""
                &&SharedPreferenceController.getType(thisActivity) =="user") {
                // 카카오톡 포스터 공유를 통해 넘어온 경우, main을 거쳤다가 가기 위해서
                if(!(intent.action == Intent.ACTION_VIEW
                    && intent.scheme.startsWith("kakao", false))) {
                    viewModel.autoLogin(null)
                }else {
                    viewModel.autoLogin(intent.data.getQueryParameter("param"))
                }
            }
            else {
                startActivity<LoginActivity>()
                finish()
            }
        //}
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this@SplashActivity, Observer { value ->

            val intent = Intent(this, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            startActivity(intent)
            finishAffinity()
        })
    }


    private fun getUpdateResponse() {
        viewModel.getUpdate()
        viewModel.isUpdated.observe(this@SplashActivity, Observer { value ->

            Log.e("isUpdate 값", value.toString())

            if(value == 0) {
                mBackgroundThread = BackgroundThread()
                mBackgroundThread!!.start()
            }else if(value == 1){
                update()
            }
            else if(value == 2){
                finishAffinity()
            }
        })
    }


    private fun update(){
        val isNecessary = SsgSagApplication.isRequiredUpdate
        Log.e("isnecessary", isNecessary.toString())

        mAdapter = DialogPlusAdapter(this, false, 0, 0, -1)
        val builder =  DialogPlus.newDialog(this)

        if(isNecessary){

            builder.apply {

                setContentHolder(ViewHolder(R.layout.dialog_fragment_required_update))
                setCancelable(false)
                setGravity(Gravity.BOTTOM)

                setOnClickListener { dialog, view ->
                    if(view.id == R.id.dialog_frag_required_update_cv_update) {
                        dialog.dismiss()

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("market://details?id=$packageName")
                        startActivity(intent)
                    }

                }
            }
        }else {
            builder.apply {

                setContentHolder(ViewHolder(R.layout.dialog_fragment_selective_update))
                setCancelable(true)
                setGravity(Gravity.BOTTOM)

                setOnClickListener { dialog, view ->
                    if (view.id == R.id.dialog_frag_selective_update_cv_later) {
                        dialog.dismiss()

                        mBackgroundThread = BackgroundThread()
                        mBackgroundThread!!.start()
                    }else if(view.id == R.id.dialog_frag_selective_update_cv_update) {
                        dialog.dismiss()

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("market://details?id=$packageName")
                        startActivity(intent)
                    }

                }
            }
        }

        builder.apply{
            setAdapter(mAdapter)
            setOverlayBackgroundResource(R.color.dialog_background)
            setContentBackgroundResource(R.drawable.bg_white_top_39_radius)

            val horizontalDpValue = 32
            val topDpValue = 40
            val bottomDpValue = 48
            val d = resources.displayMetrics.density
            val horizontalMargin = (horizontalDpValue * d).toInt()
            val topMargin = (topDpValue * d).toInt()
            val bottomMargin = (bottomDpValue * d).toInt()

            setPadding(horizontalMargin, topMargin, horizontalMargin, bottomMargin)

        }
        builder.create().show()
    }


    fun getKeyHash(context: Context): String? {
        val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

        for (signature in packageInfo.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                Log.w(TAG, "Unable to get MessageDigest. signature=$signature", e)
            }

        }
        return null
    }

    private fun setLottie(){
        viewDataBinding.actSplashLav.run{
            this.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    this@run.pauseAnimation()
                }

                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
        }
    }

    private fun getFirebaseInstanceId(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if(!task.isSuccessful){
                    Log.w("getFireBase", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // get new instance id token
                val token = task.result?.token

                if(token != null) {
                    SharedPreferenceController.setFireBaseInstanceId(this, token)
                    Log.e("fire token", token)
                }
            })
    }

    private fun logoutFirstTimeVersion231(){
        if(!SharedPreferenceController.getIsLogout(thisActivity)){
            SharedPreferenceController.setAuthorization(thisActivity, "")
            SharedPreferenceController.deleteType(thisActivity)

            SharedPreferenceController.setIsLogout(thisActivity, true)
        }
    }



    companion object {
        private val TAG = "SplashActivity"
    }
}
