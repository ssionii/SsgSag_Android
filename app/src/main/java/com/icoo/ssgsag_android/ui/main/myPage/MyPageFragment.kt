package com.icoo.ssgsag_android.ui.main.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentMyPageBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.ui.main.myPage.accountMgt.AccountMgtActivity
import com.icoo.ssgsag_android.ui.main.myPage.career.CareerActivity
import com.icoo.ssgsag_android.ui.main.myPage.contact.ContactActivity
import com.icoo.ssgsag_android.ui.main.myPage.notice.NoticeActivity
import com.icoo.ssgsag_android.ui.main.myPage.pushAlarm.PushAlarmActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.ServiceInfoActivity
import org.jetbrains.anko.support.v4.startActivity


class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_my_page
    override val viewModel: MyPageViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setOnclickListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfo()
    }

    private fun setOnclickListener(){

        viewDataBinding.fragMyPageLlCareerContainer.setOnClickListener {
            startActivity<CareerActivity>()
        }

        viewDataBinding.fragMyPageLlMypostContainer.setOnClickListener {

        }

        viewDataBinding.fragMyPageLlBookmarkContainer.setOnClickListener {

        }

        viewDataBinding.fragMyPageLlNoticeContainer.setOnClickListener {
            startActivity<NoticeActivity>()
        }

        viewDataBinding.fragMyPageLlServiceContainer.setOnClickListener {
            startActivity<ServiceInfoActivity>()
        }

        viewDataBinding.fragMyPageLlAlarmContainer.setOnClickListener {
            startActivity<PushAlarmActivity>()
        }

        viewDataBinding.fragMyPageLlSettingContainer.setOnClickListener {
            startActivity<AccountMgtActivity>()
        }

        viewDataBinding.fragMyPageCvFeedback.setOnClickListener {
            startActivity<ContactActivity>()
        }
    }


}