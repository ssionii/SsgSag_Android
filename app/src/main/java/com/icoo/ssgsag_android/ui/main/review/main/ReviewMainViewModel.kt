package com.icoo.ssgsag_android.ui.main.review.main

import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.ui.main.review.ReviewViewModel
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class ReviewMainViewModel(
    val repository: ReviewViewModel,
    val schedulerProvider: SchedulerProvider
): BaseViewModel(){

    init {

    }
}