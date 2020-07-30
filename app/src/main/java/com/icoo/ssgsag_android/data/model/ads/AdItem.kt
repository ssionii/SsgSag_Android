package com.icoo.ssgsag_android.data.model.ads

data class AdItem(
    val adIdx : Int,
    val orderNum : Int,
    val contentIdx : Int,
    val contentTitle: String,
    val contentSubtitle : String,
    val photoUrl : String,
    val adUrl : String?,
    val openEndDate : String,
    val regDate : String,
    val dday : Int,
    val isSave : Int,
    val categoryIdx : Int,
    val categoryName: String,
    val categoryOrder : Int,
    val categoryDetail : String
)