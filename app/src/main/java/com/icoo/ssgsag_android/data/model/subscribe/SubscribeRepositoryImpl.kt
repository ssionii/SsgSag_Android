package com.icoo.ssgsag_android.data.model.subscribe

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.interest.Interest
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class SubscribeRepositoryImpl(val api: NetworkService, val pref: PreferenceManager) : SubscribeRepository {
    override fun getSubscribe(): Single<ArrayList<Subscribe>> = api
        .getSubscribeResponse(pref.findPreference("TOKEN", ""))
        .map { it.data }

    override fun subscribe(interestIdx: Int): Single<Int> = api
        .subscribeResponse(pref.findPreference("TOKEN", ""), interestIdx)
        .map { it.status }

    override fun unsubscribe(interestIdx: Int): Single<Int> = api
        .unsubscribeResponse(pref.findPreference("TOKEN", ""), interestIdx)
        .map { it.status }

    override fun getInterest(): Single<Interest> = api
        .getInterestResponse(pref.findPreference("TOKEN", ""))
        .map{it.data}

    override fun reInterest(body: JsonObject): Single<Int> = api
        .reInterest(pref.findPreference("TOKEN", ""), "application/json", body)
        .map { it.status }

}