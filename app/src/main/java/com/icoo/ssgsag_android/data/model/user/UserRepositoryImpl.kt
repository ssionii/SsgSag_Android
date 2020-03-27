package com.icoo.ssgsag_android.data.model.user

import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfo
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class UserRepositoryImpl(val api: NetworkService, val pref: PreferenceManager) :
    UserRepository {

    override fun getUserInfo(): Single<UserInfo> = api
        .userInfoResponse(pref.findPreference("TOKEN", ""))
        .map { it.data }
}