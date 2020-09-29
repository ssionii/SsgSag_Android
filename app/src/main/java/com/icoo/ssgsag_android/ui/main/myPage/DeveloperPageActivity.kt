package com.icoo.ssgsag_android.ui.main.myPage

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.ActivityDeveloperPageBinding
import com.icoo.ssgsag_android.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_developer_page.*
import kotlin.system.exitProcess

class DeveloperPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeveloperPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_developer_page)
        binding.lifecycleOwner = (this)

        init()

        val setting = getSharedPreferences("setting", 0)
        val url = setting.getString("URL", getString(R.string.base_url))
        if (url == getString(R.string.base_url)) {
            tv_api_url.text = getString(R.string.base_url)
            tv_mode.text = getString(R.string.my_page_service)
        } else {
            tv_api_url.text = getString(R.string.test_url)
            tv_mode.text = getString(R.string.my_page_dev)
        }
        tv_app_version.text = packageManager.getPackageInfo(packageName, 0).versionName
        tv_uuid.text = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                tv_firebase.text = task.result?.token
            })
        tv_token.text = SharedPreferenceController.getAuthorization(this)

        btn_service.setOnClickListener {
            tv_api_url.text = getString(R.string.base_url)
            tv_mode.text = getString(R.string.my_page_service)
        }

        btn_dev.setOnClickListener {
            tv_api_url.text = getString(R.string.test_url)
            tv_mode.text = getString(R.string.my_page_dev)
        }

        btn_save.setOnClickListener {
            val editor = this.getSharedPreferences("setting", MODE_PRIVATE).edit()
            editor.clear()
            editor.putString("URL", tv_api_url.text.toString())
            editor.apply()
        }

        btn_restart.setOnClickListener {
            finishAffinity()
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            exitProcess(0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun init() {
        setSupportActionBar(tb_title)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)
    }
}