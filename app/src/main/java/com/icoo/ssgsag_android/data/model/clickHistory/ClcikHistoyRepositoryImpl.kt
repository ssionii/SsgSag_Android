package com.icoo.ssgsag_android.data.model.clickHistory

import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class ClcikHistoyRepositoryImpl(val api: NetworkService, val pref: PreferenceManager) : ClcikHistoyRepository {
    override fun recordClickHistory(posterIdx: Int, type: Int): Single<Int> = api
    .recordClickHistory(pref.findPreference("TOKEN", ""), posterIdx, type)
    .map { it.status }
}