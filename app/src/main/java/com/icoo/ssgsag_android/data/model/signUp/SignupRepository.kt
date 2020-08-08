package com.icoo.ssgsag_android.data.model.signUp

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.base.BooleanResponse
import io.reactivex.Single

interface SignupRepository {
    fun signup(body: JsonObject): Single<SignUpResponse>
    fun validateNickname(userNickname: String): Single<BooleanResponse>
    fun getUnivList() : Single<ArrayList<University>>
}