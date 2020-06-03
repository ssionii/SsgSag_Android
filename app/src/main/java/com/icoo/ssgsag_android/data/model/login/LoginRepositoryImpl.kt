package com.icoo.ssgsag_android.data.model.login

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.base.IntResponse
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Observable
import io.reactivex.Single

class LoginRepositoryImpl (val api: NetworkService, val pref: PreferenceManager) : LoginRepository {

    private val context = SsgSagApplication.getGlobalApplicationContext()
    val version: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)


    override fun login(body: JsonObject): Observable<LoginResponse> = api
        .postLoginResponse("application/json", body)
        .map { it }

    override fun autoLogin(): Single<Int> {

        return api
            .postAutoLoginResponse("application/json", pref.findPreference("TOKEN", ""), SharedPreferenceController.getFireBaseInstanceId(context))
            .map { it.status }
    }

    override fun getUpdate(): Single<IntResponse> = api
        .getUpdateResponse("android", version.versionName)
        .map { it }

}