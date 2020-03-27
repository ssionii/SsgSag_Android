package com.icoo.ssgsag_android.ui.main.subscribe

import android.os.Bundle
import android.view.MenuItem
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.subscribe.Subscribe
import com.icoo.ssgsag_android.databinding.ActivitySubscribeBinding
import com.icoo.ssgsag_android.databinding.ItemSubscribeBinding
import com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog.SubscribeContestDialogFragment
import com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog.SubscribeInternDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubscribeActivity : BaseActivity<ActivitySubscribeBinding, SubscribeViewModel>(),
    BaseRecyclerViewAdapter.OnItemClickListener, SubscribeInternDialogFragment.OnDialogDismissedListener, SubscribeContestDialogFragment.OnDialogDismissedListener{

    override val layoutResID: Int
        get() = R.layout.activity_subscribe
    override val viewModel: SubscribeViewModel by viewModel()

    val internDialogFragment = SubscribeInternDialogFragment()
    val contestDialogFragment = SubscribeContestDialogFragment()

    lateinit var subscribeTemp : Subscribe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        //Toolbar
        setSupportActionBar(viewDataBinding.actSubscribeTbToolbar)
        supportActionBar!!.run {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back)
        }

        setRecyclerViewAdapter()
    }

    private fun setRecyclerViewAdapter() {
        //RecyclerView
        viewDataBinding.actSubscribeRvSubscribe.apply {
            adapter =
                object : BaseRecyclerViewAdapter<Subscribe, ItemSubscribeBinding>() {
                    override val layoutResID: Int
                        get() = R.layout.item_subscribe
                    override val bindingVariableId: Int
                        get() = BR.subscribe
                    override val listener: OnItemClickListener?
                        get() = this@SubscribeActivity
                }
        }
    }

    override fun onDialogDismissed(isDone: Boolean) {
        if(isDone) {
            viewModel.subscribe(subscribeTemp.interestIdx, subscribeTemp.userIdx)
        }
    }


    override fun onItemClicked(item: Any?, position: Int?) {

        if((item as Subscribe).interestName =="인턴"
            && (item as Subscribe).userIdx ==0 ) {
            internDialogFragment.setOnDialogDismissedListener(this)
            internDialogFragment.show(supportFragmentManager, "intern filtering dialog")

            subscribeTemp = item
        }/*else if((item as Subscribe).interestName == "공모전"
            &&(item as Subscribe).userIdx == 0){
            contestDialogFragment.setOnDialogDismissedListener(this)
            contestDialogFragment.show(supportFragmentManager, "contest filtering dialog")

            subscribeTemp = item
        }*/

        else
            viewModel.subscribe((item as Subscribe).interestIdx, item.userIdx)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val TAG = "SubscribeActivity"
    }
}