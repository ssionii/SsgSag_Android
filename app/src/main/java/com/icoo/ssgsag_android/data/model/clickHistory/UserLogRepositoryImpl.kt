package com.icoo.ssgsag_android.data.model.clickHistory

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single
import org.json.JSONObject

class UserLogRepositoryImpl(val api: NetworkService, val pref: PreferenceManager) : UserLogRepository {

    override fun posterDetailWebSiteClick(body: JsonObject): Single<Int> = api
    .recordClickHistory(pref.findPreference("TOKEN", ""),"application/json", body)
    .map { it.status }
}