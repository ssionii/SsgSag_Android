package com.icoo.ssgsag_android.data.model.user

import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfo
import io.reactivex.Single

interface UserRepository {
    fun getUserInfo(): Single<UserInfo>
}