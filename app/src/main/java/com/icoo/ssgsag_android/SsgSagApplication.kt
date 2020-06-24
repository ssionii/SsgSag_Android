package com.icoo.ssgsag_android

import android.app.Application
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.icoo.ssgsag_android.di.appModule
import com.icoo.ssgsag_android.util.sdk.kakao.KakaoSDKAdapter
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.igaworks.v2.core.AdBrixRm
import com.igaworks.v2.core.application.AbxActivityHelper
import com.igaworks.v2.core.application.AbxActivityLifecycleCallbacks
import com.kakao.auth.KakaoSDK
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.app.Activity
import android.os.Bundle
import com.adjust.sdk.LogLevel
import io.realm.Realm
import io.realm.RealmConfiguration


class SsgSagApplication : Application() {

    lateinit var networkService: NetworkService

    companion object {
        lateinit var globalApplication: SsgSagApplication
        val ContestTitle = arrayListOf("주제", "지원자격", "시상내역")
        val ActTitle = arrayListOf("지원자격", "활동내역", "혜택")
        val ClubTitle = arrayListOf("활동분야", "모임시간", "혜택")
        val RecruitTitle = arrayListOf("모집분야", "지원자격", "근무지역")
        val eduTitle = arrayListOf("주제","내용/커리큘럼","일정/기간")
        val scholarTitle = arrayListOf("인원/혜택", "대상 및 조건","기타사항")

        var isRequiredUpdate = true

        lateinit var instance: SsgSagApplication

        fun getGlobalApplicationContext(): SsgSagApplication {
            return instance
        }

    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        globalApplication = this
        startKoin {
            androidLogger()
            androidContext(this@SsgSagApplication)
            modules(appModule)
        }

        buildNetwork()

        // Kakao Sdk 초기화
        KakaoSDK.init(KakaoSDKAdapter())

        AbxActivityHelper.initializeSdk(applicationContext , getString(R.string.adbrix_app_key), getString(R.string.adbrix_secret_key));
        registerActivityLifecycleCallbacks(AbxActivityLifecycleCallbacks())
        AdBrixRm.setEventUploadTimeInterval(AdBrixRm.AdBrixEventUploadTimeInterval.NORMAL)

        initAdjustSetting()


        Realm.init(this)
        val config : RealmConfiguration = RealmConfiguration.Builder()
            .name("appdb.realm")
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)

    }

    private fun initAdjustSetting(){
        val appToken = this.resources.getString(R.string.adjust_app_token)
        val environment = AdjustConfig.ENVIRONMENT_SANDBOX
        // val environment = AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(this, appToken, environment)
        config.setAppSecret(2, 1858703771, 1353181520, 555890878, 1372324175)
        config.setLogLevel(LogLevel.WARN)
        Adjust.onCreate(config)

        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())

    }

    private inner class AdjustLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity?) {}

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

        override fun onActivityResumed(activity: Activity) {
            Adjust.onResume()
        }

        override fun onActivityPaused(activity: Activity) {
            Adjust.onPause()
        }

        override fun onActivityStopped(activity: Activity?) {}

        override fun onActivityDestroyed(activity: Activity?) {}

    }

    private fun buildNetwork() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.base_url))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkService = retrofit.create(NetworkService::class.java)
    }
}