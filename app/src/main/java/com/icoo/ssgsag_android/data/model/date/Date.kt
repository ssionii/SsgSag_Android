package com.icoo.ssgsag_android.data.model.date

import androidx.lifecycle.LiveData
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import kotlin.collections.ArrayList

data class Date(
    var year : String,
    var month : String,
    var date : String,
    var dayOfWeek: Int,
    var isToDay: Boolean,
    var isCurrentMonth: Boolean,
    var isClickDay: Boolean,
    var schedule : ArrayList<Schedule> ? = null,
    var lines: Int
)