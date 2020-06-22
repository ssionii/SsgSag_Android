package com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage


import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseDialogFragment
import com.icoo.ssgsag_android.databinding.DialogFragmentScheduleDeleteBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

class CalendarDialogPageDeleteDialogFragment : BaseDialogFragment<DialogFragmentScheduleDeleteBinding, CalendarDialogPageViewModel>() {
    override val layoutResID: Int
        get() = R.layout.dialog_fragment_schedule_delete
    override val viewModel: CalendarDialogPageViewModel by viewModel()
    lateinit var listener : OnDialogDismissedListener

    private var isDeleted = false

    private var posterIdxList = arrayListOf<Int>()
    private var name =""

    fun setOnDialogDismissedListener(listener: OnDialogDismissedListener){
        this.listener = listener
    }
    interface OnDialogDismissedListener {
        fun onDialogDismissed(isDeleted:Boolean, posterIdx:ArrayList<Int>)
    }
    override fun dismiss() {
        listener.onDialogDismissed(isDeleted, posterIdxList)
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
                        listener.onDialogDismissed(isDeleted, posterIdxList)
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

        setButton()
        setName()
    }

    fun setInfo(posterIdx: Int, name:String){
        posterIdxList.add(posterIdx)
        this.name = name
    }

    fun setInfo(posterIdxList: ArrayList<Int>, posterNameList: ArrayList<String>){
        this.posterIdxList = posterIdxList
        this.name = posterNameList[0]
    }

    private fun setButton(){

        viewDataBinding.dialogFragScheduleDeleteCancel.setSafeOnClickListener {
            isDeleted = false
            posterIdxList.clear()
            this.dismiss()
        }

        viewDataBinding.dialogFragScheduleDeleteDelete.setSafeOnClickListener {
            isDeleted = true
            this.dismiss()
        }
    }

    fun setName(){
        if(posterIdxList.size == 1) {
            viewDataBinding.dialogFragScheduleDeleteName.text = name
            viewDataBinding.dialogFragScheduleDeleteMent.text = "해당 일정을 삭제하시겠어요?"
        }else if(posterIdxList.size > 1){
            viewDataBinding.dialogFragScheduleDeleteMent.text =
                posterIdxList.size.toString()  + "개의 일정을 삭제하시겠어요?"
            viewDataBinding.dialogFragScheduleDeleteName.textSize = 0.0f
        }
    }

    companion object {
        private val TAG = "SubscribeInternDialogFragment"

    }
}