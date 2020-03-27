package com.icoo.ssgsag_android.data.model.subscribe

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.interest.Interest
import io.reactivex.Single

interface SubscribeRepository {
    fun getSubscribe(): Single<ArrayList<Subscribe>>
    fun subscribe(interestIdx: Int): Single<Int>
    fun unsubscribe(interestIdx: Int): Single<Int>
    fun getInterest(): Single<Interest>
    fun reInterest(body: JsonObject): Single<Int>
}