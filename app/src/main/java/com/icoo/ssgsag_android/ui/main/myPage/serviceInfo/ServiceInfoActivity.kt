package com.icoo.ssgsag_android.ui.main.myPage.serviceInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.appInfo.AppInfoActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.license.LicenseActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.privacy.PrivacyActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.term.TermActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_service_info.*
import org.jetbrains.anko.startActivity

class ServiceInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_info)
        init()
        setOnClickListener()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun init() {
        //툴바
        setSupportActionBar(act_service_info_tl_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
    }

    fun setOnClickListener() {
        act_service_info_rl_app_info.setSafeOnClickListener {
            startActivity<AppInfoActivity>()
        }
        act_service_info_rl_term.setSafeOnClickListener {
            startActivity<TermActivity>()
        }
        act_service_info_rl_privacy.setSafeOnClickListener {
            startActivity<PrivacyActivity>()
        }
        act_service_info_rl_open_license.setSafeOnClickListener {
            startActivity<LicenseActivity>()
        }
    }
}
