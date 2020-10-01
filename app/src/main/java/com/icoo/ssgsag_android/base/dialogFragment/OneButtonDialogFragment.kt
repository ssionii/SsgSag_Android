package com.icoo.ssgsag_android.base.dialogFragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.databinding.DialogFragmentOneButtonBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class OneButtonDialogFragment : BaseDialogFragment<DialogFragmentOneButtonBinding, EmptyViewModel>(){

    override val layoutResID: Int
        get() = R.layout.dialog_fragment_one_button

    override val viewModel: EmptyViewModel by viewModel()

    private lateinit var listener : OneButtonDialogDismissListener

    override fun dismiss() {
        listener.onDialogDismissed()
        super.dismiss()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.run{
            setText(
                getString("content"),
                getString("text")
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


    private fun setText(content : String, text : String){
        viewDataBinding.dialogFragOneButtonTvContent.text = content
        viewDataBinding.dialogFragOneButtonTv.text = text
    }

    private fun setButton(){
        viewDataBinding.dialogFragOneButtonTv.setOnClickListener {
            listener.buttonClick()
            dismiss()
        }
    }

    interface OneButtonDialogDismissListener {
        fun buttonClick()
        fun onDialogDismissed()
    }

    fun setDialogDismissListener(listener : OneButtonDialogDismissListener){
        this.listener = listener
    }

    companion object {
        fun newInstance(content : String, text : String): OneButtonDialogFragment {
            val fragment = OneButtonDialogFragment()
            val bundle = Bundle()
            bundle.putString("content", content)
            bundle.putString("text", text)
            fragment.arguments = bundle
            return fragment
        }
    }

}