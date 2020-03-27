package com.icoo.ssgsag_android.data.model.event
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.base.NullDataResponse
import io.reactivex.Single

interface EventRepository {
    fun postEvent(jsonObject: JsonObject): Single<NullDataResponse>

}