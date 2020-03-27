package com.sopt.appjam_sggsag.Fragment.Info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.fragment_third_info.view.*

class ThirdInfoFragment: androidx.fragment.app.Fragment(){


    private var thirdInfoFragment: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thirdInfoFragment = inflater.inflate(R.layout.fragment_third_info, container, false)

        thirdInfoFragment!!.frag_third_info_ll_start.setSafeOnClickListener {
            SharedPreferenceController.setWalkthroughs(activity!!, "true")
            val intent = Intent(activity!!, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        return thirdInfoFragment
    }

}