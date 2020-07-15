package com.icoo.ssgsag_android.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.ActivityMainBinding
import com.icoo.ssgsag_android.ui.main.MainActivity.mainActivityContext.mainContext
import com.icoo.ssgsag_android.ui.main.calendar.CalendarFragment
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.coachmark.FilterCoachmarkDialogFragment
import com.icoo.ssgsag_android.ui.main.feed.FeedFragment
import com.icoo.ssgsag_android.ui.main.review.main.ReviewMainFragment
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagViewModel
import com.icoo.ssgsag_android.util.listener.BackPressHandler
import com.igaworks.v2.core.AdBrixRm
import io.fabric.sdk.android.Fabric
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, SsgSagViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_main
    override val viewModel: SsgSagViewModel by viewModel()

    object mainActivityContext {
        lateinit var mainContext: Context
    }

    object GetWidth {
        var windowWidth = 0
    }


    interface onKeyBackPressedListener{
        fun onBack()
    }

    lateinit var backPressHandler : BackPressHandler

    var mOnKeyBackPressedListener : onKeyBackPressedListener? = null
    var isSsgSaged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        mainContext = this

        backPressHandler = BackPressHandler(this)
        Fabric.with(this, Crashlytics())

        //myAuth를 원래 토큰으로 사용했는데 2.0버전은 실수로 TOKEN이라고 지음... 이미 TOKEN을 받아서 사용하는것도 많고 myAuth를 받아서 사용하는것도
        //많아가지고 일단 이렇게 했슴다... 점진적으로 다른 클래스에서 서버통신할때 토큰받아오는거 TOKEN으로 수정합시당
        PreferenceManager(this).putPreference("TOKEN", SharedPreferenceController.getAuthorization(this))

        setViewPager()
        setTabLayout()
        getWindowWidth()

        if(intent.getStringExtra("param")!=null)
            goToCalendarDetail(intent.getStringExtra("param"))

        if(SharedPreferenceController.getIsFirstOpen(this)){
            showCoachMark()
        }
    }

    // 현재 필터설정만 존재
    private fun showCoachMark(){
        val dialogFragment = FilterCoachmarkDialogFragment()
        dialogFragment.show(supportFragmentManager, "frag_dialog_filter_coachmark")
    }

    private fun setViewPager() {

        //ViewPager
        viewDataBinding.actMainVp.run {
            adapter = BasePagerAdapter(supportFragmentManager).apply {
                addFragment(FeedFragment())
                addFragment(MainFragment())
                addFragment(CalendarFragment())
                addFragment(ReviewMainFragment())
                isSaveEnabled = false
            }
            currentItem = 1
            offscreenPageLimit = 3
        }
    }

    private fun setTabLayout() {
        //TabLayout
        val bottomNavigationLayout: View =
            LayoutInflater.from(this).inflate(R.layout.navigation_main, null)

        viewDataBinding.actMainTl.run {
            setupWithViewPager(viewDataBinding.actMainVp)
            getTabAt(0)!!.customView =
                bottomNavigationLayout.findViewById(R.id.top_navigation_rl_feed) as RelativeLayout
            getTabAt(1)!!.customView =
                bottomNavigationLayout.findViewById(R.id.top_navigation_rl_ssg_sag) as RelativeLayout
            getTabAt(2)!!.customView =
                bottomNavigationLayout.findViewById(R.id.top_navigation_rl_calendar) as RelativeLayout
            getTabAt(3)!!.customView =
                bottomNavigationLayout.findViewById(R.id.top_navigation_rl_review) as RelativeLayout

            setTabRippleColor(null)
        }
    }

    fun moveFragment(item: Int){
        viewDataBinding.actMainVp.currentItem = item
    }
    fun getCurrentPage(): Int{
        return viewDataBinding.actMainVp.currentItem
    }

    private fun goToCalendarDetail(posterIdx : String){
        val intent = Intent(this, CalendarDetailActivity::class.java)
        val bundle = Bundle().apply {
            putInt("Idx", posterIdx.toInt())
            putString("from","main")
        }

        bundle?.let {
            intent.putExtras(it)
        }

        startActivity(intent)

        //adbrix
        AdBrixRm.event("touchUp_PosterDetail",
            AdBrixRm.AttrModel().setAttrs("posterIdx",posterIdx.toLong()))
    }

    private fun getWindowWidth() {

        val display: Display = this.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

       GetWidth.windowWidth = size.x

    }


    fun setOnKeyBackPressedListener(listener: onKeyBackPressedListener?){

        mOnKeyBackPressedListener = listener

    }


    override fun onBackPressed() {
       // backPressHandler.onBackPressed()

        if(mOnKeyBackPressedListener != null){
            mOnKeyBackPressedListener!!.onBack()
            mOnKeyBackPressedListener = null
        }else{
            backPressHandler.onBackPressed()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }


    override fun onPause() {
        super.onPause()

        // 백그라운드로 가면 아예 프로세스 다시 시작
//        val mStartActivity = Intent(this, MainActivity::class.java)
//        val mPendingIntentId = 123456
//        val mPendingIntent = PendingIntent.getActivity(
//            this,
//            mPendingIntentId,
//            mStartActivity,
//            PendingIntent.FLAG_CANCEL_CURRENT
//        )
//        val mgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
//        System.exit(0)

    }

    companion object {
        private val TAG = "MainActivity"
    }

}
