package com.icoo.ssgsag_android.ui.main.review.club.registration

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.databinding.ActivityClubManagerContactBinding
import com.icoo.ssgsag_android.ui.main.review.club.write.ClubReviewWriteActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.regex.Pattern


class ClubManagerContactActivity : BaseActivity<ActivityClubManagerContactBinding, ClubRgstrViewModel>(){
    override val layoutResID: Int
        get() = R.layout.activity_club_manager_contact

    override val viewModel: ClubRgstrViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        setButton()
        setEditTextChange()
    }

    private fun setButton(){
        viewDataBinding.actClubManagerContactIvBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actClubManagerContactClDone.setSafeOnClickListener {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.hideSoftInputFromWindow(
                viewDataBinding.actClubManagerContactEtName.getWindowToken(), 0)
            postAdminInfo()
            val toast = Toast.makeText(this, " 감사합니다 \uD83D\uDE42", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()
            finish()
        }
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

    private fun setEditTextChange() {
        viewDataBinding.actClubManagerContactEtName.onChange { onDataCheck() }
        viewDataBinding.actClubManagerContactEtContact.onChange { onDataCheck() }
    }

    private fun getEditText() {
        ClubRgstrActivity.ClubRgstrData.adminName = viewDataBinding.actClubManagerContactEtName.text.toString()
        ClubRgstrActivity.ClubRgstrData.adminCall = viewDataBinding.actClubManagerContactEtContact.text.toString()
    }

    private fun onDataCheck() {
        getEditText()


        if(ClubRgstrActivity.ClubRgstrData.adminName.isBlank() || ClubRgstrActivity.ClubRgstrData.adminCall.isBlank()
            || !checkCallOrEmail(ClubRgstrActivity.ClubRgstrData.adminCall)){
            // todo: viewModel로 해결하는 방법 찾기
            viewDataBinding.actClubManagerContactClDone.apply{
                backgroundColor = resources.getColor(R.color.grey_2)
                isClickable = false
            }

        }else{
            viewDataBinding.actClubManagerContactClDone.apply{
                backgroundColor = resources.getColor(R.color.ssgsag)
                isClickable = true
            }
        }
    }

    private fun checkCallOrEmail(input: String) : Boolean {
        return (Pattern.matches(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+", input)
                || Pattern.matches("^[0-9]*\$", input) )
    }

    private fun postAdminInfo(){
        val jsonObject = JSONObject()
        jsonObject.put("isAdmin", 0)
        jsonObject.put("adminName", ClubRgstrActivity.ClubRgstrData.adminName)
        jsonObject.put("adminCallNum", ClubRgstrActivity.ClubRgstrData.adminCall)

        viewModel.rgstrClub(jsonObject)
    }
}