package com.icoo.ssgsag_android.ui.main.review

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.review.event.ReviewEventActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_review_done.*
import org.jetbrains.anko.startActivity

class ReviewDoneActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_done)

        var clubIdx = -1
        var showEvent = false

        // todo: view 바꾸는거 binding adapter에서 하는 법 익히기
        when(intent.getStringExtra("from")){
            "write" -> {
                // text 그대로 유지
                clubIdx = intent.getIntExtra("clubIdx", -1)
                showEvent = intent.getBooleanExtra("showEvent", false)
            }
            "caution" ->{
                act_review_done_tv_main.text = "신고 완료"
                act_review_done_tv_text.text = "승인여부는 3일 내 이메일로 알려드릴게요."
            }
            "edit" ->{
                act_review_done_tv_main.text = "수정이\n완료되었습니다 :)"
                act_review_done_tv_text.text = ""
            }
            "rgstr"->{
                clubIdx = intent.getIntExtra("clubIdx", -1)
                act_review_done_tv_text.text = ""
            }
        }

        act_review_done_iv_cancel.setSafeOnClickListener {
            finish()
        }

        act_review_done_cl_done.setSafeOnClickListener {

            finish()

            if(showEvent) {
                val intent = Intent(this, ReviewEventActivity::class.java)
                intent.putExtra("clubIdx", clubIdx)
                startActivity(intent)
            }
        }
    }
}