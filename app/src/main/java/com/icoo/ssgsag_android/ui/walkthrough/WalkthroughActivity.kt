package com.icoo.ssgsag_android.ui.walkthrough

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import com.icoo.ssgsag_android.R
import kotlinx.android.synthetic.main.activity_walkthrough.*
import com.airbnb.lottie.LottieAnimationView


class WalkthroughActivity : AppCompatActivity() {

    var lottieAnimationView:LottieAnimationView ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)

        setProgress()
    }

    private fun setProgress() {
        act_info_vp.adapter = WalkthroughPagerAdapter(supportFragmentManager, 3)
        act_info_tl.setupWithViewPager(act_info_vp)

        val infoNavigationLayout : View = this.layoutInflater.inflate(R.layout.info_navigation_bar, null, false)//inflate뷰를 붙여줌
        act_info_tl.getTabAt(0)!!.customView = infoNavigationLayout.findViewById(R.id.info_navi_rl_progress_1) as RelativeLayout
        act_info_tl.getTabAt(1)!!.customView = infoNavigationLayout.findViewById(R.id.info_navi_rl_progress_2) as RelativeLayout
        act_info_tl.getTabAt(2)!!.customView = infoNavigationLayout.findViewById(R.id.info_navi_rl_progress_3) as RelativeLayout
          }

}
