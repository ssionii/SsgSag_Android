package com.icoo.ssgsag_android.ui.main.myPage.career

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import com.icoo.ssgsag_android.R
import kotlinx.android.synthetic.main.activity_career.*

class CareerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_career)

        init()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun init() {
        //툴바
        setSupportActionBar(act_career_tb_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)

        //NavigationBar
        setNavigationBar()

        //intent

    }

    private fun setNavigationBar() {
        act_career_vp.adapter =
            CareerPagerAdapter(supportFragmentManager, 3)
        act_career_tl.setupWithViewPager(act_career_vp)

        val bottomNavigationLayout : View = this.layoutInflater.inflate(R.layout.career_navigation_bar, null, false)//inflate뷰를 붙여줌
        act_career_tl.getTabAt(0)!!.customView = bottomNavigationLayout.findViewById(R.id.career_navigation_bar_rl_activity) as RelativeLayout
        act_career_tl.getTabAt(1)!!.customView = bottomNavigationLayout.findViewById(R.id.career_navigation_bar_rl_award) as RelativeLayout
        act_career_tl.getTabAt(2)!!.customView = bottomNavigationLayout.findViewById(R.id.career_navigation_bar_rl_certificate) as RelativeLayout
    }

    override fun onActivityResult(requestCode: Int, resultCode : Int, data : Intent? ) {
            if (resultCode == RESULT_OK) {
                //setNavigationBar()

                if (data!!.getIntExtra("view", 0) == 0)
                    act_career_vp.setCurrentItem(0)
                else if (data!!.getIntExtra("view", 0) == 1)
                    act_career_vp.setCurrentItem(1)
                else
                    act_career_vp.setCurrentItem(2)
            }
    }
}
