package com.icoo.ssgsag_android.ui.main.review.club.write

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubCategoryFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteScoreFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteSimpleFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteStartFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteStartWithNameFragment

class ReviewWritePagerAdapter(fm: FragmentManager, val fragmentCount : Int, val from: String?, val reviewType : String): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {

        if (from == "main") {
            if(reviewType == "club"){
                when (position) {
                    0 -> return ClubCategoryFragment.newInstance("write")
                    1 -> return ReviewWriteStartFragment()
                    2 -> return ReviewWriteScoreFragment()
                    3 -> return ReviewWriteSimpleFragment()
                    else -> return ClubCategoryFragment.newInstance("write")
                }
            }else{
                when (position) {
                    1 -> return ReviewWriteStartFragment()
                    2 -> return ReviewWriteScoreFragment()
                    3 -> return ReviewWriteSimpleFragment()
                    else -> return ReviewWriteStartFragment()
                }
            }
        } else {
            when (position) {
                0, 1 -> return ReviewWriteStartWithNameFragment()
                2 -> return ReviewWriteScoreFragment()
                3 -> return ReviewWriteSimpleFragment()
                else -> return ReviewWriteStartFragment()
            }
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun getCount(): Int = fragmentCount

}