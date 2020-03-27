package com.icoo.ssgsag_android.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.os.Parcelable



class BasePagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){

    private val setOfFragments: MutableSet<Fragment> = mutableSetOf()

    override fun getItem(position: Int): Fragment {
        return setOfFragments.elementAt(position)
    }
    override fun getCount(): Int = setOfFragments.size

    override fun saveState(): Parcelable? {
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    fun addFragment(fragment: Fragment) {
        setOfFragments.add(fragment)
        notifyDataSetChanged()
    }


}