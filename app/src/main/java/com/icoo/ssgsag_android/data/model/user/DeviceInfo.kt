package com.icoo.ssgsag_android.data.model.user

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DeviceInfo : RealmObject(){
    var id : Int = 0
    var token =""
    var loginType: Int = 0 // 카카오: 0, 네이버: 1
    var uuid =""
}