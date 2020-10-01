package com.icoo.ssgsag_android.ui.main.review.club.write

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.icoo.ssgsag_android.ui.main.community.review.ReviewType
import com.icoo.ssgsag_android.ui.main.community.review.write.pages.ReviewWriteInternFieldFragment
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubCategoryFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteScoreFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteSimpleFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteStartFragment
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteStartWithNameFragment

class ReviewWritePagerAdapter(fm: FragmentManager, val fragmentCount : Int, val from: String?, val reviewType : Int): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {

        if (from == "main") {
            if(reviewType == ReviewType.UNION_CLUB || reviewType == ReviewType.UNIV_CLUB){
                when (position) {
                    0 -> return ClubCategoryFragment.newInstance("write")
                    1 -> return ReviewWriteStartFragment.newInstance(1)
                    2 -> return ReviewWriteScoreFragment.newInstance(2)
                    3 -> return ReviewWriteSimpleFragment.newInstance(3)
                    else -> return ClubCategoryFragment.newInstance("write")
                }
            }else if(reviewType == ReviewType.INTERN){
                when (position) {
                    0 -> return ReviewWriteStartFragment.newInstance(0)
                    1 -> return ReviewWriteInternFieldFragment.newInstance(1)
                    2 -> return ReviewWriteScoreFragment.newInstance(2)
                    3 -> return ReviewWriteSimpleFragment.newInstance(3)
                    else -> return ReviewWriteStartFragment.newInstance(0)
                }
            }else{
                when (position) {
                    0 -> return ReviewWriteStartFragment.newInstance(0)
                    1 -> return ReviewWriteScoreFragment.newInstance(1)
                    2 -> return ReviewWriteSimpleFragment.newInstance(2)
                    else -> return ReviewWriteStartFragment.newInstance(0)
                }
            }
        } else {
            if(reviewType == ReviewType.INTERN){
                when (position) {
                    0 -> return ReviewWriteStartWithNameFragment.newInstance(0)
                    1 -> return ReviewWriteInternFieldFragment.newInstance(1)
                    2 -> return ReviewWriteScoreFragment.newInstance(2)
                    3 -> return ReviewWriteSimpleFragment.newInstance(3)
                    else -> return ReviewWriteStartFragment.newInstance(0)
                }
            }else {
                when (position) {
                    0 -> return ReviewWriteStartWithNameFragment.newInstance(0)
                    1 -> return ReviewWriteScoreFragment.newInstance(1)
                    2 -> return ReviewWriteSimpleFragment.newInstance(2)
                    else -> return ReviewWriteStartFragment.newInstance(3)
                }
            }
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun getCount(): Int = fragmentCount

}