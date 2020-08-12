package com.icoo.ssgsag_android.ui.signUp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.user.DeviceInfo
import com.icoo.ssgsag_android.databinding.ActivitySignupBinding
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.privacy.PrivacyActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.term.TermActivity
import com.icoo.ssgsag_android.ui.signUp.searchUniv.SearchUnivActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.regex.Pattern

class SignupActivity : BaseActivity<ActivitySignupBinding, SignupViewModel>() {

    object GetSignupProfile {
        lateinit var nickname: String
        var gender: String = "male"
        lateinit var birth: String
        var school = "학교 선택"
        var studentNumber = ""
        lateinit var major: String
        var grade = 6
    }

    var majorList = arrayListOf<String>()
    private val gradeList = ArrayList<String>()
    private val admissionYearList = ArrayList<String>()
    private var isClickable = false
    private var checkBirth = false
    private var checkNickName = false
    private var checkNickNameValidation = false
    private var checkSchool = false
    private var checkMajorList = false
    private var checkStudentNumber = false
    private var checkGrade = false

    private var isNotRegisteredUniv = false

    override val layoutResID: Int
        get() = R.layout.activity_signup
    override val viewModel: SignupViewModel by viewModel()

    lateinit var firebaseAnalytics : FirebaseAnalytics

    internal val disposable = CompositeDisposable()

