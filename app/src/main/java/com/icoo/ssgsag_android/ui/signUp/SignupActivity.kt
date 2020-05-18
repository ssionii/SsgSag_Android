package com.icoo.ssgsag_android.ui.signUp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
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
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.user.DeviceInfo
import com.icoo.ssgsag_android.databinding.ActivitySignupBinding
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.privacy.PrivacyActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.term.TermActivity
import com.icoo.ssgsag_android.ui.splash.SplashActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.igaworks.v2.abxExtensionApi.AbxCommon
import com.igaworks.v2.core.AdBrixRm
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import io.realm.RealmObject
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*
import java.util.regex.Pattern

class SignupActivity : BaseActivity<ActivitySignupBinding, SignupViewModel>() {

    object GetSignupProfile {
        lateinit var nickname: String
        var gender: String = "male"
        lateinit var birth: String
        lateinit var school: String
        lateinit var stduentNumber: String
        lateinit var major: String
        var grade = 0
    }

    val univMap = HashMap<String, List<String>>()
    val univList = ArrayList<String>()
    private val gradeList = ArrayList<String>()
    private val admissionYearList = ArrayList<String>()
    private var isClickable = false
    private var checkBirth = false
    private var checkNickName = false
    private var checkNickNameValidation = false
    private var checkSchoolList = false
    private var checkMajorList = false

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        realmDeviceInfo = realm.where(DeviceInfo::class.java).equalTo("id", 1 as Int).findFirst()!!

