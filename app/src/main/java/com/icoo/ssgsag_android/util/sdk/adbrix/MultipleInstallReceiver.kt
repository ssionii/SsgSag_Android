package com.icoo.ssgsag_android.util.sdk.adbrix

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.igaworks.v2.core.AbxReceiver

class MultipleInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        //AdbrixRemastered 구글 인스톨 리시버 등록

        val abxReceiver = AbxReceiver()
        abxReceiver.onReceive(context, intent)

        //INSTALL_REFERRER 를 전달 받아야 하는 다른 리시버를 등록합니다.

    }
}