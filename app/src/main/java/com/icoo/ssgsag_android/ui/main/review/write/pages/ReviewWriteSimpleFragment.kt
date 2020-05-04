package com.icoo.ssgsag_android.ui.main.review.club.write.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewWriteSimpleBinding
import com.icoo.ssgsag_android.ui.main.review.ReviewDoneActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity.ClubReviewWriteData
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.fragment_review_write_simple.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewWriteSimpleFragment :BaseFragment<FragmentReviewWriteSimpleBinding, ReviewWriteViewModel>(){
    override val layoutResID: Int
        get() = R.layout.fragment_review_write_simple
    override val viewModel: ReviewWriteViewModel by viewModel()

    var position = -1
    var isDone = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        position = arguments!!.getInt("position", -1)

        setEditTextTouch()
        setEditTextChange()
        setButton()

        viewModel.postStatus.observe(this, Observer { value ->
            if(value == 200) doneActivity()
            else if (value != 0) {
                toast("후기 입력 오류가 발생하였습니다.")
            }
        })

    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser)
            viewDataBinding.fragReviewWriteSimpleTvTitle.text = ClubReviewWriteData.clubName
    }

    override fun onResume() {
        super.onResume()

        (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragWriteReviewSimpleEtOneLine)
    }

    private fun EditText.onTouch(){
        this.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                frag_review_write_simple_nsv.requestDisallowInterceptTouchEvent(true)
                if(event?.action == MotionEvent.ACTION_UP)
                    frag_review_write_simple_nsv.requestDisallowInterceptTouchEvent(false)


                return false
            }
        })
    }

    private fun setEditTextTouch(){
        viewDataBinding.fragReviewWriteSimpleEtAdvantage.onTouch()
        viewDataBinding.fragReviewWriteSimpleEtDisadvantage.onTouch()
        viewDataBinding.fragWriteReviewSimpleEtTip.onTouch()
    }


    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if((this@onChange == viewDataBinding.fragWriteReviewSimpleEtOneLine) && s!!.length > 20){
                    toast( "20자 이내로 입력해주세요.")
                }
            }
        })
    }


    private fun setEditTextChange() {
        viewDataBinding.fragWriteReviewSimpleEtOneLine.onChange { onDataCheck() }
        viewDataBinding.fragReviewWriteSimpleEtAdvantage.onChange { onDataCheck() }
        viewDataBinding.fragReviewWriteSimpleEtDisadvantage.onChange { onDataCheck() }
        viewDataBinding.fragWriteReviewSimpleEtTip.onChange { onDataCheck() }

    }

    private fun setButton(){
        viewDataBinding.fragWriteReviewSimpleIvBack.setSafeOnClickListener {
            (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragWriteReviewSimpleEtOneLine)
            (activity as ReviewWriteActivity).toPrevPage(position)
        }

        viewDataBinding.fragReviewWriteSimpleClDone.setSafeOnClickListener {

            if(isDone) {
                if (viewModel.isRgstrClub)
                    postRgstrClubReview()
                else
                    postNonRgstrClubReview()
                activity!!.finish()
                (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragWriteReviewSimpleEtOneLine)
            }else{
                toast("20자 이상 입력해주세요")
            }
        }
    }

    private fun getEditText() {
        ClubReviewWriteData.oneLine = viewDataBinding.fragWriteReviewSimpleEtOneLine.text.toString()
        ClubReviewWriteData.advantage = viewDataBinding.fragReviewWriteSimpleEtAdvantage.text.toString()
        ClubReviewWriteData.disadvantage = viewDataBinding.fragReviewWriteSimpleEtDisadvantage.text.toString()
        ClubReviewWriteData.honeyTip = viewDataBinding.fragWriteReviewSimpleEtTip.text.toString()
    }

    private fun onDataCheck() {
        getEditText()

        if(ClubReviewWriteData.oneLine.isEmpty() || ClubReviewWriteData.advantage.length < 20 || ClubReviewWriteData.disadvantage.length < 20){
            viewDataBinding.fragReviewWriteSimpleClDone.apply{
                backgroundColor = resources.getColor(R.color.grey_2)
                isDone = false
            }
        }else{
            viewDataBinding.fragReviewWriteSimpleClDone.apply{
                backgroundColor = resources.getColor(R.color.ssgsag)
                isDone = true
            }
        }
    }

    // 등록되어 있지 않은 동아리의 후기 입력
    private fun postNonRgstrClubReview(){
        val jsonObject = JSONObject()

        jsonObject.put("clubType", viewModel.clubType.value)
        jsonObject.put("univOrLocation", ClubReviewWriteData.univOrLocation)
        jsonObject.put("clubName", ClubReviewWriteData.clubName)


        // 날짜
        var clubStartDate = "20" + ClubReviewWriteData.startYear.substring(0,2) + "-"
        ClubReviewWriteData.startMonth.let {
            if(it.length == 2){
                clubStartDate += "0" + it.substring(0,1)
            }else{
                clubStartDate += it.substring(0,2)
            }
        }
        jsonObject.put("clubStartDate", clubStartDate)


        var clubEndDate ="20" + ClubReviewWriteData.endYear.substring(0,2) + "-"
        ClubReviewWriteData.endMonth.let {
            if(it.length == 2){
                clubEndDate += "0" + it.substring(0,1)
            }else{
                clubEndDate += it.substring(0,2)
            }
        }
        jsonObject.put("clubEndDate", clubEndDate)

        // 인턴 직무
        if(ClubReviewWriteData.fieldName != "")
            jsonObject.put("fieldName", ClubReviewWriteData.fieldName)


        // 평점
        jsonObject.put("score0", ClubReviewWriteData.score0)
        jsonObject.put("score1", ClubReviewWriteData.score1)
        jsonObject.put("score2", ClubReviewWriteData.score2)
        jsonObject.put("score3", ClubReviewWriteData.score3)
        jsonObject.put("score4", ClubReviewWriteData.score4)

        jsonObject.put("oneLine", ClubReviewWriteData.oneLine)
        jsonObject.put("advantage", ClubReviewWriteData.advantage)
        jsonObject.put("disadvantage", ClubReviewWriteData.disadvantage)
        if(ClubReviewWriteData.honeyTip != "")
            jsonObject.put("honeyTip", ClubReviewWriteData.honeyTip)

        viewModel.postClubReview(jsonObject)
    }

    // 등록되어 있는 동아리의 후기를 입력할 때
    private fun postRgstrClubReview(){
        val jsonObject = JSONObject()
        var clubStartDate = "20" + ClubReviewWriteData.startYear.substring(0,2) + "-"
        ClubReviewWriteData.startMonth.let {
            if(it.length == 2){
                clubStartDate += "0" + it.substring(0,1)
            }else{
                clubStartDate += it.substring(0,2)
            }
        }
        jsonObject.put("clubStartDate", clubStartDate)


        var clubEndDate ="20" + ClubReviewWriteData.endYear.substring(0,2) + "-"
        ClubReviewWriteData.endMonth.let {
            if(it.length == 2){
                clubEndDate += "0" + it.substring(0,1)
            }else{
                clubEndDate += it.substring(0,2)
            }
        }
        jsonObject.put("clubEndDate", clubEndDate)

        // 인턴 직무
        if(ClubReviewWriteData.fieldName != "")
            jsonObject.put("fieldName", ClubReviewWriteData.fieldName)


        // 평점
        jsonObject.put("score0", ClubReviewWriteData.score0)
        jsonObject.put("score1", ClubReviewWriteData.score1)
        jsonObject.put("score2", ClubReviewWriteData.score2)
        jsonObject.put("score3", ClubReviewWriteData.score3)
        jsonObject.put("score4", ClubReviewWriteData.score4)

        jsonObject.put("oneLine", ClubReviewWriteData.oneLine)
        jsonObject.put("advantage", ClubReviewWriteData.advantage)
        jsonObject.put("disadvantage", ClubReviewWriteData.disadvantage)
        if(ClubReviewWriteData.honeyTip != "") {
            jsonObject.put("honeyTip", ClubReviewWriteData.honeyTip)
        }

        jsonObject.put("clubIdx", viewModel.clubIdx)

        viewModel.postClubReview(jsonObject)
        viewModel.isRgstrClub = false
    }

    fun doneActivity(){
        val intent = Intent(activity!!, ReviewDoneActivity::class.java)
        intent.putExtra("from", "write")
        intent.putExtra("showEvent", viewModel.showEvent)
        intent.putExtra("clubIdx", viewModel.clubIdx)

        startActivity(intent)
    }

    companion object {
        fun newInstance(position: Int): ReviewWriteSimpleFragment {
            val fragment = ReviewWriteSimpleFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }
}