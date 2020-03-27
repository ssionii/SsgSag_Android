package com.icoo.ssgsag_android.ui.main.review.club.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.databinding.ActivityClubReviewEditBinding
import com.icoo.ssgsag_android.ui.main.myPage.myReview.MyReviewViewModel
import com.icoo.ssgsag_android.ui.main.review.ReviewDoneActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ClubReviewWriteActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ClubReviewEditActivity : BaseActivity<ActivityClubReviewEditBinding,MyReviewViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_club_review_edit

    override val viewModel: MyReviewViewModel by viewModel()

    private val startYearList = ArrayList<String>()
    private val startMonthList = ArrayList<String>()
    private val endYearList = ArrayList<String>()
    private val endMonthList = ArrayList<String>()
    var startYear = ""
    var startMonth = ""
    var endYear =""
    var endMonth =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setMyReviewDetail(intent.getSerializableExtra("clubReview") as ClubPost)
        viewDataBinding.vm = viewModel

        setActDateSpinner()
        setRatingBar()
        setEditTextTouch()
        setEditTextChange()
        setButton()

        viewModel.myReviewDetail.observe(this, androidx.lifecycle.Observer {
            if(it.clubType == 0){
                viewDataBinding.actClubReviewEditLlActPlace.visibility = VISIBLE
                viewDataBinding.actClubReviewEditLlUniv.visibility = GONE
            }else{
                viewDataBinding.actClubReviewEditLlActPlace.visibility = GONE
                viewDataBinding.actClubReviewEditLlUniv.visibility = VISIBLE
            }
        })

        viewModel.updateStatus.observe(this, androidx.lifecycle.Observer {
            if(it == 200){
                val intent = Intent(this@ClubReviewEditActivity, ReviewDoneActivity::class.java)
                intent.putExtra("from", "edit")

                startActivity(intent)

                finish()
            }else if(it != 0){
                finish()
                toast("오류가 발생하였습니다.")
            }
        })

    }

    private fun setActDateSpinner(){

        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR).toString().substring(2, 4).toInt()

        for (i in year - 10..year) {
            startYearList.add(i.toString() + "년")
            endYearList.add(i.toString() + "년")
        }

        startYearList.add(viewModel.myReviewDetail.value!!.clubStartDate.substring(2,4) +"년")
        endYearList.add(viewModel.myReviewDetail.value!!.clubEndDate.substring(2,4) +"년")
        startYear = viewModel.myReviewDetail.value!!.clubStartDate.substring(2,4)
        endYear = viewModel.myReviewDetail.value!!.clubEndDate.substring(2,4)

        for(i in 1..12){
            startMonthList.add(i.toString() + "월")
            endMonthList.add(i.toString() + "월")
        }

        viewModel.myReviewDetail.value!!.clubStartDate.substring(5,7).let {
            if(it[0] == '0'){
                startMonthList.add(it[1] + "월")
                startMonth = it[1].toString() + "월"
            }else {
                startMonthList.add(it + "월")
                startMonth = it + "월"
            }
        }

        viewModel.myReviewDetail.value!!.clubEndDate.substring(5,7).let {
            if(it[0] == '0'){
                endMonthList.add(it[1] + "월")
                endMonth = it[1].toString() + "월"
            }else {
                endMonthList.add(it + "월")
                endMonth = it + "월"
            }
        }

        val startYearAdapter = CustomAdapter()
        val endYearAdapter = CustomAdapter()
        viewDataBinding.actClubReviewEditSpStartYear.run{
            adapter = startYearAdapter.apply{
                addAll(startYearList)
            }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != startYearList.size -1) {
                           startYear = startYearList[position]

                        }
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(startYearAdapter.count)
        }

        viewDataBinding.actClubReviewEditSpEndYear.run{
            adapter = endYearAdapter.apply {
                addAll(endYearList)
            }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != endYearList.size -1)
                            endYear = endYearList[position]
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(endYearAdapter.count)
        }


        val startMonthAdapter = CustomAdapter()
        val endMonthAdapter = CustomAdapter()
        viewDataBinding.actClubReviewEditSpStartMonth.run{
            adapter = startMonthAdapter.apply{
                addAll(startMonthList)
            }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != startMonthList.size -1)
                           startMonth = startMonthList[position]
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(startMonthAdapter.count)
        }

        viewDataBinding.actClubReviewEditSpEndMonth.run{
            adapter = endMonthAdapter.apply {
                addAll(endMonthList)
            }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != endMonthList.size -1)
                            endMonth = endMonthList[position]
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(endMonthAdapter.count)
        }
    }

    inner class CustomAdapter : ArrayAdapter<String>(this, R.layout.item_spinner_text) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent)
            if (position == count) {
                //(v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                (v.findViewById<View>(android.R.id.text1) as TextView).text = getItem(count)
            }
            return v
        }

        override fun getCount(): Int {
            return super.getCount() - 1
        }
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }


    private fun setRatingBar(){

        viewDataBinding.actClubReviewEditRb0.setOnRatingChangeListener { ratingBar, rating ->
            onDataCheck()
        }

        viewDataBinding.actClubReviewEditRb1.setOnRatingChangeListener { ratingBar, rating ->
            onDataCheck()
        }

        viewDataBinding.actClubReviewEditRb2.setOnRatingChangeListener { ratingBar, rating ->
            onDataCheck()
        }
        viewDataBinding.actClubReviewEditRb3.setOnRatingChangeListener { ratingBar, rating ->
            onDataCheck()
        }
        viewDataBinding.actClubReviewEditRb4.setOnRatingChangeListener { ratingBar, rating ->
            onDataCheck()
        }
    }

    private fun EditText.onTouch(){
        this.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                viewDataBinding.actClubReviewEditNsv.requestDisallowInterceptTouchEvent(true)
                if(event?.action == MotionEvent.ACTION_UP)
                    viewDataBinding.actClubReviewEditNsv.requestDisallowInterceptTouchEvent(false)


                return false
            }
        })
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if((this@onChange == viewDataBinding.actClubReviewEditEtOneLine) && s!!.length > 20){
                    toast( "20자 이내로 입력해주세요.")
                }
            }
        })
    }

    private fun setEditTextChange() {
        viewDataBinding.actClubReviewEditEtOneLine.onChange { onDataCheck() }
        viewDataBinding.actClubReviewEditEtAdvantage.onChange { onDataCheck() }
        viewDataBinding.actClubReviewEditEtDisadvantage.onChange { onDataCheck() }
        viewDataBinding.actClubReviewEditEtTip.onChange { onDataCheck() }

    }


    private fun setEditTextTouch(){
        viewDataBinding.actClubReviewEditEtAdvantage.onTouch()
        viewDataBinding.actClubReviewEditEtDisadvantage.onTouch()
        viewDataBinding.actClubReviewEditEtTip.onTouch()
    }

    private fun onDataCheck() {

        if( viewDataBinding.actClubReviewEditEtOneLine.text.isEmpty()
            || viewDataBinding.actClubReviewEditEtAdvantage.text.length < 20
            || viewDataBinding.actClubReviewEditEtDisadvantage.text.length < 20){

            viewDataBinding.actClubReviewEditClDone.apply{
                backgroundColor = resources.getColor(R.color.grey_2)
                isClickable = false
            }
        }else{
            viewDataBinding.actClubReviewEditClDone.apply{
                backgroundColor = resources.getColor(R.color.ssgsag)
                isClickable = true
            }
        }
    }

    private fun updateReview(){

        val jsonObject = JSONObject()
        var clubStartDate = "20" + startYear.substring(0,2) + "-"
        startMonth.let {
            if(it.length == 2){
                clubStartDate += "0" + it.substring(0,1)
            }else{
                clubStartDate += it.substring(0,2)
            }
        }
        jsonObject.put("clubStartDate", clubStartDate)

        var clubEndDate ="20" + endYear.substring(0,2) + "-"
        endMonth.let {
            if(it.length == 2){
                clubEndDate += "0" + it.substring(0,1)
            }else{
                clubEndDate += it.substring(0,2)
            }
        }
        jsonObject.put("clubEndDate", clubEndDate)

        // 평점
        jsonObject.put("score0", viewDataBinding.actClubReviewEditRb0.rating)
        jsonObject.put("score1", viewDataBinding.actClubReviewEditRb1.rating)
        jsonObject.put("score2", viewDataBinding.actClubReviewEditRb2.rating)
        jsonObject.put("score3", viewDataBinding.actClubReviewEditRb3.rating)
        jsonObject.put("score4", viewDataBinding.actClubReviewEditRb4.rating)

        jsonObject.put("oneLine", viewDataBinding.actClubReviewEditEtOneLine.text)
        jsonObject.put("advantage", viewDataBinding.actClubReviewEditEtAdvantage.text)
        jsonObject.put("disadvantage", viewDataBinding.actClubReviewEditEtDisadvantage.text)
        if(viewDataBinding.actClubReviewEditEtTip.text.isNotEmpty()) {
            jsonObject.put("honeyTip", viewDataBinding.actClubReviewEditEtTip.text)
        }else
            jsonObject.put("honeyTip", "")

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        viewModel.updateReview(body)
    }

    private fun setButton(){
        viewDataBinding.actClubReviewEditClDone.setSafeOnClickListener {
            updateReview()
            hideKeyboard(viewDataBinding.actClubReviewEditEtOneLine)
        }

        viewDataBinding.actClubReviewEditCancel.setSafeOnClickListener {
            finish()
        }
    }

    fun hideKeyboard(et: EditText){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(et.getWindowToken(), 0)

    }

}