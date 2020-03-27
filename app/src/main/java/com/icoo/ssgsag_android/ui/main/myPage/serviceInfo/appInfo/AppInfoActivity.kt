package com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.appInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.icoo.ssgsag_android.R
import kotlinx.android.synthetic.main.activity_app_info.*
import android.widget.TextView
import android.R.attr.versionCode
import android.content.pm.PackageManager
import android.content.pm.PackageInfo



class AppInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)
        var pInfo: PackageInfo? = null
        try {
            pInfo = packageManager.getPackageInfo(
                this.packageName, 0
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val versionName = pInfo!!.versionName

        val versionNameTextView = findViewById(R.id.act_app_info_tv_version) as TextView
        versionNameTextView.text = "버전 :$versionName"

        init()
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
        setSupportActionBar(act_app_info_tb_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
    }
}
