package com.icoo.ssgsag_android.data.model.login

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.base.IntResponse
import com.icoo.ssgsag_android.data.model.base.NullDataResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call

interface LoginRepository {

    fun login(body: JsonObject): Observable<LoginResponse>
    fun autoLogin() : Single<Int>
    fun getUpdate() : Single<IntResponse>
}