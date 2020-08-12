package com.icoo.ssgsag_android.data.model.signUp

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.base.BooleanResponse
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class SignupRepositoryImpl (val api: NetworkService, val pref: PreferenceManager) : SignupRepository {

    override fun signup(body: JsonObject): Single<SignUpResponse> = api
        .postSignUpResponse("application/json", body)
        .map { it }

    override fun validateNickname(userNickname: String): Single<BooleanResponse> = api
        .validateNickname(userNickname)
        .map { it }

    override fun getUnivList(): Single<ArrayList<University>> = api
        .getUnivList()
        .map { it.data }
}