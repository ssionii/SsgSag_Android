package com.icoo.ssgsag_android.ui.signUp.school

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.signUp.SignUpResponse
import com.icoo.ssgsag_android.databinding.ActivitySignUpSchoolBinding
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.login.LoginViewModel
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.signUp.profile.SignUpProfileActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up_school.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.igaworks.v2.abxExtensionApi.AbxCommon
import com.igaworks.v2.core.AdBrixRm
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*
import java.util.regex.Pattern


class SignUpSchoolActivity : BaseActivity<ActivitySignUpSchoolBinding, LoginViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_sign_up_school
    override val viewModel: LoginViewModel by viewModel()

    val univMap = HashMap<String, List<String>>()
    val univList = ArrayList<String>()
    private var isClickable = false
    private var checkSchoolList = false
    private var checkMajorList = false
    private var checkGrade = false
    private var checkSid = false
    private val gradeList = ArrayList<String>()
    private val admissionYearList = ArrayList<String>()

    object GetSignUpSchool {
        lateinit var school: String
        lateinit var major: String
        lateinit var grade: String
        lateinit var sid: String
    }

    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }

    internal val disposable = CompositeDisposable()

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        init()
        setGradeListAndAdmissionYearList()
        getJsonList()
        setOnClickListener()
        setEditTextChange()
        navigator()
        act_sign_up_school_at_school.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                act_sign_up_school_at_major.setText("")
                val text = s.toString()
                if (checkUnivValidate(text))
                    onMajorSetByUniv()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        act_sing_up_school_sn_year.setSelection(admissionYearList.count() - 1)
    }

    inner class CustomAdapter: ArrayAdapter<String>(this@SignUpSchoolActivity, R.layout.item_spinner) {
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

    private fun setGradeListAndAdmissionYearList() {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)

        for (i in 2000..year)
            admissionYearList.add(i.toString())
        admissionYearList.add("입학년도")
        for (i in 1..5)
            gradeList.add(i.toString())
        gradeList.add("학년")

        val yearAdapter = CustomAdapter()
        val gradeAdapter = CustomAdapter()



        act_sing_up_school_sn_year.run {
            adapter = yearAdapter.apply {
                    addAll(admissionYearList)
                }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        onDataCheck()
                        GetSignUpSchool.sid = (position + 2000).toString()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(yearAdapter.count)
        }

        act_sing_up_school_sn_grade.run {
            adapter = gradeAdapter.apply {
                    addAll(gradeList)
                }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        onDataCheck()
                        GetSignUpSchool.grade = (position + 1).toString()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(gradeAdapter.count)
        }
    }

    fun checkUnivValidate(text: String): Boolean {
        for (i in univList.indices) {
            if (text == univList[i])
                return true
        }
        return false
    }

    private fun onMajorSetByUniv() {
        val test = act_sign_up_school_at_school.text.toString()
        val majorAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            univMap.get(
                act_sign_up_school_at_school.text.toString()
            ) // Array
        )
        act_sign_up_school_at_major.setAdapter(majorAdapter)
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
        //툴바
        setSupportActionBar(act_sign_up_school_tb_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

    private fun setOnClickListener() {

        act_sign_up_school_iv_next.setSafeOnClickListener {
            if (isClickable) {
                if (checkValidity()) {
                    //postSignUpResponseData()
                }
            }
        }


    }

    private fun checkValidity(): Boolean {

        val patternGrade: Pattern = Pattern.compile("^[0-9]*\$")
        val filtersGrade = arrayOf(InputFilter.LengthFilter(10))

        val patternSid: Pattern = Pattern.compile("^[0-9]*\$")
        val filtersSid = arrayOf(InputFilter.LengthFilter(20))

//        act_sign_up_school_et_grade.filters = filtersGrade
//        act_sign_up_school_et_sid.filters = filtersSid

        getEditText()

        for (schoolName in univList) {
            if (schoolName == GetSignUpSchool.school) {
                Log.d("리스트에학교이름체크", "" + schoolName)
                checkSchoolList = true
                break
            } else
                checkSchoolList = false
        }

        if (!checkSchoolList) {
            toast("학교 이름을 리스트에서 골라 주세요")
        }

        univMap.get(act_sign_up_school_at_school.text.toString())?.let {
            for (majorName in it) {
                if (majorName == GetSignUpSchool.major) {
                    checkMajorList = true
                    break
                } else
                    checkMajorList = false
            }
        }

        if (!checkMajorList) {
            toast("학과 이름을 리스트 에서 골라 주세요")
        }

        if (patternGrade.matcher(GetSignUpSchool.grade).matches()) {
            checkGrade = true
        } else {
            checkGrade = false
            toast("학년은 1자에서 10자 사이로 숫자로만 입력해주세요")
        }

        if (patternSid.matcher(GetSignUpSchool.sid).matches() && GetSignUpSchool.sid.length >= 2) {
            checkSid = true
        } else {
            checkSid = false
            toast("학번은 2자에서 20자 사이 숫자로만 입력해주세요")
        }

        return checkSchoolList && checkMajorList && checkGrade && checkSid
    }

    fun getJsonList() {
        val univObj = JSONObject(loadJSONFromAsset("majorListByUniv.json"))
        val univJsonArr = univObj.getJSONArray("content")

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

    private fun setAutoCompleteTextList() {

        val schoolAdapter = ArrayAdapter<String>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            univList // Array
        )
        act_sign_up_school_at_school.setAdapter(schoolAdapter)
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
        act_sign_up_school_at_school.onChange { onDataCheck() }
        act_sign_up_school_at_school.onChange { onDataCheck() }
        act_sign_up_school_at_major.onChange { onDataCheck() }
//        act_sign_up_school_et_grade.onChange { onDataCheck() }
//        act_sign_up_school_et_sid.onChange { onDataCheck() }
    }

    private fun onDataCheck() {
        getEditText()
        if (GetSignUpSchool.school.isEmpty() || GetSignUpSchool.major.isEmpty() || GetSignUpSchool.grade.isEmpty() || GetSignUpSchool.sid.isEmpty()) {
            isClickable = false
            act_sign_up_school_iv_next.setImageResource(R.drawable.bt_start_unactive)
        } else if (GetSignUpSchool.school.isNotEmpty() && GetSignUpSchool.major.isNotEmpty() && GetSignUpSchool.grade.isNotEmpty() && GetSignUpSchool.sid.isNotEmpty()) {
            isClickable = true
            act_sign_up_school_iv_next.setImageResource(R.drawable.bt_start)
        }
    }

    private fun getEditText() {
        GetSignUpSchool.school = act_sign_up_school_at_school.text.toString()
        GetSignUpSchool.major = act_sign_up_school_at_major.text.toString()
//        GetSignUpSchool.grade = act_sign_up_school_et_grade.text.toString()
//       GetSignUpSchool.sid = act_sign_up_school_et_sid.text.toString()
    }

    /*
    private fun postSignUpResponseData() {
        val jsonObject = JSONObject()

        jsonObject.put("userName", SignUpProfileActivity.GetSignUpProfile.clubName)
        jsonObject.put("userNickname", SignUpProfileActivity.GetSignUpProfile.nickName)
        jsonObject.put("signupType", LoginActivity.GetLogin.loginType)
        jsonObject.put("accessToken", LoginActivity.GetLogin.token)
        jsonObject.put("uuid",LoginActivity.GetLogin.uuid)
        jsonObject.put("userUniv", GetSignUpSchool.school)
        jsonObject.put("userMajor", GetSignUpSchool.major)
        jsonObject.put("userStudentNum", GetSignUpSchool.sid)
        jsonObject.put("userGender", SignUpProfileActivity.GetSignUpProfile.gender)
        jsonObject.put("userBirth", SignUpProfileActivity.GetSignUpProfile.birth)
        jsonObject.put("userPushAllow", 1)
        jsonObject.put("userInfoAllow", 1)
        jsonObject.put("userGrade", GetSignUpSchool.grade)
        jsonObject.put("osType", 0)
        var gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        //endregion

        val signUpResponse: Call<SignUpResponse> =
            networkService.postSignUpResponse("application/json", gsonObject)
        signUpResponse.enqueue(object : Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.d("signup fail", t.toString())
            }

            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    Log.d("SIGNUPlog값", response.body()?.status.toString()) //status가 201이면 성공적

                    if (response.body()?.status == "201") {
                        viewModel.login(
                            LoginActivity.GetLogin.token,
                            LoginActivity.GetLogin.loginType
                        )

                        //adbrix
                       try{
                           // 회원가입 이벤트 추가 정보를 설정합니다.
                           val signUpAttr = AdBrixRm.AttrModel()
                               .setAttrs("userName", SignUpProfileActivity.GetSignUpProfile.clubName)
                               .setAttrs("userNickname", SignUpProfileActivity.GetSignUpProfile.nickName)
                               .setAttrs("accessToken", LoginActivity.GetLogin.token)
                               .setAttrs("uuid",LoginActivity.GetLogin.uuid)
                               .setAttrs("userUniv", GetSignUpSchool.school)
                               .setAttrs("userMajor", GetSignUpSchool.major)
                               .setAttrs("userStudentNum", GetSignUpSchool.sid)
                               .setAttrs("userGender", SignUpProfileActivity.GetSignUpProfile.gender)
                               .setAttrs("userBirth", SignUpProfileActivity.GetSignUpProfile.birth)
                               .setAttrs("userPushAllow", 1)
                               .setAttrs("userInfoAllow", 1)
                               .setAttrs("userGrade", GetSignUpSchool.grade)
                               .setAttrs("osType", 0)

                           // 이벤트 추가 정보를 회원가입 이벤트 정보로 설정합니다.
                           val properties = AdBrixRm.CommonProperties.SignUp()
                               .setAttrModel(signUpAttr)

                           // 회원 가입 API 호출
                           AbxCommon.signUp(AdBrixRm.CommonSignUpChannel.Google,properties)
                       }catch(e: java.lang.Exception){
                           e.printStackTrace()
                       }

                        // 유저 정보
                        if(SignUpProfileActivity.GetSignUpProfile.gender == "female")
                            AdBrixRm.setGender(AdBrixRm.AbxGender.FEMALE)
                        else
                            AdBrixRm.setGender(AdBrixRm.AbxGender.MALE)

                        val userProperties = AdBrixRm.UserProperties()
                            .setAttrs("birth",SignUpProfileActivity.GetSignUpProfile.birth)
                            .setAttrs("major",GetSignUpSchool.major)
                            .setAttrs("univ",GetSignUpSchool.school)

                        AdBrixRm.saveUserProperties(userProperties)


                    } else {
                        toast("입력 정보를 확인해주세요")
                        Log.e("signup status:" , response.body()?.status)
                    }
                }
            }
        })
    }*/

    private fun navigator() {
        viewModel.activityToStart.observe(this@SignUpSchoolActivity, Observer { value ->
            val intent = Intent(this, value.first.java)
            startActivity(intent)
            finishAffinity()
        })
    }

    /*
    private fun postLoginResponse(token: String, loginType: Int) {

        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("accessToken", token)
        jsonObject.put("loginType", loginType)

        Log.d("TOKEN", "" + token)

        val gsonObject: JsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        disposable.add(networkService.postLoginResponse("application/json", gsonObject)
            .subscribeOn(Schedulers.io())
            .map { it.data.token }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe({ responseToken ->
                SharedPreferenceController.setAuthorization(this@SignUpSchoolActivity, responseToken)
                Log.d("token", responseToken)
                startActivity<MainActivity>()
                finishAffinity()
            }) {
                toast("네트워크 상태를 확인해주세요.")
            })
    }*/

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }

}