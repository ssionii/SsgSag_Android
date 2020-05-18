package com.icoo.ssgsag_android.ui.main.review.club.registration

import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.review.ReviewWriteRelam
import com.icoo.ssgsag_android.databinding.ActivityNotRgstrClubBinding
import com.icoo.ssgsag_android.databinding.ItemClubRgstrCategoryBinding
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.realm.Realm
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class NonRgstrClubActivity : BaseActivity<ActivityNotRgstrClubBinding, ClubRgstrViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_not_rgstr_club

    override val viewModel: ClubRgstrViewModel by viewModel()

    val realm = Realm.getDefaultInstance()

    private var isDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewDataBinding.actNotRgstrClubTvName.text = intent.getStringExtra("clubName")
        setButton()
        setRv()
        refreshRv()
        onDataCheck()
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
//                isClickable = true

                isDone = true
            }
        }else{
            viewDataBinding.actNotRgstrClubClDone.apply{
                backgroundColor = resources.getColor(R.color.grey_2)
//                isClickable = false
                isDone = false
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

            if(isDone) {
                viewModel.selectedClubCategoryList.run{
                    var temp = ""
                    for(i in 0 until this.size - 1){
                        temp += this[i] + ","
                    }
                    temp+= this[this.size-1]

                    realm.beginTransaction()
                    val reviewWriteRealm = realm.where(ReviewWriteRelam::class.java).equalTo("id", 1 as Int).findFirst()!!
                    reviewWriteRealm.categoryList = temp
                    realm.commitTransaction()
                }

                viewModel.selectedClubCategoryList.clear()

                finish()
                val reviewWriteVm: ReviewWriteViewModel by viewModel()
                reviewWriteVm.setIsNotRgstr(true)
            }else{


                toast("카테고리를 1개 이상 선택해주세요.")
            }
        }
    }
}