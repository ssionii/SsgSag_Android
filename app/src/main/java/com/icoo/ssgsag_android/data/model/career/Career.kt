package com.icoo.ssgsag_android.data.model.career

data class Career(
    val careerIdx : Int,
    val userIdx : Int,
    val careerType : Int,
    val careerName : String,
    val careerContent: String,
    val careerDate1 : String,
    val careerDate2 : String,
    val careerRegDate : String
)