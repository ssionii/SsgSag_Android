package com.icoo.ssgsag_android.ui.main.review.club.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_club_manager_check.*

class ClubManagerCheckActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_manager_check)




        act_club_manager_check_iv_back.setSafeOnClickListener {
            finish()
        }

        act_club_manager_check_ll_yes.setSafeOnClickListener {

            val RgstrIntent = Intent(this, ClubRgstrActivity::class.java)
            RgstrIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            // 동아리 정보 추가에서 온 거면 clubIdx 추가해주기
            if(intent.getBooleanExtra("isFromReviewDetail", false))
                RgstrIntent.putExtra("clubIdx", intent.getIntExtra("clubIdx", -1))

            startActivity(RgstrIntent)



            finish()
        }

        act_club_manager_check_ll_no.setSafeOnClickListener {
            val intent = Intent(this, ClubManagerContactActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            startActivity(intent)

            finish()
       }

    }
}