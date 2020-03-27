package com.icoo.ssgsag_android.ui.main.review.club.registration

import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.databinding.ActivityNotRgstrClubBinding
import com.icoo.ssgsag_android.databinding.ItemClubRgstrCategoryBinding
import com.icoo.ssgsag_android.ui.main.review.club.write.ClubReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ClubReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class NonRgstrClubActivity : BaseActivity<ActivityNotRgstrClubBinding, ClubRgstrViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_not_rgstr_club

    override val viewModel: ClubRgstrViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.actNotRgstrClubTvName.text = intent.getStringExtra("clubName")
        setButton()
        setRv()
        refreshRv()
    }

    private fun setRv(){

        viewDataBinding.actNotRgstrClubRvCategory.run{
            adapter =
                object: BaseRecyclerViewAdapter<Category, ItemClubRgstrCategoryBinding>(){
                    override val layoutResID: Int
                        get() = R.layout.item_club_rgstr_category
                    override val bindingVariableId: Int
                        get() = BR.category
                    override val listener: OnItemClickListener?
                        get() = onItemClickListener
                }
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
        }
    }

    private fun refreshRv() {

        (viewDataBinding.actNotRgstrClubRvCategory.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
            replaceAll(viewModel.defaultClubCategoryList)
            notifyDataSetChanged()
        }
    }

    private val onItemClickListener
            = object: BaseRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClicked(item: Any?, position: Int?) {
            viewModel.selectCategory(position!!)
            refreshRv()
            onDataCheck()
        }
    }

    private fun onDataCheck(){
        if(viewModel.selectedClubCategoryList.size != 0){
            viewDataBinding.actNotRgstrClubClDone.apply{
                backgroundColor = resources.getColor(R.color.ssgsag)
                isClickable = true
            }
        }else{
            viewDataBinding.actNotRgstrClubClDone.apply{
                backgroundColor = resources.getColor(R.color.grey_2)
                isClickable = false
            }
        }
    }

    private fun setButton(){
        viewDataBinding.actNotRgstrClubIvBack.setSafeOnClickListener {
            viewModel.selectedClubCategoryList.clear()
            viewModel.initCategoryList()
            finish()
        }

        viewDataBinding.actNotRgstrClubClDone.setSafeOnClickListener {
            ClubReviewWriteActivity.ClubReviewWriteData.categoryList = viewModel.selectedClubCategoryList

            finish()
            val clubReviewWriteVm : ClubReviewWriteViewModel by viewModel()
            clubReviewWriteVm.setIsNotRgstr(true)
        }
    }
}