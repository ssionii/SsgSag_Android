package com.icoo.ssgsag_android.ui.main.myPage

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityMyPageBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.app.Activity
import android.content.pm.ServiceInfo
import com.icoo.ssgsag_android.ui.main.myPage.accountMgt.AccountMgtActivity
import com.icoo.ssgsag_android.ui.main.myPage.career.CareerActivity
import com.icoo.ssgsag_android.ui.main.myPage.contact.ContactActivity
import com.icoo.ssgsag_android.ui.main.myPage.myReview.MyReviewActivity
import com.icoo.ssgsag_android.ui.main.myPage.notice.NoticeActivity
import com.icoo.ssgsag_android.ui.main.myPage.pushAlarm.PushAlarmActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.ServiceInfoActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MyPageActivity : BaseActivity<ActivityMyPageBinding, MyPageViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_my_page
    override val viewModel: MyPageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        setSafeOnclickListener()
    }

    private fun setSafeOnclickListener(){
        viewDataBinding.actMyPageIvCancel.setSafeOnClickListener {
            finish()
        }
        viewDataBinding.actMyPageLlReviewContainer.setSafeOnClickListener {
            startActivity<MyReviewActivity>()
        }
        viewDataBinding.actMyPageLlCareerContainer.setSafeOnClickListener {
            startActivity<CareerActivity>()
        }
        viewDataBinding.actMyPageLlAlarmContainer.setSafeOnClickListener {
            startActivity<PushAlarmActivity>()
        }
        viewDataBinding.actMyPageLlNoticeContainer.setSafeOnClickListener {
            startActivity<NoticeActivity>()
        }
        viewDataBinding.actMyPageLlInquiryContainer.setSafeOnClickListener {
            startActivity<ContactActivity>()
        }
        viewDataBinding.actMyPageLlServiceContainer.setSafeOnClickListener {
            startActivity<ServiceInfoActivity>()
        }
        viewDataBinding.actMyPageLlSettingContainer.setSafeOnClickListener {
            startActivity<AccountMgtActivity>()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfo()

    }

    override fun finish() {
        super.finish()

        overridePendingTransition(
            R.anim.anim_not_move,
            R.anim.anim_slide_out_left)
    }

    companion object {
        private val TAG = "MyPageActivity"
    }

}