package com.icoo.ssgsag_android.ui.main.review.event

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.ActivityReviewEventBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_review_event.*
import org.jetbrains.anko.backgroundColor
import org.json.JSONObject
import java.util.regex.Pattern
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewEventActivity : BaseActivity<ActivityReviewEventBinding, ReviewEventViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_review_event
    override val viewModel: ReviewEventViewModel by viewModel()

    var clubIdx = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clubIdx = intent.getIntExtra("clubIdx", -1)
        viewDataBinding.vm = viewModel

        setButton()
        setEditTextChange()
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setEditTextChange() {
        viewDataBinding.actReviewEventEtName.onChange { onDataCheck() }
        viewDataBinding.actReviewEventEtPhone.onChange { onDataCheck() }
    }

    private fun setButton(){
        viewDataBinding.actReviewEventClDone.setSafeOnClickListener {
           postEventInfo()

        }

        viewDataBinding.actReviewEventIvCancel.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actReviewEventCbPersonalInfo.setSafeOnClickListener {
            onDataCheck()
        }

        viewDataBinding.actReviewEventLlPersonalInfo.setOnClickListener {
            viewDataBinding.actReviewEventCbPersonalInfo.isChecked = !viewDataBinding.actReviewEventCbPersonalInfo.isChecked
            onDataCheck()
        }


        viewDataBinding.actReviewEventCbPushAlarm.setSafeOnClickListener {
            if((it as CheckBox).isChecked){
                SharedPreferenceController.setReceivableCard(this, "false")
            }else{
                SharedPreferenceController.setReceivableCard(this, "true")
            }
        }

        viewDataBinding.actReviewEventLlPushAlarm.setOnClickListener {
            viewDataBinding.actReviewEventCbPushAlarm.isChecked = !viewDataBinding.actReviewEventCbPushAlarm.isChecked
        }
    }

    private fun onDataCheck(){

        if(viewDataBinding.actReviewEventEtName.text.isEmpty()|| viewDataBinding.actReviewEventEtPhone.text.isEmpty()
            || !Pattern.matches("^[0-9]*\$", viewDataBinding.actReviewEventEtPhone.text)
            || !viewDataBinding.actReviewEventCbPersonalInfo.isChecked){
            viewDataBinding.actReviewEventClDone.apply{
                backgroundColor = Color.parseColor("#aaaaaa")
                isClickable = false
            }

        }else{
            viewDataBinding.actReviewEventClDone.apply{
                backgroundColor = Color.parseColor("#656ef0")
                isClickable = true
            }
        }
    }

    private fun postEventInfo(){

        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("eventType",0)
        jsonObject.put("userName", viewDataBinding.actReviewEventEtName.text)
        jsonObject.put("userPhone", viewDataBinding.actReviewEventEtPhone.text)
        jsonObject.put("objectIdx", clubIdx)


        val body: JsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        viewModel.postEvent(body)
        finish()
    }
}