package com.icoo.ssgsag_android.ui.main.coachmark

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseDialogFragment
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.DialogFragmentCoachmarkFilterBinding
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagViewModel
import com.icoo.ssgsag_android.ui.main.ssgSag.filter.SsgSagFilterActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.support.v4.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterCoachmarkDialogFragment : BaseDialogFragment<DialogFragmentCoachmarkFilterBinding, SsgSagViewModel>() {

    override val layoutResID: Int
    get() = R.layout.dialog_fragment_coachmark_filter
    override val viewModel: SsgSagViewModel by viewModel()

    override fun dismiss() {
        super.dismiss()
        SharedPreferenceController.setIsFirstOpen(activity!!, false)
    }

    override fun onResume() {
        super.onResume()

        dialog!!.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(
                dialog: DialogInterface, keyCode: Int,
                event: KeyEvent
            ): Boolean {

                return if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //This is the filter
                    if (event.action != KeyEvent.ACTION_DOWN)
                        true
                    else {
                        dismiss()
                        true // pretend we've processed it
                    }
                } else
                    false // pass on to be processed as normal
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

//        viewDataBinding.dialogFragCoachmarkFilterRl.setSafeOnClickListener {
//            dismiss()
//        }

        viewDataBinding.dialogFragCoachmarkFilterIv.setSafeOnClickListener {
            startActivity<SsgSagFilterActivity>()
            dismiss()
        }
    }

}
