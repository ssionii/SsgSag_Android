package com.icoo.ssgsag_android.ui.main.myPage.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icoo.ssgsag_android.R
import android.content.Intent
import android.graphics.Color
import android.view.MenuItem
import android.widget.ImageView
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_contact.*
import org.jetbrains.anko.toast


class ContactActivity : AppCompatActivity() {

    private var category = ""
    private var selectedCateInt = -1
    private var selectedCate: ImageView ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        init()
        setSendButton()
        setCategoryButton()

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
        setSupportActionBar(act_contact_tb_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
    }

    private fun setSendButton() {
        act_contact_iv_forward.setSafeOnClickListener {
            if (selectedCateInt != -1) {
                val email = Intent(Intent.ACTION_SEND)
                email.type = "plain/text"
                val address = arrayOf("ssgsag.univ@gmail.com")
                email.putExtra(Intent.EXTRA_EMAIL, address)
                email.putExtra(Intent.EXTRA_SUBJECT, "문의사항: " + category)
                email.putExtra(Intent.EXTRA_TEXT, act_contact_et_contents.text)
                startActivity(email)
                finish()

            } else {
                toast("분류를 선택해주세요.")
            }
        }

    }

    private fun setCategoryButton(){
        act_contact_rl_cate_1.setSafeOnClickListener {
            if(selectedCateInt != 0){
                setUnSelectedButton(selectedCateInt)
                act_contact_rl_cate_1.isSelected = true
                act_contact_tv_cate_1.isSelected = true
                selectedCateInt = 0
                category ="피드백"
            }
        }
        act_contact_rl_cate_2.setSafeOnClickListener {
            if(selectedCateInt != 1){
                setUnSelectedButton(selectedCateInt)
                act_contact_rl_cate_2.isSelected = true
                act_contact_tv_cate_2.isSelected = true
                selectedCateInt = 1
                category = "오류"
            }
        }
        act_contact_rl_cate_3.setSafeOnClickListener {
            if(selectedCateInt != 2){
                setUnSelectedButton(selectedCateInt)
                act_contact_rl_cate_3.isSelected = true
                act_contact_tv_cate_3.isSelected = true
                selectedCateInt = 2
                category = "기능추가"
            }
        }
        act_contact_rl_cate_4.setSafeOnClickListener {
            if(selectedCateInt != 3){
                setUnSelectedButton(selectedCateInt)
                act_contact_rl_cate_4.isSelected = true
                act_contact_tv_cate_4.isSelected = true
                selectedCateInt = 3
                category ="디자인개선"
            }
        }
        act_contact_rl_cate_5.setSafeOnClickListener {
            if(selectedCateInt != 4){
                setUnSelectedButton(selectedCateInt)
                act_contact_rl_cate_5.isSelected = true
                act_contact_tv_cate_5.isSelected = true
                selectedCateInt = 4
                category = "홍보문의"
            }
        }
        act_contact_rl_cate_6.setSafeOnClickListener {
            if(selectedCateInt != 5){
                setUnSelectedButton(selectedCateInt)
                act_contact_rl_cate_6.isSelected = true
                act_contact_tv_cate_6.isSelected = true
                selectedCateInt = 5
                category ="기타"
            }
        }
    }

    private fun setUnSelectedButton(before: Int){
        when(before){
            0 ->{
                act_contact_rl_cate_1.isSelected = false
                act_contact_tv_cate_1.isSelected = false
            }
            1 ->{
                act_contact_rl_cate_2.isSelected = false
                act_contact_tv_cate_2.isSelected = false
            }
            2 ->{
                act_contact_rl_cate_3.isSelected = false
                act_contact_tv_cate_3.isSelected = false
            }
            3 ->{
                act_contact_rl_cate_4.isSelected = false
                act_contact_tv_cate_4.isSelected = false
            }
            4 ->{
                act_contact_rl_cate_5.isSelected = false
                act_contact_tv_cate_5.isSelected = false
            }
            5 ->{
                act_contact_rl_cate_6.isSelected = false
                act_contact_tv_cate_6.isSelected = false
            }
        }
    }
}
