package com.icoo.ssgsag_android.data.model.signUp


data class UniversityListResponse(
    val status : Int,
    val message : String,
    val data : ArrayList<University>
)

data class University(
    val head : String,
    val univInfoList: ArrayList<UnivInfo>
)

data class UnivInfo(
    val univName : String,
    val majorList : ArrayList<String>
)
