package com.icoo.ssgsag_android.ui.main.community.review.photoViewPager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_photo_viewpager.*

class PhotoViewPagerActivity : AppCompatActivity(){

    private var photoList = arrayOf<String>()
    private var clickedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewpager)

        photoList = intent.getStringArrayExtra("photoList")
        clickedPosition = intent.getIntExtra("clickedPosition", 0)

        act_photo_viewpager_tv.text = (clickedPosition+1).toString() + "/" + photoList.size.toString()


        setVp()
        setButton()
    }

    private fun setVp(){
        act_photo_viewpager_vp.run{
            adapter = BasePagerAdapter(supportFragmentManager).apply {

                for(i in 0 until photoList.size){
                    var photoFragment = PhotoFragment.newInstance(photoList[i])
                    addFragment(photoFragment)
                }
                isSaveEnabled = false
            }

            addOnPageChangeListener(onPageChangeListener)
            currentItem = clickedPosition
        }


    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            act_photo_viewpager_tv.text = (position+1).toString() + "/" + photoList.size.toString()
        }
    }

    private fun setButton(){
        act_photo_viewpager_iv.setSafeOnClickListener {
            finish()
        }
    }


}