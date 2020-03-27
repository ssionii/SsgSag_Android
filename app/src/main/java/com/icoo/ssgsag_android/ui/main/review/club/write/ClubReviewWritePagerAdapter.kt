package com.icoo.ssgsag_android.ui.main.review.club.write

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubCategoryFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ClubReviewWriteScoreFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ClubReviewWriteSimpleFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ClubReviewWriteStartFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ClubReviewWriteStartWithNameFragment

class ClubReviewWritePagerAdapter(fm: FragmentManager, val fragmentCount : Int, val from: String?): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {

        if (from == "main") {
            when (position) {
                0 -> return ClubCategoryFragment.newInstance("write")
                1 -> return ClubReviewWriteStartFragment()
                2 -> return ClubReviewWriteScoreFragment()
                3 -> return ClubReviewWriteSimpleFragment()
                else -> return ClubCategoryFragment.newInstance("write")
            }
        } else {
            when (position) {
                0, 1 -> return ClubReviewWriteStartWithNameFragment()
                2 -> return ClubReviewWriteScoreFragment()
                3 -> return ClubReviewWriteSimpleFragment()
                else -> return ClubCategoryFragment.newInstance("write")
            }
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun getCount(): Int = fragmentCount

}