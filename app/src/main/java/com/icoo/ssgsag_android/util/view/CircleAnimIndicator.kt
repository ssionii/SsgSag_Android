package com.icoo.ssgsag_android.util.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import java.util.jar.Attributes

public class CircleAnimIndicator(context : Context, attrs : AttributeSet?) : LinearLayout(context, attrs) {

    private val itemMargin = 8
    private val animDuration = 250

    private var mDefaultCircle = 0
    private var mSelectCircle = 0

    private lateinit var imageDot : ArrayList<ImageView>


    /**
     * 기본 점 생성
     * @param count 점의 갯수
     * @param defaultCircle 점의 이미지
     */
    public fun createDotPanel(count : Int, defaultCircle : Int, selectCircle : Int) {

        mDefaultCircle = defaultCircle
        mSelectCircle = selectCircle

        imageDot = arrayListOf()

        for (i in 0 until count) {

            val dot = ImageView(context)
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.topMargin = itemMargin
            params.bottomMargin = itemMargin
            params.leftMargin = itemMargin
            params.rightMargin = itemMargin
            params.gravity = Gravity.CENTER

            dot.layoutParams = params
            dot.setImageResource(defaultCircle)
            dot.setTag(dot.id, false)

            imageDot.add(dot)
            this.addView(dot)

        }


        selectDot(0)
    }

    /**
     * 선택된 점 표시
     * @param position
     */
    public fun selectDot(position : Int) {
        for (i in 0 until imageDot.size) {
            if (i == position) {
                imageDot[i].setImageResource(mSelectCircle)
            } else {
//                if (imageDot[i].getTag(imageDot[i].id) as Boolean) {
                    imageDot[i].setImageResource(mDefaultCircle)
//                }
            }
        }
    }


    /**
     * 선택된 점의 애니메이션
     * @param view
     * @param startScale
     * @param endScale
     */
    public fun selectScaleAnim(view : View, startScale : Float, endScale : Float) {
        val  anim = ScaleAnimation(
            startScale, endScale,
            startScale, endScale,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f)
        anim.fillAfter = true
        anim.duration = animDuration.toLong()
        view.startAnimation(anim)
        view.setTag(view.id,true)
    }


    /**
     * 선택되지 않은 점의 애니메이션
     * @param view
     * @param startScale
     * @param endScale
     */
    public fun defaultScaleAnim(view : View, startScale : Float, endScale : Float) {
        val anim = ScaleAnimation(
            startScale, endScale,
            startScale, endScale,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f)
        anim.fillAfter = true
        anim.duration = animDuration.toLong()
        view.startAnimation(anim)
        view.setTag(view.id,false)
    }

}