package com.icoo.ssgsag_android.ui.main.coachmark

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.dialogFragment.BaseDialogFragment
import com.icoo.ssgsag_android.databinding.DialogFragmentCoachmarkFilterBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagViewModel
import com.icoo.ssgsag_android.ui.main.ssgSag.filter.SsgSagFilterActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.support.v4.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterCoachmarkDialogFragment : BaseDialogFragment<DialogFragmentCoachmarkFilterBinding, SsgSagViewModel>(){

    override val layoutResID: Int
    get() = R.layout.dialog_fragment_coachmark_filter
    override val viewModel: SsgSagViewModel by viewModel()

    override fun dismiss() {
        super.dismiss()
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

                        val activity = activity as MainActivity
                        activity.setOnKeyBackPressedListener(null)
                        activity.onBackPressed()

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


        viewDataBinding.dialogFragCoachmarkFilterIv.setSafeOnClickListener {
            startActivity<SsgSagFilterActivity>()
            dismiss()
        }
    }

}
