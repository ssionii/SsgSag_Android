package com.icoo.ssgsag_android.util

import java.util.*

fun getCoachTime(): String {
    val now = Calendar.getInstance()
    val year = now.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH) + 1 // Note: zero based!
    val day = now.get(Calendar.DAY_OF_MONTH)
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val minute = now.get(Calendar.MINUTE)
    val second = now.get(Calendar.SECOND)

    return String.format("%d-%02d-%02d %02d:%02d:%02d", year, month, day + 2, hour, minute, second)
}
