package com.icoo.ssgsag_android.ui.signUp.searchUniv

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.signUp.UnivInfo
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

    val univNameFromRegister = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {
            val name = data!!.getStringExtra("univName")

            val result = Intent().apply{
                putExtra("from", "register")
                putExtras(
                    bundleOf("univName" to name)
                )
            }

            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        setButton()
        setUnivRv()
        setUnivSearch()
    }

    private fun setUnivRv(){

        univContainerRecyclerViewAdapter = UnivContainerRecyclerViewAdapter(this)

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

    private fun setUnivSearch(){
       viewDataBinding.actSearchUnivEtName.addTextChangedListener(object : TextWatcher{
           override fun afterTextChanged(s: Editable?) {
               if(s.toString().isNotEmpty()){
                   viewDataBinding.actSearchUnivCvRegUniv.visibility = VISIBLE
                   viewDataBinding.actSearchUnivTvEmptyName.visibility = GONE
               }else{
                   viewDataBinding.actSearchUnivCvRegUniv.visibility = GONE
                   viewDataBinding.actSearchUnivTvEmptyName.visibility = VISIBLE
               }
           }
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               univContainerRecyclerViewAdapter.filter.filter(s)
           }
       })
    }

    fun univNameClicked(univInfo : UnivInfo){
        val result = Intent().apply{
            putExtra("from", "search")
            putExtra("univName", univInfo.univName)
            putExtra("majorList", univInfo.majorList)
        }
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    private fun setButton(){
        viewDataBinding.actSearchUnivClCancel.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actSearchUnivCvRegUniv.setSafeOnClickListener {
            val intent = Intent(this, RegisterUnivActivity::class.java)
            univNameFromRegister.launch(intent)
        }
    }




}