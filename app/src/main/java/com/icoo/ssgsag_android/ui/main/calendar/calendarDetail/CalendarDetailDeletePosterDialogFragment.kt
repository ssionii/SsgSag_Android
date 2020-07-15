package com.icoo.ssgsag_android.ui.main.calendar.calendarDetail

import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage.CalendarDialogPageViewModel


import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseDialogFragment
import com.icoo.ssgsag_android.databinding.DialogFragmentCalendarDetailDeletePosterBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarDetailDeletePosterDialogFragment : BaseDialogFragment<DialogFragmentCalendarDetailDeletePosterBinding, CalendarDetailViewModel>() {
    override val layoutResID: Int
        get() = R.layout.dialog_fragment_calendar_detail_delete_poster
    override val viewModel: CalendarDetailViewModel by viewModel()
    lateinit var listener : OnDialogDismissedListener

    private var isDeleted = false

    private var text = ""

    fun setOnDialogDismissedListener(listener: OnDialogDismissedListener){
        this.listener = listener
    }
    interface OnDialogDismissedListener {
        fun onDialogDismissed(isDeleted:Boolean)
    }
    override fun dismiss() {
        listener.onDialogDismissed(isDeleted)
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
                        listener.onDialogDismissed(isDeleted)
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

        if(text != ""){
            viewDataBinding.dialogFragmentTv.text = text
        }

        setButton()
    }

    fun setTextView(text : String){
        this.text = text
    }

    private fun setButton(){

        viewDataBinding.dialogFragmentTvCancel.setSafeOnClickListener {
            isDeleted = false
            this.dismiss()
        }

        viewDataBinding.dialogFragmentTvOk.setSafeOnClickListener {
            isDeleted = true
            this.dismiss()
        }
    }
}