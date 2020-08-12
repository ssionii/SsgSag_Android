package com.icoo.ssgsag_android.ui.main.block

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.user.DeviceInfo
import com.icoo.ssgsag_android.databinding.ActivityMainBlockBinding
import com.icoo.ssgsag_android.databinding.ActivityRegisterUnivBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.signUp.searchUniv.SearchUnivViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.realm.Realm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Observer


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