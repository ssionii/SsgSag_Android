package com.icoo.ssgsag_android.ui.signUp.searchUniv

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityRegisterUnivBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterUnivActivity : BaseActivity<ActivityRegisterUnivBinding, SearchUnivViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_register_univ

    override val viewModel: SearchUnivViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setEditText()
    }

    private fun setButton(){

        viewDataBinding.actRegisterUnivClCancel.setSafeOnClickListener {
            finish()
        }


        viewDataBinding.actRegisterUnivClAdd.setSafeOnClickListener {
            val name = viewDataBinding.actRegisterUnivEtName.text.toString()
            if(name != "") {
                val result = Intent().apply {
                    putExtras(
                        bundleOf("univName" to name)
                    )
                }

                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }

    private fun setEditText(){
        viewDataBinding.actRegisterUnivEtName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty() || s.toString().contains(" ")){
                    viewDataBinding.actRegisterUnivClAdd.run{
                        backgroundColor = context.resources.getColor(R.color.grey_2)
                        isClickable = false
                    }
                }else{
                    viewDataBinding.actRegisterUnivClAdd.run{
                        backgroundColor = context.resources.getColor(R.color.ssgsag)
                        isClickable = true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}