package com.icoo.ssgsag_android.ui.main.myPage.career

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.icoo.ssgsag_android.ui.main.myPage.career.activity.ActivityFragment
import com.icoo.ssgsag_android.ui.main.myPage.career.award.AwardFragment
import com.icoo.ssgsag_android.ui.main.myPage.career.certification.CertificationFragment

class CareerPagerAdapter(fm: FragmentManager, val fragmentCount : Int): FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return ActivityFragment()
            1 -> return AwardFragment()
            2 -> return CertificationFragment()
            else -> return ActivityFragment()
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun getCount(): Int = fragmentCount

}