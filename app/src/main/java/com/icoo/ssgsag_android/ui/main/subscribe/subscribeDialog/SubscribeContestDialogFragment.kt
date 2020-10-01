package com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog

import com.icoo.ssgsag_android.databinding.DialogFragmentContestFilterBinding
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.dialogFragment.BaseDialogFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeFilter
import com.icoo.ssgsag_android.databinding.ItemFilterMatchParentBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import com.icoo.ssgsag_android.util.view.SpacesItemDecoration
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubscribeContestDialogFragment : BaseDialogFragment<DialogFragmentContestFilterBinding, SubscribeDialogViewModel>() ,  BaseRecyclerViewAdapter.OnItemClickListener{
    override val layoutResID: Int
        get() = R.layout.dialog_fragment_contest_filter
    override val viewModel: SubscribeDialogViewModel by viewModel()
    lateinit var listener : OnDialogDismissedListener

    private var isDone = false

    fun setOnDialogDismissedListener(listener: OnDialogDismissedListener){
        this.listener = listener
    }
    interface OnDialogDismissedListener {
        fun onDialogDismissed(isDone: Boolean)
    }
    override fun dismiss() {
        listener.onDialogDismissed(isDone)
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
                        listener.onDialogDismissed(isDone)
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

        setRv()
        refresh()
        setButton()

        viewModel.interest.observe(this, androidx.lifecycle.Observer {
            viewDataBinding.dialogFragContestFilterRv.adapter?.notifyDataSetChanged()
        })
    }




    private fun setButton(){
        viewDataBinding.dialogFragContestFilterRlSelectAll.setSafeOnClickListener {
            viewModel.selectAll("contest")

            // databinding 이용해서 해야하는데 ㅜㅡㅜ 일단 시간없음
            if(viewModel.selectedAllContest) {
                viewDataBinding.dialogFragContestFilterIvSelectAll.setImageResource(R.drawable.select_all)
                viewDataBinding.dialogFragContestFilterTvSelectAll.setTextColor(resources.getColor(R.color.selectedTabColor))
            } else{
                viewDataBinding.dialogFragContestFilterIvSelectAll.setImageResource(R.drawable.select_all_passive)
                viewDataBinding.dialogFragContestFilterTvSelectAll.setTextColor(resources.getColor(R.color.click))

            }

        }


        viewDataBinding.dialogFragContestFilterTvCancel.setSafeOnClickListener {
            isDone = false
            //viewModel.getInterest()
            this.dismiss()
        }

        viewDataBinding.dialogFragContestFilterTvDone.setSafeOnClickListener {
            if(!viewModel.combineInterest())
                toast("각 항목을 1개 이상 선택해주세요")
            else {
                isDone = true
                this.dismiss()
            }
        }
    }

    fun refresh() {

        (viewDataBinding.dialogFragContestFilterRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
            replaceAll(viewModel.areaFilter.value!!)
            notifyDataSetChanged()
        }
    }

    private fun setRv() {

        viewDataBinding.dialogFragContestFilterRv.apply {
            adapter =
                object : BaseRecyclerViewAdapter<SubscribeFilter, ItemFilterMatchParentBinding>() {
                    override val layoutResID: Int
                        get() = R.layout.item_filter_match_parent
                    override val bindingVariableId: Int
                        get() = BR.subscribeFilter
                    override val listener: OnItemClickListener?
                        get() = this@SubscribeContestDialogFragment
                }
            layoutManager = NonScrollGridLayoutManager(activity!!, 2)
            addItemDecoration(SpacesItemDecoration(2, 48))
        }

    }

    override fun onItemClicked(item: Any?, position: Int?){
        viewModel.clickInterest((item as SubscribeFilter).idx)
    }

    companion object {
        private val TAG = "SubscribeInternDialogFragment"

    }
}