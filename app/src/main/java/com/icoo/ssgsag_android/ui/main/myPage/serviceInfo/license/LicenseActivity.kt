package com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.license

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.myPage.DeveloperPageActivity
import kotlinx.android.synthetic.main.activity_license.*


class LicenseActivity : AppCompatActivity() {
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)
        init()

        act_license_tl_toolbar.setOnClickListener {
            count++
            when (count) {
                10 -> {
                    val intent = Intent(this, DeveloperPageActivity::class.java)
                    startActivity(intent)
                }
                9 -> {
                    Toast.makeText(this, "개발자 모드 변경까지 1번 남았어요!", Toast.LENGTH_SHORT).show()
                }
                8 -> {
                    Toast.makeText(this, "개발자 모드 변경까지 2번 남았어요!", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
        setSupportActionBar(act_license_tl_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        count = 0
    }
}
