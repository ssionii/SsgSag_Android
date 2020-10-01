package com.icoo.ssgsag_android.ui.main.review.club.registration

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubCategoryFragment
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubContactFragment
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubDetailInfoFragment
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubSimpleInfoFragment

class ClubRgstrPagerAdapter(fm: FragmentManager, val fragmentCount : Int): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> return ClubCategoryFragment.newInstance("rgstr")
            1 -> return ClubSimpleInfoFragment()
            2 -> return ClubDetailInfoFragment()
            3 -> return ClubContactFragment()
            else -> return ClubCategoryFragment.newInstance("rgstr")
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun getCount(): Int = fragmentCount

}