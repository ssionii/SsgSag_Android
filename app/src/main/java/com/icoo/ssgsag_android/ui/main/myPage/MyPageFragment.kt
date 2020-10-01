package com.icoo.ssgsag_android.ui.main.myPage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentMyPageBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.ui.main.myPage.accountMgt.AccountMgtActivity
import com.icoo.ssgsag_android.ui.main.myPage.career.CareerActivity
import com.icoo.ssgsag_android.ui.main.myPage.contact.ContactActivity
import com.icoo.ssgsag_android.ui.main.myPage.myBoard.MyBoardActivity
import com.icoo.ssgsag_android.ui.main.myPage.notice.NoticeActivity
import com.icoo.ssgsag_android.ui.main.myPage.pushAlarm.PushAlarmActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.ServiceInfoActivity
import org.jetbrains.anko.support.v4.startActivity


class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_my_page
    override val viewModel: MyPageViewModel by viewModel()

    val accountRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode

        if(resultCode == Activity.RESULT_OK) {
            (activity as MainActivity).viewModel.getUserInfo()
        }
    }

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
            val intent = Intent(requireActivity(), MyBoardActivity::class.java)
            intent.putExtra("type", "post")
            startActivity(intent)
        }

        viewDataBinding.fragMyPageLlBookmarkContainer.setOnClickListener {
            val intent = Intent(requireActivity(), MyBoardActivity::class.java)
            intent.putExtra("type", "bookmark")
            startActivity(intent)
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

            val intent = Intent(requireActivity(), AccountMgtActivity::class.java)
            accountRequest.launch(intent)
        }

        viewDataBinding.fragMyPageLlFeedback.setOnClickListener {
            startActivity<ContactActivity>()
        }

        viewDataBinding.fragMyPageCvFeedback.setOnClickListener {
            startActivity<ContactActivity>()
        }
    }


}