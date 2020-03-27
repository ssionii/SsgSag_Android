package com.icoo.ssgsag_android.ui.main.myPage.career

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.myPage.career.activity.ActivityFragment
import com.icoo.ssgsag_android.ui.main.myPage.career.award.AwardFragment
import com.icoo.ssgsag_android.ui.main.myPage.career.certification.CertificationFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.fragment_career_delete.*

class CareerDeleteDialogFragment: androidx.fragment.app.DialogFragment(){
    var idx: Int? = null
    var category: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_career_delete, container,false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        frag_career_delete_tv_yes.setSafeOnClickListener {
            when (category) {
                1 -> (parentFragment as ActivityFragment).deleteCareerResponse(idx!!)
                2 -> (parentFragment as AwardFragment).deleteCareerResponse(idx!!)
                3 -> (parentFragment as CertificationFragment).deleteCareerResponse(idx!!)
            }
            dismiss()
        }

        frag_career_delete_tv_cancel.setSafeOnClickListener {
            dismiss()
        }
    }
}
