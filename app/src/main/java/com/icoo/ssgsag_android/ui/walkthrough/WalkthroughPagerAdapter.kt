package com.icoo.ssgsag_android.ui.walkthrough

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sopt.appjam_sggsag.Fragment.Info.*

class WalkthroughPagerAdapter(fm: FragmentManager, val fragmentCount : Int): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FirstInfoFragment()
            1 -> return SecondInfoFragment()
            2 -> return ThirdInfoFragment()
            else -> return FirstInfoFragment()
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun getCount(): Int = fragmentCount
}