    val realm = Realm.getDefaultInstance()
    lateinit var realmDeviceInfo : DeviceInfo

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    val univNameFromRegister = prepareCall(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data
        var univName = ""

        if(resultCode == Activity.RESULT_OK) {
            univName = data!!.getStringExtra("univName")

            when(data.getStringExtra("from")){
                "search" -> {
                    majorList = data.getStringArrayListExtra("majorList")
                    isNotRegisteredUniv = false
                }
                "register" -> {
                    isNotRegisteredUniv = true
                }
            }

            viewDataBinding.actSignupTvSchoolName.apply {
                text = univName
                textColor = context.resources.getColor(R.color.black)
            }

            GetSignupProfile.school = univName

            setMajorList()
            onDataCheck()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        realmDeviceInfo = realm.where(DeviceInfo::class.java).equalTo("id", 1 as Int).findFirst()!!



        setButton()
        checkNicknameValidate()
        setUnivSearch()
        setMajorList()
        setGradeListAndAdmissionYearList()

        setEditTextChange()
        navigator()
        logEVENT_NAME_REGISTRATION_OPENEvent()
    }

    inner class CustomAdapter : ArrayAdapter<String>(this@SignupActivity, R.layout.item_spinner) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent)
            if (position == count) {
                //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count)
                (v.findViewById<View>(android.R.id.text1) as TextView).textSize = 14f
            }
            return v
        }

        override fun getCount(): Int {
            //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
            return super.getCount() - 1
        }
    }

    // 학교 검색으로 찾기
    private fun setUnivSearch(){
        viewDataBinding.actSignupLlUniv.setSafeOnClickListener {
            val intent = Intent(this, SearchUnivActivity::class.java)
            univNameFromRegister.launch(intent)
        }
    }

    private fun setMajorList(){
        val majorAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            majorList
        )
        viewDataBinding.actSignupAtMajor.setAdapter(majorAdapter)
    }

    private fun setButton() {

        viewDataBinding.actSignupClBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actSignupLlMale.setSafeOnClickListener {
            viewDataBinding.actSignupTvMale.setTextColor(Color.parseColor("#656ef0"))
            viewDataBinding.actSignupIvMale.setImageResource(R.drawable.select_all)
            viewDataBinding.actSignupTvFemale.setTextColor(Color.parseColor("#dddddd"))
            viewDataBinding.actSignupIvFemale.setImageResource(R.drawable.select_all_passive_0)
            GetSignupProfile.gender = "male"

        }

        viewDataBinding.actSignupLlFemale.setSafeOnClickListener {
            viewDataBinding.actSignupTvMale.setTextColor(Color.parseColor("#dddddd"))
            viewDataBinding.actSignupIvMale.setImageResource(R.drawable.select_all_passive_0)
            viewDataBinding.actSignupTvFemale.setTextColor(Color.parseColor("#656ef0"))
            viewDataBinding.actSignupIvFemale.setImageResource(R.drawable.select_all)
            GetSignupProfile.gender = "female"

        }

        viewDataBinding.actSignupRlDone.setSafeOnClickListener {
            if (isClickable) {
                if (checkValidity()) {
                    postSignUpResponseData()
                }
            }
        }

        viewDataBinding.actSignupCbTerms.setSafeOnClickListener {
            onDataCheck()
        }

        viewDataBinding.actSignupTvService.setSafeOnClickListener {
            startActivity<TermActivity>()
        }

        viewDataBinding.actSignupTvPrivacy.setSafeOnClickListener {
            startActivity<PrivacyActivity>()
        }
    }

    private fun checkNicknameValidate(){
        val patternNickName: Pattern = Pattern.compile("^[a-zA-Zㄱ-ㅣ가-힣0-9]*\$")
        val filtersNickName = arrayOf(InputFilter.LengthFilter(10))

        viewDataBinding.actSignupEtNickname.filters = filtersNickName

        viewDataBinding.actSignupEtNickname.onChange {
            viewModel.validateUserNickname(viewDataBinding.actSignupEtNickname.text.toString())

        }

        viewModel.isPossivleNickname.observe(this, androidx.lifecycle.Observer {

            if (patternNickName.matcher(viewDataBinding.actSignupEtNickname.text.toString()).matches()) {
                checkNickName = true
            }else{
                checkNickName = false
            }

            if(it && viewDataBinding.actSignupEtNickname.text.length > 1 && checkNickName) {
                viewDataBinding.actSignupTvNicknameResult.visibility = VISIBLE
                viewDataBinding.actSignupTvNicknameResult.text = "사용 가능한 닉네임입니다."
                viewDataBinding.actSignupTvNicknameResult.setTextColor(Color.parseColor("#6cca4a"))
                checkNickNameValidation = true
            }
            else {
                viewDataBinding.actSignupTvNicknameResult.visibility = VISIBLE
                viewDataBinding.actSignupTvNicknameResult.text = "사용할 수 없는 닉네임입니다."
                viewDataBinding.actSignupTvNicknameResult.setTextColor(Color.parseColor("#fe6d6d"))
                checkNickNameValidation = false
            }
            onDataCheck()
        })
    }

    private fun checkValidity(): Boolean {
        val patternBirth: Pattern = Pattern.compile("^[0-9]*\$")
        val filtersBirth = arrayOf(InputFilter.LengthFilter(6))

        /*
        val patternNickName: Pattern = Pattern.compile("^[a-zA-Zㄱ-ㅣ가-힣0-9]*\$")
        val filtersNickName = arrayOf(InputFilter.LengthFilter(10))
        */
        viewDataBinding.actSignupEtBirth.filters = filtersBirth
        //viewDataBinding.actSignupEtNickname.filters = filtersNickName

        getEditText()

        if (patternBirth.matcher(GetSignupProfile.birth).matches() && GetSignupProfile.birth.length == 6) {
            checkBirth = true
        } else {
            checkBirth = false
            toast("생년월일을 6자리로 입력해주세요")
        }

        if(GetSignupProfile.school.isNotEmpty() && viewDataBinding.actSignupTvSchool.text != "학교 선택"){
            checkSchool = true
        }else{
            checkSchool = false
            toast("학교를 선택해주세요")
        }

        if(isNotRegisteredUniv){
            checkMajorList = true
        }else {
            checkMajorList = majorList.contains(viewDataBinding.actSignupAtMajor.text.toString())
            if (!checkMajorList) {
                toast("학과 이름을 리스트 에서 골라 주세요")
            }
        }

        checkStudentNumber = GetSignupProfile.studentNumber != ""
        if (!checkStudentNumber) {
            toast("학번을 입력해주세요")
        }

        checkGrade = GetSignupProfile.grade != 0
        if (!checkGrade) {
            toast("학년을 입력해주세요")
        }


        return checkBirth && checkNickName && checkMajorList && checkStudentNumber && checkGrade && checkSchool
    }

    private fun setGradeListAndAdmissionYearList() {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val freshman = year.toString().substring(2)

        for (i in freshman.toInt() - 10..freshman.toInt()) {
            if(i < 10){
                admissionYearList.add("~ 0" + i.toString())
            }else
                admissionYearList.add(i.toString())
        }
        admissionYearList.add("학번")
        for (i in 1..5)
            gradeList.add(i.toString())
        gradeList.add("학년")

        val yearAdapter = CustomAdapter()
        val gradeAdapter = CustomAdapter()

        viewDataBinding.actSignupSnStudentNumber.run {
            adapter = yearAdapter.apply {
                addAll(admissionYearList)
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
                        onDataCheck()
                        GetSignupProfile.studentNumber =
                            (position + freshman.toInt() - 10).toString()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(yearAdapter.count)
        }

        viewDataBinding.actSignupSnGrade.run {
            adapter = gradeAdapter.apply {
                addAll(gradeList)
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
                        onDataCheck()
                        GetSignupProfile.grade = (position + 1)
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(gradeAdapter.count)
        }
    }

    private fun getEditText() {
        GetSignupProfile.nickname = viewDataBinding.actSignupEtNickname.text.toString()
        GetSignupProfile.birth = viewDataBinding.actSignupEtBirth.text.toString()
        GetSignupProfile.major = viewDataBinding.actSignupAtMajor.text.toString()
    }

    private fun onDataCheck() {
        getEditText()

        if (GetSignupProfile.birth.isEmpty() || GetSignupProfile.nickname.isEmpty() || GetSignupProfile.gender.isNullOrEmpty()
            || GetSignupProfile.school.isEmpty() || GetSignupProfile.major.isEmpty() || GetSignupProfile.studentNumber == "" || GetSignupProfile.grade == 6
            || !viewDataBinding.actSignupCbTerms.isChecked || (checkNickNameValidation == false)
        ) {
            isClickable = false
            viewDataBinding.actSignupIvDone.backgroundColor = context.resources.getColor(R.color.grey_2)
        } else if (GetSignupProfile.birth.isNotEmpty() && GetSignupProfile.nickname.isNotEmpty() && GetSignupProfile.gender!!.isNotEmpty()
            && GetSignupProfile.school.isNotEmpty() && GetSignupProfile.major.isNotEmpty() && GetSignupProfile.studentNumber.isNotEmpty() && GetSignupProfile.grade != 0
            && viewDataBinding.actSignupCbTerms.isChecked && checkNickNameValidation
        ) {
            isClickable = true
            viewDataBinding.actSignupIvDone.backgroundColor = context.resources.getColor(R.color.ssgsag)
        }
    }

    private fun setEditTextChange() {
        viewDataBinding.actSignupEtNickname.onChange { onDataCheck() }
        viewDataBinding.actSignupEtBirth.onChange { onDataCheck() }
        viewDataBinding.actSignupAtMajor.onChange { onDataCheck() }
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


    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }

    private fun postSignUpResponseData() {

        val jsonObject = JSONObject()

        jsonObject.put("userNickname", GetSignupProfile.nickname)
        jsonObject.put("signupType", realmDeviceInfo.loginType)
        jsonObject.put("accessToken", realmDeviceInfo.token)
        jsonObject.put("uuid", realmDeviceInfo.uuid)
        jsonObject.put("userUniv", GetSignupProfile.school)
        jsonObject.put("userMajor", GetSignupProfile.major)
        jsonObject.put("userStudentNum", GetSignupProfile.studentNumber)
        jsonObject.put("userGender", GetSignupProfile.gender)
        jsonObject.put("userBirth", GetSignupProfile.birth)
        jsonObject.put("userGrade", GetSignupProfile.grade)
        jsonObject.put("userPushAllow", 1)
        jsonObject.put("userInfoAllow", 1)
        jsonObject.put("osType", 0)
        jsonObject.put("registrationCode", SharedPreferenceController.getFireBaseInstanceId(this))
        var gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        viewModel.signup(gsonObject)

        viewModel.loginToken.observe(this, androidx.lifecycle.Observer {
            viewModel.autoLogin(it, null)
        })
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this@SignupActivity, androidx.lifecycle.Observer { value ->
            val intent = Intent(this, value.first.java)
            startActivity(intent)
            finishAffinity()
        })
    }


    private fun logEVENT_NAME_COMPELTE_REGISTRATIONEvent() {
        val logger = AppEventsLogger.newLogger(this)
        logger.logEvent("REGISTRATION_COMPELTE");

        val params = Bundle()
        firebaseAnalytics.logEvent("REGISTRATION_COMPELTE", params)

        val adjustEvent = AdjustEvent("xzagyy")
        Adjust.trackEvent(adjustEvent)
    }


    private fun logEVENT_NAME_REGISTRATION_OPENEvent() {
        val logger = AppEventsLogger.newLogger(this)
        logger.logEvent("REGISTRATION_OPEN")

        val params = Bundle()
        firebaseAnalytics.logEvent("REGISTRATION_OPEN", params)

        val adjustEvent = AdjustEvent("pj08ph")
        Adjust.trackEvent(adjustEvent)
    }



}