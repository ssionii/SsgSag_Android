package com.icoo.ssgsag_android.ui.main.community.review

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_how_write_review.*

class HowWriteReviewActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_write_review)

        act_how_write_review_iv_back.setSafeOnClickListener {
            finish()
        }

        act_how_write_review_cl_done.setSafeOnClickListener {
            val reviewWriteIntent = Intent(this, ReviewWriteActivity::class.java)
            reviewWriteIntent.apply {
                putExtra("from", intent.getStringExtra("from"))
                putExtra("reviewType", intent.getIntExtra("reviewType", ReviewType.ACT))
                putExtra("clubName", intent.getStringExtra("clubName"))
                putExtra("clubIdx", intent.getIntExtra("clubIdx", 0))

            }
            startActivity(reviewWriteIntent)
            finish()

        }
    }
}