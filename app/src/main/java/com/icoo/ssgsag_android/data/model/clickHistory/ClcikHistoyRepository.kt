package com.icoo.ssgsag_android.data.model.clickHistory

import io.reactivex.Single

interface ClcikHistoyRepository {
    fun recordClickHistory(posterIdx: Int, type: Int): Single<Int>
}