package com.icoo.ssgsag_android.ui.main.myPage.pushAlarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.R
import kotlinx.android.synthetic.main.activity_push_alarm.*


class PushAlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_alarm)
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
        setSupportActionBar(act_push_alarm_tl_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        //뷰

        if (SharedPreferenceController.getReceivableCard(this) == "true") {
            act_push_switch_card.isChecked = true
        } else {
            act_push_switch_card.isChecked = false
        }

        act_push_switch_card.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked) {
                SharedPreferenceController.setReceivableCard(this, "true")
            } else {
                SharedPreferenceController.setReceivableCard(this, "false")
            }
        }

        if (SharedPreferenceController.getReceivableTodo(this) == "true") {
            act_push_switch_todo.isChecked = true
        } else {
            act_push_switch_todo.isChecked = false
        }

        act_push_switch_todo.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked) {
                SharedPreferenceController.setReceivableTodo(this, "true")
            } else {
                SharedPreferenceController.setReceivableTodo(this, "false")
            }
        }

    }
}
