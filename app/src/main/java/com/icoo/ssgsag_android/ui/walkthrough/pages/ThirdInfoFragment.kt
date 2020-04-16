package com.sopt.appjam_sggsag.Fragment.Info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.fragment_third_info.view.*
import org.jetbrains.anko.support.v4.act

class ThirdInfoFragment: androidx.fragment.app.Fragment(){


    private var thirdInfoFragment: View? = null

    lateinit var firebaseAnalytics : FirebaseAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thirdInfoFragment = inflater.inflate(R.layout.fragment_third_info, container, false)

        firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!)

        thirdInfoFragment!!.frag_third_info_ll_start.setSafeOnClickListener {
            SharedPreferenceController.setWalkthroughs(activity!!, "true")
            val intent = Intent(activity!!, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()

            logEVENT_NAME_CUSTOMIZED_FILTEREvent()
        }

        return thirdInfoFragment
    }

    private fun logEVENT_NAME_CUSTOMIZED_FILTEREvent() {
        val logger = AppEventsLogger.newLogger(activity!!)
        logger.logEvent("FIRST_OPEN")

        val params = Bundle()
        firebaseAnalytics.logEvent("FIRST_OPEN", params)

        val adjustEvent = AdjustEvent("lo92yh")
        Adjust.trackEvent(adjustEvent)
    }


}