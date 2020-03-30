package com.icoo.ssgsag_android.ui.main.review.club.registration.pages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.databinding.FragmentClubContactBinding
import com.icoo.ssgsag_android.ui.main.review.ReviewDoneActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity.ClubRgstrData
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.json.JSONObject
import java.util.regex.Pattern


class ClubContactFragment : BaseFragment<FragmentClubContactBinding, ClubRgstrViewModel>(){
    override val layoutResID: Int
        get() = R.layout.fragment_club_contact
    override val viewModel: ClubRgstrViewModel by viewModel()

    val position = 3



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setButton()
        setEditTextChange()
    }

    override fun onResume() {
        super.onResume()

        onDataCheck()
        (activity as ClubRgstrActivity).hideKeyboard(viewDataBinding.fragClubContactEtEmail)
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setButton(){

        viewDataBinding.fragClubContactIvBack.setSafeOnClickListener {
            (activity as ClubRgstrActivity).toPrevPage(position)
        }

        viewDataBinding.fragClubContactClNext.setSafeOnClickListener {
            rgstrClub()

            activity!!.finish()
            (activity as ClubRgstrActivity).hideKeyboard(viewDataBinding.fragClubContactEtEmail)
            val intent = Intent(activity!!, ReviewDoneActivity::class.java)
            intent.putExtra("from", "rgstr")
            startActivity(intent)
        }
    }
    private fun setEditTextChange() {
        viewDataBinding.fragClubContactEtEmail.onChange { onDataCheck() }
        viewDataBinding.fragClubContactEtPhone.onChange { onDataCheck() }
    }

    private fun getEditText() {
        ClubRgstrData.adminEmail = viewDataBinding.fragClubContactEtEmail.text.toString()
        ClubRgstrData.adminCall = viewDataBinding.fragClubContactEtPhone.text.toString()
    }

    private fun onDataCheck() {
        getEditText()

        if(ClubRgstrData.adminEmail.isEmpty() || ClubRgstrData.adminCall.isEmpty()
            || !Pattern.matches("^[0-9]*\$", ClubRgstrData.adminCall)
            || !checkEmail(ClubRgstrData.adminEmail)){
            // todo: viewModel로 해결하는 방법 찾기
            viewDataBinding.fragClubContactClNext.apply{
                backgroundColor = Color.parseColor("#aaaaaa")
                isClickable = false
            }

        }else{
            viewDataBinding.fragClubContactClNext.apply{
                backgroundColor = Color.parseColor("#656ef0")
                isClickable = true
            }
        }
    }

    private fun checkEmail(input: String) : Boolean {
        return (Pattern.matches(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+", input) )
    }


    private fun rgstrClub(){
        val jsonObject = JSONObject()
        jsonObject.put("isAdmin", 1)
        jsonObject.put("clubType", viewModel.clubType.value)
        jsonObject.put("univOrLocation", ClubRgstrData.univOrLocation)
        jsonObject.put("clubName", ClubRgstrData.clubName)
        jsonObject.put("oneLine", ClubRgstrData.oneLine)
        jsonObject.put("activeNum", ClubRgstrData.activeNum)
        jsonObject.put("meetingTime", ClubRgstrData.meetingTime)
        jsonObject.put("clubFee", ClubRgstrData.clubFee)
        jsonObject.put("introduce", ClubRgstrData.introduce)

        ClubRgstrData.categoryList.let{
            var categoryList = ""
            for(i in 0.. it.size-2) {
                categoryList += it[i]
                categoryList += ","
            }
            categoryList += it[it.size-1]
            jsonObject.put("categoryList", categoryList)
        }

        viewModel.clubPhotoUrlList.let{
            if(it.size > 0) {
                var photoList = ""
                for(i in 0..it.size-2){
                    photoList += it[i]
                    photoList += ","
                }
                photoList += it[it.size-1]


                jsonObject.put("clubPhotoUrlList", photoList)
            }
        }

        if(ClubRgstrData.clubWebsite.isNotEmpty())
            jsonObject.put("clubWebsite", ClubRgstrData.clubWebsite)
        else
            jsonObject.put("clubWebsite", null)

        if(ClubRgstrData.adminName.isNotEmpty()) jsonObject.put("adminEmail", ClubRgstrData.adminEmail)
        if(ClubRgstrData.adminCall.isNotEmpty()) jsonObject.put("adminCallNum", ClubRgstrData.adminCall)


        // 동아리 등록보다 후기가 먼저 등록된 경우 (동아리 상세에서 정보 추가 한 경우) clubIdx 추가
        viewModel.clubIdx.let {
            if(it != -1) jsonObject.put("clubIdx", it)
        }

        viewModel.rgstrClub(jsonObject)
        viewModel.clearPhotos()

    }

}