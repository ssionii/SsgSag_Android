package com.icoo.ssgsag_android.data.model.schedule

import com.icoo.ssgsag_android.data.model.date.Date


data class Schedule(
    val posterIdx : Int,
    val categoryIdx : Int,
    val subCategoryIdx: Int,
    val photoUrl: String,
    val thumbPhotoUrl: String,
    val posterName : String,
    val posterRegDate: String?,
    val posterStartDate: String?,
    val posterEndDate: String,
    val posterWebSite: String?,
    val documentDate: String,
    val contentIdx: Int,
    val keyword: String?,
    val isCompleted : Int,
    val isEnded : Int,
    var isFavorite : Int,
    val likeNum: Int,
    val swipeNum: Int,
    val favoriteNum: Int,
    val outline : String,
    val dday : Int,
    val date: Date?,
    var isAlone: Boolean? = true,
    var isLast: Boolean? = true,
    var selectType : Int? = 0 // 0: favorite, 1: selector
)