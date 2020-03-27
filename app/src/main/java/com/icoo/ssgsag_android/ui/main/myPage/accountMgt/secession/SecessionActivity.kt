package com.icoo.ssgsag_android.ui.main.myPage.accountMgt.secession

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_secession.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecessionActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secession)

        init()
        setOnClickListener()
    }

    private fun deleteWithdrawalResponse() {
        val deleteWithdrawalResponse =
            networkService.deleteWithdrawalResponse(SharedPreferenceController.getAuthorization(this))

        deleteWithdrawalResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                Log.e("user delete fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                if (response.isSuccessful) {
                    Log.e("회원탈퇴",response.body()!!.message)
                    if (response.body()!!.status.toString() == "204") {
                        SharedPreferenceController.deleteAuthorization(this@SecessionActivity)
                        SharedPreferenceController.deleteType(this@SecessionActivity)
                        toast("회원탈퇴 되었습니다.")
                        finishAffinity()
                    }

                }
            }
        })
    }

    private fun init() {
        //툴바
        setSupportActionBar(act_secession_tl_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setOnClickListener(){
        act_withdrawal_tv_delete.setSafeOnClickListener {
            deleteWithdrawalResponse()
        }
        act_withdrawal_tv_cancel.setSafeOnClickListener {
            finish()
        }
    }

}
