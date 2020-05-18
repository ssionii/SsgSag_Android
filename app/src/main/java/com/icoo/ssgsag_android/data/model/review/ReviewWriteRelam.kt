package com.icoo.ssgsag_android.data.model.review

import io.realm.RealmObject

open class ReviewWriteRelam : RealmObject(){
        var id = 0

        var clubName: String = ""
        var univOrLocation = ""

        var fieldName = ""

        var startYear = ""
        var startMonth = ""
        var endYear =""
        var endMonth =""

        // 점수 평가
        var score0 = 0
        var score1 = 0
        var score2 = 0
        var score3 = 0
        var score4 = 0

        // 간단 평가
        var oneLine = ""
        var advantage = ""
        var disadvantage = ""
        var honeyTip =""

        var categoryList = ""

}