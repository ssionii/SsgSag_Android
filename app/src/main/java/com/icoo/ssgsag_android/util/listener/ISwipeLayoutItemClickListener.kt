package com.icoo.ssgsag_android.util.listener

import com.icoo.ssgsag_android.data.model.date.Date

interface ISwipeLayoutItemClickListener {
    fun onItemClick()
    fun onItemClick(date: Date, isCalRefresh: Boolean)
    fun onViewClick(posterIdx : Int)
}