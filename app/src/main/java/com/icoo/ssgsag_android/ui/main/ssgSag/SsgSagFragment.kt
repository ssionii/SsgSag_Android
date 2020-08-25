package com.icoo.ssgsag_android.ui.main.ssgSag

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.FragmentSsgSagBinding
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.photoEnlarge.PhotoExpandActivity
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import org.jetbrains.anko.support.v4.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.SimpleItemAnimator
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersFragment
import com.icoo.ssgsag_android.ui.main.ssgSag.todaySwipePoster.TodaySwipePosterActivity
import com.icoo.ssgsag_android.ui.main.ssgSag.todaySwipePoster.TodaySwipePosterViewModel


class SsgSagFragment : BaseFragment<FragmentSsgSagBinding, SsgSagViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_ssg_sag
    override val viewModel: SsgSagViewModel by viewModel()

    private var ssgSagCardStackAdapter = SsgSagCardStackAdapter(SsgSagApplication.getGlobalApplicationContext())

    lateinit var firebaseAnalytics : FirebaseAnalytics

    val todaySsgSagViewModel : TodaySwipePosterViewModel by viewModel()

    private var position = 0
    private var ssgsag = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ssgsag = false
        viewDataBinding.vm = viewModel

        firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!)

        ssgSagCardStackAdapter.setHasStableIds(true)
        setSsgSagCardStackView()


        navigator()
        setEndPage()
        setButton()
        setTodaySwipePosterBtn()

        if(!SharedPreferenceController.getSsgSagCoachMark(activity!!))
            setCoachMark()
    }

    override fun onResume() {
        super.onResume()

        position = 0
        viewModel.getAllPosters()

        // todo: 이거 서버에서 미리 값을 받아와야 함
        todaySsgSagViewModel.getTodaySwipePosterSize()

    }



    private fun setSsgSagCardStackView() {
        //CardStackView
        viewDataBinding.fragSsgSagCv.apply {
            adapter = ssgSagCardStackAdapter.apply {
                setOnnSsgSagItemClickListener(ssgSagListener)
                (itemAnimator as SimpleItemAnimator).run {
                    changeDuration = 0
                    supportsChangeAnimations = false
                }}
            layoutManager = CardStackLayoutManager(activity, cardStackListener).apply {
                setVisibleCount(3)
                setDirections(Direction.HORIZONTAL)
                setCanScrollHorizontal(true)
                setCanScrollVertical(false)
            }
        }


        viewModel.allPosters.observe(this@SsgSagFragment, Observer { value ->
            if(value.size != 0){
                viewDataBinding.fragSsgSagCv.apply{
                    visibility = GONE

                    (adapter as SsgSagCardStackAdapter).apply {
                        replaceAll(value)
                        notifyDataSetChanged()
                    }

                    viewDataBinding.fragSsgSagCv.visibility = VISIBLE
                }
            }else
                viewDataBinding.fragSsgSagCv.visibility = GONE

        })
    }

    private val cardStackListener = object : CardStackListener {
        override fun onCardDisappeared(view: View?, position: Int) {}
        override fun onCardDragging(direction: Direction?, ratio: Float) {}
        override fun onCardSwiped(direction: Direction?) {
            if (direction == Direction.Right) {
                if(position < viewModel.allPosters.value!!.size) {
                    viewModel.ssgSag(viewModel.allPosters.value!![position].posterIdx, 1)
                }
            } else if (direction == Direction.Left) {
                if(position < viewModel.allPosters.value!!.size) {
                    viewModel.ssgSag(viewModel.allPosters.value!![position].posterIdx, 0)
                }
            }
            viewModel.posterCountDown()
            (activity as MainActivity).isSsgSaged = true
            position++
            ssgsag = true

            todaySsgSagViewModel.getTodaySwipePosterSize()
        }

        override fun onCardCanceled() {}
        override fun onCardAppeared(view: View?, position: Int) {}
        override fun onCardRewound() {}
    }

    private val ssgSagListener = object : SsgSagCardStackAdapter.OnSsgSagItemClickListener {
        override fun onEnlargeClicked(photoUrl: String) {
            val intent = Intent(activity!!, PhotoExpandActivity::class.java)
            intent.putExtra("photoUrl", photoUrl)
            startActivity(intent)
        }
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(activity, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            view!!.context.startActivity(intent)

        })
    }

    private fun setButton() {

        viewDataBinding.fragSsgSagCvGoTodaySsgsag.setSafeOnClickListener {
            startActivity<TodaySwipePosterActivity>()
        }

    }

    private fun setEndPage() {

        viewModel.posterCount.observe(this@SsgSagFragment, Observer { value ->
            viewDataBinding.fragSsgSagTvSsgSagCount.text = value.toString()

            if(value == 0){
                viewDataBinding.fragSsgSagRlEnd.visibility = VISIBLE
                if(ssgsag) logEVENT_NAME_SWIPE_SUCCESSEvent()


                viewDataBinding.fragSsgSagRlCount.visibility = GONE

                viewModel.getUserCnt()

            }else if(viewDataBinding.fragSsgSagRlEnd.visibility == VISIBLE && viewModel.posterCount.value!! != 0) {
                viewDataBinding.fragSsgSagRlEnd.visibility = GONE
                viewDataBinding.fragSsgSagRlCount.visibility = VISIBLE
            }
        })

    }

    private fun setTodaySwipePosterBtn(){
        todaySsgSagViewModel.totalSize.observe(this, Observer { value ->
            if(value > 0){
                viewDataBinding.fragSsgSagCvGoTodaySsgsag.setCardBackgroundColor(activity!!.resources.getColor(R.color.ssgsag))
                viewDataBinding.fragSsgSagCvGoTodaySsgsag.isClickable = true
            }else{
                viewDataBinding.fragSsgSagCvGoTodaySsgsag.setCardBackgroundColor(activity!!.resources.getColor(R.color.grey_3))
                viewDataBinding.fragSsgSagCvGoTodaySsgsag.isClickable = false
            }
        })
    }


    private fun setCoachMark(){

        viewDataBinding.fragSsgsagClCoachmarkContainer.visibility = View.VISIBLE

        var isShown = false

        viewModel.posterCount.observe(this, Observer {
            if(it > 0 && !isShown) {
                viewDataBinding.fragSsgsagClCoachmarkContainer.visibility = VISIBLE
                isShown = true
            }

        })

        viewDataBinding.fragSsgsagClCoachmarkContainer.setOnTouchListener( object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                viewDataBinding.fragSsgsagClCoachmarkContainer.visibility = GONE
                viewDataBinding.fragSsgsagClCoachmark2Container.visibility = VISIBLE
                return false
            }
        })


        viewDataBinding.fragSsgsagClCoachmark2Container.setOnTouchListener( object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
               return true
            }
        })

        viewDataBinding.fragSsgsagClCoachmark2.setOnClickListener {
            viewDataBinding.fragSsgsagClCoachmark2Container.visibility = View.GONE

            SharedPreferenceController.setSsgSagCoachMark(activity!!, true)
        }

    }

    private fun logEVENT_NAME_SWIPE_SUCCESSEvent() {
        val logger = AppEventsLogger.newLogger(activity)
        logger.logEvent("SWIPE_SUCCESS")

        val params = Bundle()
        firebaseAnalytics.logEvent("SWIPE_SUCCESS", params)

        val adjustEvent = AdjustEvent("j8ehc8")
        Adjust.trackEvent(adjustEvent)
    }

    private fun logEVENT_NAME_TUTORIALS_COMPLETEEvent() {
        val logger = AppEventsLogger.newLogger(activity)
        logger.logEvent("TUTORIALS_COMPLETE")

        val params = Bundle()
        firebaseAnalytics.logEvent("TUTORIALS_COMPLETE", params)

        val adjustEvent = AdjustEvent("iy1h7x")
        Adjust.trackEvent(adjustEvent)
    }

    companion object {
        private val TAG = "SsgSagFragment"

        fun newInstance(): SsgSagFragment {
            val fragment = SsgSagFragment()
            return fragment
        }
    }
}