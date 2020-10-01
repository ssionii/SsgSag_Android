package com.icoo.ssgsag_android.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.ActivityMainBinding
import com.icoo.ssgsag_android.databinding.ItemCalendarListBinding
import com.icoo.ssgsag_android.databinding.NavigationMainBinding
import com.icoo.ssgsag_android.ui.main.MainActivity.mainActivityContext.mainContext
import com.icoo.ssgsag_android.ui.main.calendar.CalendarFragment
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.coachmark.FilterCoachmarkDialogFragment
import com.icoo.ssgsag_android.ui.main.community.CommunityFragment
import com.icoo.ssgsag_android.ui.main.myPage.MyPageFragment
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagViewModel
import com.icoo.ssgsag_android.ui.main.userNotice.UserNoticeFragment
import com.icoo.ssgsag_android.util.listener.BackPressHandler
import org.jetbrains.anko.image
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

        //myAuth를 원래 토큰으로 사용했는데 2.0버전은 실수로 TOKEN이라고 지음... 이미 TOKEN을 받아서 사용하는것도 많고 myAuth를 받아서 사용하는것도
        //많아가지고 일단 이렇게 했슴다... 점진적으로 다른 클래스에서 서버통신할때 토큰받아오는거 TOKEN으로 수정합시당
        PreferenceManager(this).putPreference("TOKEN", SharedPreferenceController.getAuthorization(this))

        viewModel.getUserInfo()
        getWindowWidth()
        setViewPager()

        viewModel.userInfo.observe(this, Observer {
            setTabLayout(it.userProfileUrl)
        })

        setBottomNavigation()


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
                addFragment(MainFragment())
                addFragment(CalendarFragment())
                addFragment(CommunityFragment())
                addFragment(UserNoticeFragment())
                addFragment(MyPageFragment())
                isSaveEnabled = false
            }
            currentItem = 0
            offscreenPageLimit = 4
        }
    }

    private fun setTabLayout(profileUrl : String) {

        val viewGroup : ViewGroup = viewDataBinding.root as ViewGroup

        val naviBinding = DataBindingUtil.inflate<NavigationMainBinding>(LayoutInflater.from(this), R.layout.navigation_main, viewGroup, false)

        Glide.with(this)
            .load(profileUrl)
            .error(R.drawable.img_default_profile)
            .apply(RequestOptions().circleCrop())
            .into(naviBinding.naviMainIvMypage)

        viewDataBinding.actMainTl.run {
            setupWithViewPager(viewDataBinding.actMainVp)

            getTabAt(0)!!.customView = naviBinding.topNavigationRlHome
            getTabAt(1)!!.customView = naviBinding.topNavigationRlCalendar
            getTabAt(2)!!.customView = naviBinding.topNavigationRlCommunity
            getTabAt(3)!!.customView = naviBinding.topNavigationRlNotification
            getTabAt(4)!!.customView = naviBinding.topNavigationRlMypage

            setTabRippleColor(null)

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        3 -> {
                            viewDataBinding.actMainClNoticeCount.visibility = GONE
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        3 -> viewModel.getUserNoticeCount()
                    }
                }

            })
        }


    }

    private fun setBottomNavigation(){
        viewDataBinding.actMainClNoticeCount.bringToFront()
        viewModel.getUserNoticeCount()
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
