package com.icoo.ssgsag_android.util.extensionFunction

import java.util.*
import kotlin.collections.ArrayList

fun dayOfWeekExtension(date: ArrayList<Int>): String {
    val cal = Calendar.getInstance()
    cal.set(date[0], date[1] - 1, date[2])
    return getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK))
}

fun getDateInfo(date: String, type: Int): Int {
    when(type) {
        0 -> return date.substring(0, 4).toInt()
        1 -> return date.substring(5, 7).toInt()
        2 -> return date.substring(8, 10).toInt()
        else -> return 0
    }
}

fun getDayOfWeek(dayOfWeek: Int): String {
    when (dayOfWeek) {
        1 -> return "일"
        2 -> return "월"
        3 -> return "화"
        4 -> return "수"
        5 -> return "목"
        6 -> return "금"
        7 -> return "토"
        else -> return "error"
    }
}