        setButton()
        checkNicknameValidate()
        getJsonList()
        setGradeListAndAdmissionYearList()
        setEditTextChange()
        navigator()
        logEVENT_NAME_REGISTRATION_OPENEvent()
        viewDataBinding.actSignupAtSchool.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewDataBinding.actSignupAtMajor.setText("")
                val text = s.toString()
                if (checkUnivValidate(text))
                    onMajorSetByUniv()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    inner class CustomAdapter : ArrayAdapter<String>(this@SignupActivity, R.layout.item_spinner) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent)
            if (position == count) {
                //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count)
            }
            return v
        }

        override fun getCount(): Int {
            //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
            return super.getCount() - 1
        }
    }


    // 학교 리스트 가져오기
    fun getJsonList() {
        val thread = Thread(Runnable {
            try {
                val inputStream= URL("http://ssgsag-alb-2141317761.ap-northeast-2.elb.amazonaws.com/validUnivList").openStream()
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

                var str : String? = ""
                val buffer = StringBuffer()

                str = bufferedReader.readLine()
                while(str != null){
                    buffer.append(str)
                    Log.e("buffer", buffer.toString())
                    str = bufferedReader.readLine()
                }

                val bundle = Bundle()
                bundle.putString("UnivListJson", buffer.toString())

                val msg = getUnivListHandler.obtainMessage()
                msg.data = bundle
                getUnivListHandler.sendMessage(msg)


            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()

    }

    private val getUnivListHandler= GetUnivListHandler(this)

    // 핸들러 객체 만들기
    private class GetUnivListHandler(signupActivity: SignupActivity) : Handler() {

        private val signupActivityWeakReference: WeakReference<SignupActivity>

        init {
            signupActivityWeakReference = WeakReference<SignupActivity>(signupActivity)
        }

        override fun handleMessage(msg: Message) {
            val activity = signupActivityWeakReference.get()
            if (activity != null) {
                activity.handleMessage(msg)
                // 핸들메세지로 결과값 전달
            }
        }
    }

    private fun handleMessage(msg: Message){
        val bundle = msg.data

        val univObj = JSONObject(bundle.getString("UnivListJson"))
        val univJsonArr = univObj.getJSONArray("data")

        for (i in 0 until univJsonArr.length()) {
            val univJsonObj = univJsonArr.getJSONObject(i)

            val univName = univJsonObj.getString("학교명")
            val majorName = univJsonObj.getString("학부·과(전공)명")
            val majorArr = majorName
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .split(",")

            univList.add(univName)
            univMap.put(univName, majorArr)
        }

        setAutoCompleteTextList()

    }


    private fun setButton() {

        viewDataBinding.actSignupIvBack.setSafeOnClickListener {
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

        for (schoolName in univList) {
            if (schoolName == GetSignupProfile.school) {
                checkSchoolList = true
                break
            } else
                checkSchoolList = false
        }

        if (!checkSchoolList) {
            toast("학교 이름을 리스트에서 골라 주세요")
        }

        univMap.get(viewDataBinding.actSignupAtSchool.text.toString())?.let {
            for (majorName in it) {
                if (majorName == GetSignupProfile.major) {
                    checkMajorList = true
                    break
                } else
                    checkMajorList = false
            }
        }

        if (!checkMajorList) {
            toast("학과 이름을 리스트 에서 골라 주세요")
        }

        return checkBirth && checkNickName && checkSchoolList && checkMajorList
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
                        GetSignupProfile.stduentNumber =
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
        GetSignupProfile.school = viewDataBinding.actSignupAtSchool.text.toString()
        GetSignupProfile.major = viewDataBinding.actSignupAtMajor.text.toString()
    }

    private fun onDataCheck() {
        getEditText()
        if (GetSignupProfile.birth.isEmpty() || GetSignupProfile.nickname.isEmpty() || GetSignupProfile.gender.isNullOrEmpty()
            || GetSignupProfile.school.isEmpty() || GetSignupProfile.major.isEmpty() || GetSignupProfile.stduentNumber.isEmpty() || GetSignupProfile.grade == 0
            || !viewDataBinding.actSignupCbTerms.isChecked || (checkNickNameValidation == false)
        ) {
            isClickable = false
            viewDataBinding.actSignupIvDone.setBackgroundColor(Color.parseColor("#c9c9c9"))
        } else if (GetSignupProfile.birth.isNotEmpty() && GetSignupProfile.nickname.isNotEmpty() && GetSignupProfile.gender!!.isNotEmpty()
            && GetSignupProfile.school.isNotEmpty() && GetSignupProfile.major.isNotEmpty() && GetSignupProfile.stduentNumber.isNotEmpty() && GetSignupProfile.grade != 0
            && viewDataBinding.actSignupCbTerms.isChecked && checkNickNameValidation
        ) {
            isClickable = true
            viewDataBinding.actSignupIvDone.setBackgroundResource(R.drawable.background_signup_done)
        }
    }


    private fun setEditTextChange() {
        viewDataBinding.actSignupEtNickname.onChange { onDataCheck() }
        viewDataBinding.actSignupEtBirth.onChange { onDataCheck() }
        viewDataBinding.actSignupAtSchool.onChange { onDataCheck() }
        viewDataBinding.actSignupAtMajor.onChange { onDataCheck() }
    }

    private fun loadJSONFromAsset(fileName: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open(fileName)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun setAutoCompleteTextList() {

        val schoolAdapter = ArrayAdapter<String>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            univList // Array
        )
        viewDataBinding.actSignupAtSchool.setAdapter(schoolAdapter)
    }

    fun checkUnivValidate(text: String): Boolean {
        for (i in univList.indices) {
            if (text == univList[i])
                return true
        }
        return false
    }

    private fun onMajorSetByUniv() {
        val majorAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            univMap.get(
                viewDataBinding.actSignupAtSchool.text.toString()
            ) // Array
        )
        viewDataBinding.actSignupAtMajor.setAdapter(majorAdapter)
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
        jsonObject.put("userStudentNum", GetSignupProfile.stduentNumber)
        jsonObject.put("userGender", GetSignupProfile.gender)
        jsonObject.put("userBirth", GetSignupProfile.birth)
        jsonObject.put("userGrade", GetSignupProfile.grade)
        jsonObject.put("userPushAllow", 1)
        jsonObject.put("userInfoAllow", 1)
        jsonObject.put("osType", 0)
        var gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        viewModel.signup(gsonObject)

        viewModel.loginToken.observe(this, androidx.lifecycle.Observer {
            viewModel.login(
                realmDeviceInfo.token,
                realmDeviceInfo.loginType
            )

            //adbrix
            try {
                // 회원가입 이벤트 추가 정보를 설정합니다.
                val signUpAttr = AdBrixRm.AttrModel()
                    .setAttrs("userNickname",GetSignupProfile.nickname)
                    .setAttrs("accessToken",realmDeviceInfo.token )
                    .setAttrs("uuid", realmDeviceInfo.uuid)
                    .setAttrs("userUniv", GetSignupProfile.school)
                    .setAttrs("userMajor", GetSignupProfile.major)
                    .setAttrs("userStudentNum", GetSignupProfile.stduentNumber)
                    .setAttrs("userGender", GetSignupProfile.gender)
                    .setAttrs("userBirth", GetSignupProfile.birth)
                    .setAttrs("userPushAllow", 1)
                    .setAttrs("userInfoAllow", 1)
                    .setAttrs("userGrade", GetSignupProfile.grade.toLong())
                    .setAttrs("osType", 0)

                // 이벤트 추가 정보를 회원가입 이벤트 정보로 설정합니다.
                val properties = AdBrixRm.CommonProperties.SignUp()
                    .setAttrModel(signUpAttr)

                // 회원 가입 API 호출
                AbxCommon.signUp(AdBrixRm.CommonSignUpChannel.Google, properties)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            // 유저 정보
            if (GetSignupProfile.gender == "female")
                AdBrixRm.setGender(AdBrixRm.AbxGender.FEMALE)
            else
                AdBrixRm.setGender(AdBrixRm.AbxGender.MALE)

            val userProperties = AdBrixRm.UserProperties()
                .setAttrs("birth", GetSignupProfile.birth)
                .setAttrs("major", GetSignupProfile.major)
                .setAttrs("univ", GetSignupProfile.school)

            AdBrixRm.saveUserProperties(userProperties)
            logEVENT_NAME_COMPELTE_REGISTRATIONEvent()

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