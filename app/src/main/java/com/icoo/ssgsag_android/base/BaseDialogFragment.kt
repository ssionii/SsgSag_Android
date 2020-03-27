package com.icoo.ssgsag_android.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.icoo.ssgsag_android.R

abstract class BaseDialogFragment<T : ViewDataBinding, E : BaseViewModel> : DialogFragment() {
    lateinit var viewDataBinding: T

    abstract val layoutResID: Int
    abstract val viewModel: E

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context, R.style.Dialog);
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog.window!!.setStatusBarColor(context!!.getColor(R.color.white))

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResID, container, false)
        viewDataBinding.lifecycleOwner = this@BaseDialogFragment
        return viewDataBinding.root
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val transaction = manager.beginTransaction()
        val prevFragment = manager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }


}