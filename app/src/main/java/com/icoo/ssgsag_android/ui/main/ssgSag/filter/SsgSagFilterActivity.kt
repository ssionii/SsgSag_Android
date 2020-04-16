package com.icoo.ssgsag_android.ui.main.ssgSag.filter

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.databinding.ActivitySsgsagFilterBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.databinding.ItemClubRgstrCategoryBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.util.listener.BackPressHandler
import com.icoo.ssgsag_android.util.view.NonScrollLinearLayoutManager
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager


class SsgSagFilterActivity : BaseActivity<ActivitySsgsagFilterBinding, SsgSagFilterViewModel>(),  BaseRecyclerViewAdapter.OnItemClickListener {

    override val layoutResID: Int
        get() = R.layout.activity_ssgsag_filter
    override val viewModel: SsgSagFilterViewModel by viewModel()

    lateinit var firebaseAnalytics : FirebaseAnalytics

    lateinit var backPressHandler : BackPressHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setButton()
        setRv()
        refreshRv()

        viewModel.jobFilter.observe(this, Observer {
            viewDataBinding.actSsgsagFilterRvJob.adapter?.notifyDataSetChanged()
        })

        viewModel.fieldFilter.observe(this, Observer {
            viewDataBinding.actSsgsagFilterRvField.adapter?.notifyDataSetChanged()
        })
    }

    private fun setButton(){

        if(!SharedPreferenceController.getIsFirstOpen( this)) {
            viewDataBinding.actSsgsagFilterIvBack.setSafeOnClickListener {
                finish()
            }
        }

        viewDataBinding.actSsgsagFilterRlDone.setSafeOnClickListener {
            if(!viewModel.combineInterest())
                toast("각 항목을 1개 이상 선택해주세요")
            else {
                viewModel.combineInterest()
                logEVENT_NAME_CUSTOMIZED_FILTEREvent()
                SharedPreferenceController.setIsFirstOpen( this, false)
                finish()

            }
        }
    }

    private fun setRv(){

        viewDataBinding.actSsgsagFilterRvInfo.apply{
            adapter =
                object: BaseRecyclerViewAdapter<Category, ItemClubRgstrCategoryBinding>(){
                    override val layoutResID: Int
                        get() = R.layout.item_club_rgstr_category
                    override val bindingVariableId: Int
                        get() = BR.category
                    override val listener: OnItemClickListener?
                        get() = null
                }
            layoutManager = WrapContentLinearLayoutManager(RecyclerView.HORIZONTAL)
        }

        viewDataBinding.actSsgsagFilterRvField.apply {
            adapter =
                object: BaseRecyclerViewAdapter<Category, ItemClubRgstrCategoryBinding>(){
                    override val layoutResID: Int
                        get() = R.layout.item_club_rgstr_category
                    override val bindingVariableId: Int
                        get() = BR.category
                    override val listener: OnItemClickListener?
                        get() = this@SsgSagFilterActivity
                }
            layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL)
            //addItemDecoration(SpacesItemDecoration(3, 24))

        }

        viewDataBinding.actSsgsagFilterRvEnterprise.apply {
            adapter =
                object: BaseRecyclerViewAdapter<Category, ItemClubRgstrCategoryBinding>(){
                    override val layoutResID: Int
                        get() = R.layout.item_club_rgstr_category
                    override val bindingVariableId: Int
                        get() = BR.category
                    override val listener: OnItemClickListener?
                        get() = this@SsgSagFilterActivity
                }
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        }


        viewDataBinding.actSsgsagFilterRvJob.apply {
            adapter =
                object: BaseRecyclerViewAdapter<Category, ItemClubRgstrCategoryBinding>(){
                    override val layoutResID: Int
                        get() = R.layout.item_club_rgstr_category
                    override val bindingVariableId: Int
                        get() = BR.category
                    override val listener: OnItemClickListener?
                        get() = this@SsgSagFilterActivity
                }
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)

        }
    }

    private fun refreshRv() {

        viewModel.userInfo.observe(this, Observer { value ->
            (viewDataBinding.actSsgsagFilterRvInfo.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                replaceAll(value)
                notifyDataSetChanged()
            }
        })

        viewModel.fieldFilter.observe(this, Observer { value ->
            (viewDataBinding.actSsgsagFilterRvField.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                replaceAll(value)
                notifyDataSetChanged()
            }
        })

        viewModel.enterpriseFilter.observe(this, Observer { value ->
            (viewDataBinding.actSsgsagFilterRvEnterprise.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                replaceAll(value)
                notifyDataSetChanged()
            }

        })

        viewModel.jobFilter.observe(this, Observer { value ->
            (viewDataBinding.actSsgsagFilterRvJob.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                replaceAll(value)
                notifyDataSetChanged()
            }

        })
    }


    override fun onItemClicked(item: Any?, position: Int?){
        viewModel.clickInterest(position!!, (item as Category).categoryIdx)
    }

    private fun logEVENT_NAME_CUSTOMIZED_FILTEREvent() {
        val logger = AppEventsLogger.newLogger(this)
        logger.logEvent("CUSTOMIZED_FILTER")

        val params = Bundle()
        firebaseAnalytics.logEvent("CUSTOMIZED_FILTER", params)

        val adjustEvent = AdjustEvent("i499qb")
        Adjust.trackEvent(adjustEvent)
    }

    override fun onBackPressed() {
        if(SharedPreferenceController.getIsFirstOpen( this))
            backPressHandler.onBackPressed()


    }

    companion object {
        private val TAG = "SsgSagFilterActivity"

    }
}