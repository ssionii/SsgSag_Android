package com.icoo.ssgsag_android.ui.signUp.searchUniv

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivitySearchUnivBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollLinearLayoutManager
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchUnivActivity : BaseActivity<ActivitySearchUnivBinding, SearchUnivViewModel>() {


    override val layoutResID: Int
        get() = R.layout.activity_search_univ

    override val viewModel: SearchUnivViewModel by viewModel()

    lateinit var univContainerRecyclerViewAdapter : UnivContainerRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setUnivRv()
    }

    private fun setUnivRv(){

        univContainerRecyclerViewAdapter = UnivContainerRecyclerViewAdapter()

        viewDataBinding.actSearchUnivRv.run{
            adapter = univContainerRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.univList.observe(this, Observer {
            univContainerRecyclerViewAdapter.run{
                replaceAll(it)
                notifyDataSetChanged()
            }
        })
    }

    private fun setButton(){
        viewDataBinding.actSearchUnivClCancel.setSafeOnClickListener {
            finish()
        }
    }



}