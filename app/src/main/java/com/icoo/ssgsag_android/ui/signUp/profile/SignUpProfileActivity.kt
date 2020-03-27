package com.icoo.ssgsag_android.ui.signUp.profile

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_sign_up_profile.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import android.text.InputFilter
import com.icoo.ssgsag_android.ui.signUp.school.SignUpSchoolActivity
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.privacy.PrivacyActivity
import java.util.regex.Pattern
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.term.TermActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class SignUpProfileActivity : AppCompatActivity() {

object GetSignUpProfile {
    lateinit var name: String
    lateinit var birth: String
    lateinit var nickName: String
    var gender: String? = null
}
private var isClickable = false

private var checkName = false
private var checkBirth = false
private var checkNickName = false

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up_profile)

    init()

    setOnClickListener()
    setEditTextChange()
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
    setSupportActionBar(act_sign_up_profile_tb_toolbar)
    supportActionBar!!.setTitle("")
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
}

private fun setOnClickListener() {

    act_sign_up_profile_iv_next.setSafeOnClickListener {
        if (isClickable) {
            if(checkValidity()){
//                    Log.v("NAMEBIRTHNICKNAME","NAMEBIRTHNICKNAME"+GetSignUpProfile.clubName+GetSignUpProfile.birth+GetSignUpProfile.nickName)
                startActivity<SignUpSchoolActivity>()
                //finish()
            }
        } else {
        }
    }

    act_sign_up_profile_cb_terms.setSafeOnClickListener {
        if (act_sign_up_profile_cb_terms.isChecked)
            onDataCheck()
        else
            onDataCheck()
    }

    act_sign_up_profile_iv_female.setSafeOnClickListener {
        act_sign_up_profile_iv_female.setImageResource(R.drawable.bt_female_active)
        act_sign_up_profile_tv_female.setTextColor(Color.parseColor("#ffffff"))
        act_sign_up_profile_iv_male.setImageResource(R.drawable.bt_male_unactive)
        act_sign_up_profile_tv_male.setTextColor(Color.parseColor("#9e9e9e"))
        GetSignUpProfile.gender = "female"
        onDataCheck()
    }
    act_sign_up_profile_iv_male.setSafeOnClickListener {
        act_sign_up_profile_iv_male.setImageResource(R.drawable.bt_male_active)
        act_sign_up_profile_tv_male.setTextColor(Color.parseColor("#ffffff"))
        act_sign_up_profile_iv_female.setImageResource(R.drawable.bt_female_unactive)
        act_sign_up_profile_tv_female.setTextColor(Color.parseColor("#9e9e9e"))
        GetSignUpProfile.gender = "male"
        onDataCheck()
    }
    act_sign_up_profile_tv_service.setSafeOnClickListener {
        startActivity<TermActivity>()
    }
    act_sign_up_profile_tv_privacy.setSafeOnClickListener {
        startActivity<PrivacyActivity>()
    }
}

private fun checkValidity(): Boolean {
    val patternName : Pattern = Pattern.compile("^[a-zA-Zㄱ-ㅣ가-힣]*\$")
    val filtersName = arrayOf(InputFilter.LengthFilter(10))

    val patternBirth : Pattern = Pattern.compile("^[0-9]*\$")
    val filtersBirth = arrayOf(InputFilter.LengthFilter(6))

    val patternNickName : Pattern = Pattern.compile("^[a-zA-Zㄱ-ㅣ가-힣0-9]*\$")
    val filtersNickName = arrayOf(InputFilter.LengthFilter(10))

    act_sign_up_profile_et_name.filters = filtersName
    act_sign_up_profile_et_birth.filters = filtersBirth
    act_sign_up_profile_et_nickname.filters = filtersNickName

    getEditText()

    if(patternName.matcher(GetSignUpProfile.name).matches()) {
        checkName = true
    }
    else {
        checkName = false
        toast("이름은 1자에서 10자 사이로 한글과 영문으로만 입력해주세요")
    }


    if(patternBirth.matcher(GetSignUpProfile.birth).matches() && GetSignUpProfile.birth.length==6) {
        checkBirth = true
    }
    else {
        checkBirth = false
        toast("생년월일을 6자리로 입력해주세요")
    }

    if(patternNickName.matcher(GetSignUpProfile.nickName).matches()) {
        checkNickName = true
    }
    else {
        checkNickName = false
        toast("닉네임은 1자에서 10자 사이로 한글, 영문, 숫자로만 입력해주세요")
    }
    return checkName && checkBirth && checkNickName
}
private fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

private fun setEditTextChange(){
    act_sign_up_profile_et_name.onChange { onDataCheck() }
    act_sign_up_profile_et_birth.onChange { onDataCheck() }
    act_sign_up_profile_et_nickname.onChange { onDataCheck() }
}

private fun onDataCheck() {
    getEditText()
    if (GetSignUpProfile.name.isEmpty() || GetSignUpProfile.birth.isEmpty() || GetSignUpProfile.nickName.isEmpty() || GetSignUpProfile.gender.isNullOrEmpty() || !act_sign_up_profile_cb_terms.isChecked) {
        isClickable = false
        act_sign_up_profile_iv_next.setImageResource(R.drawable.bt_next_unactive)
    } else if (GetSignUpProfile.name.isNotEmpty() && GetSignUpProfile.birth.isNotEmpty() && GetSignUpProfile.nickName.isNotEmpty() && GetSignUpProfile.gender!!.isNotEmpty() && act_sign_up_profile_cb_terms.isChecked) {
        isClickable = true
        act_sign_up_profile_iv_next.setImageResource(R.drawable.bt_next_active)
    }
}

private fun getEditText() {
    GetSignUpProfile.name = act_sign_up_profile_et_name.text.toString()
    GetSignUpProfile.birth = act_sign_up_profile_et_birth.text.toString()
    GetSignUpProfile.nickName = act_sign_up_profile_et_nickname.text.toString()
}

}
