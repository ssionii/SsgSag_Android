package com.icoo.ssgsag_android.util.view

import android.content.Context
import android.icu.util.Measure
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class AutoHeightViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var tempHeightMeasureSpec = heightMeasureSpec

        val mode = MeasureSpec.getMode(tempHeightMeasureSpec)
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            super.onMeasure(widthMeasureSpec, tempHeightMeasureSpec)

            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val h = child.measuredHeight
                if (h > height) height = h
            }

            tempHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, tempHeightMeasureSpec)
    }
}