package com.icoo.ssgsag_android.data.model.event

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.base.NullDataResponse
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class EventRepositoryImpl (val api: NetworkService, val pref: PreferenceManager) : EventRepository {

    override fun postEvent(jsonObject: JsonObject): Single<NullDataResponse> = api
        .postEvent("application/json", pref.findPreference("TOKEN", ""), jsonObject)
        .map { it }

}