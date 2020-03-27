package com.icoo.ssgsag_android.ui.main.myPage.career.careerEdit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.icoo.ssgsag_android.R
import kotlinx.android.synthetic.main.activity_career_edit.*
import org.jetbrains.anko.*
import java.util.*
import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.ui.main.myPage.career.CareerActivity
import com.icoo.ssgsag_android.ui.main.myPage.career.datePicker.CustomDatePickerDialog
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CareerEditActivity : AppCompatActivity() {
    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }
    private var mIdx: Int ?= null
    private var mSection: Int ?= null
    private var mIsAdd: Boolean ?= null //true면 추가상태, false면 수정상태

    private val mTitle = arrayListOf("대외활동", "수상내역", "자격증")

    private val mCalendar = Calendar.getInstance()
    private val mYear = mCalendar.get(Calendar.YEAR)
    private val mMonth = mCalendar.get(Calendar.MONTH)

    private var mStartYear: String = mYear.toString()
    private var mStartMonth: String = (mMonth + 1).toString()

    private var mEndYear: String = mYear.toString()
    private var mEndMonth: String = (mMonth + 1).toString()
    lateinit var dateSet:DatePickerDialog.OnDateSetListener

    private fun showDatePicker(state: Int) {
        val customDatePickerDialog = CustomDatePickerDialog()
        when (state) {
            0 -> {
                dateSet = DatePickerDialog.OnDateSetListener { picker, year, monthOfYear, dayOfMonth ->
                    mStartYear = year.toString()
                    mStartMonth = monthOfYear.toString()
                    act_career_detail_date_start.text = mStartYear + "년 " + mStartMonth + "월 "
                }
                customDatePickerDialog.setListener(dateSet)
                customDatePickerDialog.setValue(mStartYear.toInt(), mStartMonth.toInt())
                customDatePickerDialog.show(supportFragmentManager, "StartDate")
            }
            1 -> {
                dateSet = DatePickerDialog.OnDateSetListener { picker, year, monthOfYear, dayOfMonth ->
                    mEndYear = year.toString()
                    mEndMonth = monthOfYear.toString()
                    act_career_detail_date_end.text = mEndYear + "년 " + mEndMonth + "월 "
                }
                customDatePickerDialog.setListener(dateSet)
                customDatePickerDialog.setValue(mEndYear.toInt(), mEndMonth.toInt())
                customDatePickerDialog.show(supportFragmentManager, "EndDate")
            }
            2 -> {
                dateSet = DatePickerDialog.OnDateSetListener { picker, year, monthOfYear, dayOfMonth ->
                    mStartYear = year.toString()
                    mStartMonth = monthOfYear.toString()
                    act_career_detail_date_start_only.text = mStartYear + "년 " + mStartMonth + "월 "
                }
                customDatePickerDialog.setListener(dateSet)
                customDatePickerDialog.setValue(mStartYear.toInt(), mStartMonth.toInt())
                customDatePickerDialog.show(supportFragmentManager, "StartDateOnly")
            }
            else -> { }
        }

    }

    private fun setOnClickListener() {
        act_career_detail_date_start.setSafeOnClickListener {
            showDatePicker(0)
        }
        act_career_detail_date_end.setSafeOnClickListener{
            showDatePicker(1)
        }
        act_career_detail_date_start_only.setSafeOnClickListener {
            showDatePicker(2)
        }
        act_career_detail_save_save.setSafeOnClickListener {
            sendData()
        }
    }

    private fun sendData() {
        var title: String = act_career_detail_et_title.text.toString()
        var notes: String = act_career_detail_et_note.text.toString()

        val intent = Intent(this@CareerEditActivity, CareerActivity::class.java)
        intent.putExtra("view", mSection)

        if(mSection == 0) {
            if (title.isNotEmpty() && notes.isNotEmpty() && (mStartYear.toInt() < mEndYear.toInt() || (mStartYear.toInt() == mEndYear.toInt() && mStartMonth.toInt() <= mEndMonth.toInt()))) {
                if(mIsAdd!!)
                    postCareerAddResponse()
                else
                    putCareerUpdateResponse()
                setResult(RESULT_OK, intent)
                //finish()
            } else
                toast("내용을 확인해 주세요.")
        } else {
            if (title.isNotEmpty() && notes.isNotEmpty()) {
                if(mIsAdd!!)
                    postCareerAddResponse()
                else
                    putCareerUpdateResponse()
                setResult(RESULT_OK, intent)
                //finish()
            } else
                toast("내용을 확인해 주세요.")
        }
    }

    private fun putCareerUpdateResponse() {
        var jsonObject = JsonObject()
        jsonObject.addProperty("careerIdx", mIdx)
        jsonObject.addProperty("careerType", mSection)
        jsonObject.addProperty("careerName", act_career_detail_et_title.text.toString())
        jsonObject.addProperty("careerContent", act_career_detail_et_note.text.toString())
        jsonObject.addProperty("careerDate1", mStartYear + "-" + mStartMonth)
        if(mSection == 0)
            jsonObject.addProperty("careerDate2", mEndYear + "-" + mEndMonth)
        else
            jsonObject.addProperty("careerDate2", "")


        val putCareerUpdateResponse =
            networkService.putCareerUpdateResponse("application/json", SharedPreferenceController.getAuthorization(this), jsonObject)

        putCareerUpdateResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                if (response.isSuccessful) {
                    finish()
                }
            }
        })
    }

    private fun postCareerAddResponse() {
        var jsonObject = JsonObject()
        jsonObject.addProperty("careerType", mSection)
        jsonObject.addProperty("careerName", act_career_detail_et_title.text.toString())
        jsonObject.addProperty("careerContent", act_career_detail_et_note.text.toString())
        jsonObject.addProperty("careerDate1", mStartYear + "-" + mStartMonth)
        if(mSection == 0)
            jsonObject.addProperty("careerDate2", mEndYear + "-" + mEndMonth)
        else
            jsonObject.addProperty("careerDate2", "")


        val postCareerAddResponse =
            networkService.postCareerAddResponse("application/json", SharedPreferenceController.getAuthorization(this), jsonObject)

        postCareerAddResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                if (response.isSuccessful) {
                    finish()
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_career_edit)

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
        //region toolbar
        setSupportActionBar(act_career_detail_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_x_close)
        //endregion

        //region intent
        val intent = getIntent()
        mIsAdd = intent.getBooleanExtra("isAdd", true)
        if (intent.getBooleanExtra("isAdd", true) == false) {
            mIdx = intent.getIntExtra("idx", 0)
            when (intent.getIntExtra("section", 0)) {
                0 -> {
                    mSection = 0
                    act_career_detail_tv_title.text = mTitle[0]
                    act_career_detail_et_title.hint = "활동명 입력"
                    act_career_detail_et_title.setText(intent.getStringExtra("careerName"))

                    act_career_detail_rl_except_act.visibility = View.GONE
                    act_career_detail_ll_act.visibility = View.VISIBLE
                    act_career_detail_tv_content_title.text = "활동내용"
                    act_career_detail_et_note.setText(intent.getStringExtra("careerContent"))
                    act_career_detail_et_note.hint = getString(R.string.out_activity_contents)

                    act_career_detail_date_start.text = (intent.getStringExtra("careerDate1").replace("-", "년 ") + "월")
                    mStartYear = intent.getStringExtra("careerDate1").substring(0, intent.getStringExtra("careerDate1").indexOf("-"))
                    mStartMonth = intent.getStringExtra("careerDate1").substring(intent.getStringExtra("careerDate1").indexOf("-") + 1)

                    act_career_detail_date_end.text = (intent.getStringExtra("careerDate2").replace("-", "년 ") + "월")
                    mEndYear = intent.getStringExtra("careerDate2").substring(0, intent.getStringExtra("careerDate2").indexOf("-"))
                    mEndMonth = intent.getStringExtra("careerDate2").substring(intent.getStringExtra("careerDate2").indexOf("-") + 1)
                }
                1 -> {
                    mSection = 1
                    act_career_detail_tv_title.text = mTitle[1]
                    act_career_detail_et_title.hint = "수상명 입력"
                    act_career_detail_et_title.setText(intent.getStringExtra("careerName"))

                    act_career_detail_rl_except_act.visibility = View.VISIBLE
                    act_career_detail_ll_act.visibility = View.GONE

                    act_career_detail_tv_year.text = "수상"
                    act_career_detail_date_start_only.text = (intent.getStringExtra("careerDate1").replace("-", "년 ") + "월")
                    mStartYear = intent.getStringExtra("careerDate1").substring(0, intent.getStringExtra("careerDate1").indexOf("-"))
                    mStartMonth = intent.getStringExtra("careerDate1").substring(intent.getStringExtra("careerDate1").indexOf("-") + 1)

                    act_career_detail_et_note.setText(intent.getStringExtra("careerContent"))
                }
                2 -> {
                    mSection = 2
                    act_career_detail_tv_title.text = mTitle[2]
                    act_career_detail_et_title.hint = "자격증명 입력"
                    act_career_detail_et_title.setText(intent.getStringExtra("careerName"))

                    act_career_detail_rl_except_act.visibility = View.VISIBLE
                    act_career_detail_ll_act.visibility = View.GONE

                    act_career_detail_tv_year.text = "취득"
                    act_career_detail_date_start_only.text = (intent.getStringExtra("careerDate1").replace("-", "년 ") + "월")
                    mStartYear = intent.getStringExtra("careerDate1").substring(0, intent.getStringExtra("careerDate1").indexOf("-"))
                    mStartMonth = intent.getStringExtra("careerDate1").substring(intent.getStringExtra("careerDate1").indexOf("-") + 1)

                    act_career_detail_et_note.setText(intent.getStringExtra("careerContent"))
                }
                else -> {
                }
            }
        } else {
            when (intent.getIntExtra("section", 0)) {
                0 -> {
                    mSection = 0
                    act_career_detail_tv_title.text = mTitle[0]
                    act_career_detail_et_title.hint = "활동명 입력"

                    act_career_detail_rl_except_act.visibility = View.GONE
                    act_career_detail_ll_act.visibility = View.VISIBLE

                    act_career_detail_tv_content_title.text = "활동내용"
                    act_career_detail_et_note.hint = getString(R.string.out_activity_contents)

                    act_career_detail_date_start.text = (mStartYear + "년 " + mStartMonth + "월 ")
                    act_career_detail_date_end.text = (mEndYear + "년 " + mEndMonth + "월 ")
                }
                1 -> {
                    mSection = 1
                    act_career_detail_tv_title.text = mTitle[1]
                    act_career_detail_et_title.hint = "수상명 입력"

                    act_career_detail_rl_except_act.visibility = View.VISIBLE
                    act_career_detail_ll_act.visibility = View.GONE
                    act_career_detail_tv_year.text = "수상"

                    act_career_detail_date_start_only.text = (mStartYear + "년 " + mStartMonth + "월 ")
                }
                2 -> {
                    mSection = 2
                    act_career_detail_tv_title.text = mTitle[2]
                    act_career_detail_et_title.hint = "자격증명 입력"

                    act_career_detail_rl_except_act.visibility = View.VISIBLE
                    act_career_detail_ll_act.visibility = View.GONE
                    act_career_detail_tv_year.text = "취득"

                    act_career_detail_date_start_only.text = (mStartYear + "년 " + mStartMonth + "월 ")
                }
                else -> {
                }
            }
        }
        //endregion
    }
}
