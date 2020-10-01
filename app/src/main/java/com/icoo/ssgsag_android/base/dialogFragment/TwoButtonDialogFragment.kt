package com.icoo.ssgsag_android.base.dialogFragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.databinding.DialogFragmentTwoButtonBinding
import com.icoo.ssgsag_android.ui.main.community.review.ReviewListPageFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class TwoButtonDialogFragment : BaseDialogFragment<DialogFragmentTwoButtonBinding, EmptyViewModel>(){

    override val layoutResID: Int
        get() = R.layout.dialog_fragment_two_button

    override val viewModel: EmptyViewModel by viewModel()

    private lateinit var listener : TwoButtonDialogDismissListener

    override fun dismiss() {
        listener.onDialogDismissed()
        super.dismiss()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        arguments?.run{
            setText(
                getString("content"),
                getString("leftText"),
                getString("rightText")
            )
        }
        setButton()
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
                    if (event.action != KeyEvent.ACTION_DOWN) true
                    else {
                        listener.onDialogDismissed()
                        dismiss()
                        true // pretend we've processed it
                    }
                } else false // pass on to be processed as normal
            }
        })
    }


    private fun setText(content : String, leftText : String, rightText : String){
        viewDataBinding.dialogFragTwoButtonTvContent.text = content
        viewDataBinding.dialogFragTwoButtonTvLeft.text = leftText
        viewDataBinding.dialogFragTwoButtonTvRight.text = rightText
    }

    private fun setButton(){
        viewDataBinding.dialogFragTwoButtonTvLeft.setOnClickListener {
            listener.onLeftButtonClick()
        }

        viewDataBinding.dialogFragTwoButtonTvRight.setOnClickListener {
            listener.onRightButtonClick()
        }
    }

    interface TwoButtonDialogDismissListener {
        fun onLeftButtonClick()
        fun onRightButtonClick()
        fun onDialogDismissed()
    }

    fun setDialogDismissListener(listener : TwoButtonDialogDismissListener){
        this.listener = listener
    }

    companion object {
        fun newInstance(content : String, leftText : String, rightText : String): TwoButtonDialogFragment {
            val fragment = TwoButtonDialogFragment()
            val bundle = Bundle()
            bundle.putString("content", content)
            bundle.putString("leftText", leftText)
            bundle.putString("rightText", rightText)
            fragment.arguments = bundle
            return fragment
        }
    }

}