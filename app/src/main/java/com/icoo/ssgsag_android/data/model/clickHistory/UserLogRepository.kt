package com.icoo.ssgsag_android.data.model.clickHistory

import com.google.gson.JsonObject
import io.reactivex.Single
import org.json.JSONObject

interface UserLogRepository {
    fun posterDetailWebSiteClick(body: JsonObject): Single<Int>
}