package com.icoo.ssgsag_android.ui.main.community.board.postDetail.write

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.databinding.ActivityBoardCounselPostWriteBinding
import com.icoo.ssgsag_android.databinding.ItemBoardCounselPostWriteCategoryBinding
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import com.icoo.ssgsag_android.util.view.SpacesItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class  BoardCounselPostWriteActivity: BaseActivity<ActivityBoardCounselPostWriteBinding, BoardPostWriteViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_board_counsel_post_write

    override val viewModel: BoardPostWriteViewModel by viewModel()

    val categoryList = arrayListOf(
        Category(0, false,"취업/진로"),
        Category(0, false,"공모전/대외활동"),
        Category(0, false,"일상/연애"),
        Category(0, false,"학교생활"),
        Category(0, false,"기타")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.actBoardCounselPostWriteToolbar.toolbarCancelTvTitle.text = "고민 상담톡 작성"

        setCategoryRv()
    }

    fun setCategoryRv(){

        val d = resources.displayMetrics.density

        viewDataBinding.actBoardCounselPostWriteRvCategory.apply{
            adapter = object : BaseRecyclerViewAdapter<Category, ItemBoardCounselPostWriteCategoryBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_board_counsel_post_write_category
                override val bindingVariableId: Int
                    get() = BR.category
                override val listener: OnItemClickListener?
                    get() = onCategoryClickListener
            }

            layoutManager = NonScrollGridLayoutManager(this@BoardCounselPostWriteActivity, 3)
            addItemDecoration(SpacesItemDecoration(3, (8 * d).toInt()))
        }

        (viewDataBinding.actBoardCounselPostWriteRvCategory.adapter as BaseRecyclerViewAdapter<Category, *>).run{
            replaceAll(categoryList)
            notifyDataSetChanged()
        }
    }

    val onCategoryClickListener = object : BaseRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClicked(item: Any?, position: Int?) {
            for(category in categoryList){
                category.isChecked = false
            }
            categoryList[position!!].isChecked = true

            (viewDataBinding.actBoardCounselPostWriteRvCategory.adapter as BaseRecyclerViewAdapter<Category, *>).run{
                replaceAll(categoryList)
                notifyDataSetChanged()
            }
        }
    }
}