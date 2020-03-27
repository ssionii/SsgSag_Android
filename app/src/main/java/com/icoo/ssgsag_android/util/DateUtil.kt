package com.icoo.ssgsag_android.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    val yearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
    val monthFormat = SimpleDateFormat("MM", Locale.KOREA)
    val dateFormat = SimpleDateFormat("dd", Locale.KOREA)

    private fun setGradeAndAdmissionYear() {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH) + 1 // Note: zero based!
        val day = now.get(Calendar.DAY_OF_MONTH)
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)
        val second = now.get(Calendar.SECOND)
        val millis = now.get(Calendar.MILLISECOND)

        System.out.printf("%d-%02d-%02d %02d:%02d:%02d.%03d", year, month, day, hour, minute, second, millis)
    }
}