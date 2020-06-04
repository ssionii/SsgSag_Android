package com.icoo.ssgsag_android.ui.main.calendar.calendarDialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage.CalendarDialogPageFragment
import java.text.SimpleDateFormat
import java.util.*

class CalendarDialogPagerAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val frgMap : HashMap<Int, CalendarDialogPageFragment>
    private val listDayByMillis = ArrayList<Long>()
    private val isPageChange = false
    private var numOfDay : Int = 0
    private var categoryList : ArrayList<Int> = arrayListOf()
    private var showFavorite = false

    init{
        clearPrevFragments(fm)
        frgMap = HashMap()
    }

    private fun clearPrevFragments(fm: FragmentManager) {
        val listFragment = fm.fragments

        if (listFragment != null) {
            val ft = fm.beginTransaction()

            for (f in listFragment) {
                if (f is CalendarDialogPageFragment) {
                    ft.remove(f)
                }
            }
            ft.commitAllowingStateLoss()
        }
    }

    override fun getItem(position: Int) : Fragment{
        var frg : CalendarDialogPageFragment ? = null
        if (frgMap.size > 0) {
            frg = frgMap[position]
        }
        if (frg == null) {
            frg = CalendarDialogPageFragment.newInstance(position)
            frgMap[position] = frg;
        }
        frg.setTimeByMillis(listDayByMillis[position])
        frg.setShowFavorite(showFavorite)
        return frg
    }

    override fun getCount() : Int{
        return listDayByMillis.size
    }

    fun setNumOfDay(calendar : Calendar) {
        this.numOfDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DATE, 1)

        for (i in 0 until numOfDay) {
            listDayByMillis.add(calendar.timeInMillis)
            calendar.add(Calendar.DATE, 1)
        }
        notifyDataSetChanged()
    }

    fun setShowFavorite (favorite: Boolean){
        this.showFavorite = favorite
    }

    fun addNext(calendar : Calendar) {
        val lastDayMillis = listDayByMillis[listDayByMillis.size - 1]
        this.numOfDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendar.timeInMillis = lastDayMillis
        for (i in 0 until numOfDay) {
            calendar.add(Calendar.DATE, 1)
            listDayByMillis.add(calendar.timeInMillis)
        }

        notifyDataSetChanged()
    }

    fun addPrev(calendar : Calendar) {
        val lastDayMillis = listDayByMillis[0]
        this.numOfDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendar.timeInMillis = lastDayMillis
        //schedule.set(Schedule.DATE, 1)
        for (i in numOfDay downTo 1) {
            calendar.add(Calendar.DATE, -1)
            listDayByMillis.add(0, calendar.timeInMillis)
        }
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any) : Int{
        return PagerAdapter.POSITION_NONE
    }

    fun getDayDisplayed(position : Int) : String{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = listDayByMillis[position]

        val yearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
        val monthFormat = SimpleDateFormat("MM", Locale.KOREA)
        val dateFormat = SimpleDateFormat("dd", Locale.KOREA)

        val date = Date()
        date.time = listDayByMillis[position]

        val year = yearFormat.format(date).toInt()
        val month = monthFormat.format(date).toInt()
        val day = dateFormat.format(date).toInt()

        return "${year}년 ${month}월 ${day}일"
    }
}