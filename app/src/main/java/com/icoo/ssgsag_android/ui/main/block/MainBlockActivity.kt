package com.icoo.ssgsag_android.ui.main.block

import android.content.Intent
import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityMainBlockBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_contact.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainBlockActivity : BaseActivity<ActivityMainBlockBinding, MainBlockViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_main_block

    override val viewModel: MainBlockViewModel by viewModel()

    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        navigator()

    }

    private fun setButton(){

        viewDataBinding.actMainBlockCvRefresh.setSafeOnClickListener {
            viewModel.autoLogin(null)
        }

        viewDataBinding.actMainBlockTvMail.setSafeOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf("ssgsag.univ@gmail.com")
            email.putExtra(Intent.EXTRA_EMAIL, address)
            email.putExtra(Intent.EXTRA_SUBJECT, "문의사항: 학교 정보")
            startActivity(email)
        }
    }

    private fun navigator() {

        viewModel.activityToStart.observe(this, androidx.lifecycle.Observer { value ->
            val intent = Intent(this, value.first.java )
            startActivity(intent)
            finishAffinity()
        })
    }

}