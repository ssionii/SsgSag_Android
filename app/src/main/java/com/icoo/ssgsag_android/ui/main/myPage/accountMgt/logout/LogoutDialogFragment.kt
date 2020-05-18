package com.icoo.ssgsag_android.ui.main.myPage.accountMgt.logout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.user.DeviceInfo
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.igaworks.v2.core.AdBrixRm
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_logout.*

class LogoutDialogFragment: androidx.fragment.app.DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_logout, container,false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        frag_logout_tv_yes.setSafeOnClickListener {
            SharedPreferenceController.setAuthorization(activity!!, "")
            SharedPreferenceController.deleteType(activity!!)

            AdBrixRm.login("")
            //activity!!.finishAffinity()
            ActivityCompat.finishAffinity(activity!!)
            val intent = Intent(activity!!, LoginActivity::class.java)
            intent.putExtra("isLogout", true)
            startActivity(intent)
            dismiss()
        }

        frag_logout_tv_cancel.setSafeOnClickListener {
            dismiss()
        }
    }
}
