package com.icoo.ssgsag_android.ui.main.review.club.edit

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.dialog_fragment_review_edit.*
import java.util.*

class ClubReviewEditDialogFragment : DialogFragment() {

    lateinit var listener : OnDialogDismissedListener

    fun setOnDialogDismissedListener(listener: OnDialogDismissedListener){
        this.listener = listener
    }
    interface OnDialogDismissedListener {
        fun onDeleteDialogDismissed(idx: Int, position: Int)
        fun onEditDialogDismissed(item: ClubPost?, position: Int)
    }

    var usage = "edit"
    var idx = 0
    var item : ClubPost? = null
    var position = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dialog_fragment_review_edit, container, false)

        v.findViewById<TextView>(R.id.dialog_review_edit_tv_cancel).setSafeOnClickListener { dismiss()}

        if(usage == "delete"){
            v.findViewById<TextView>(R.id.dialog_review_edit_tv_title).text = "후기를 정말 삭제하시겠습니까?"
            v.findViewById<TextView>(R.id.dialog_review_edit_tv_text).text = "삭제된 후기는 복구가 불가능합니다.\n" + "신중히 결정해주세요 \uD83D\uDE2D"
            v.findViewById<TextView>(R.id.dialog_review_edit_tv_edit).apply{
                text = "삭제"
                setSafeOnClickListener {
                    listener.onDeleteDialogDismissed(idx, position)
                    dismiss()
                }
            }
        }else{
            v.findViewById<TextView>(R.id.dialog_review_edit_tv_edit).setSafeOnClickListener {
                listener.onEditDialogDismissed(item, position)
                dismiss()
            }
        }

        return v
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

}