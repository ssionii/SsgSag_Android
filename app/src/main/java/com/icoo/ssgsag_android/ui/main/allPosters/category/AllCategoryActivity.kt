package com.icoo.ssgsag_android.ui.main.allPosters.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.databinding.ItemAllPostersBinding
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityAllCategoryBinding
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.DialogPlusAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class AllCategoryActivity : BaseActivity<ActivityAllCategoryBinding, AllCategoryViewModel>(),
    BaseRecyclerViewAdapter.OnItemClickListener {

    override val layoutResID: Int
        get() = R.layout.activity_all_category
    override val viewModel: AllCategoryViewModel by viewModel()

    private var category = 0

    private var curPage = 0
    private var interestNum = ""

    val requestFromDetail = prepareCall(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {
            if(data != null) {
                val result = Intent().apply {
                    putExtra("posterIdx", data!!.getIntExtra("posterIdx", 0))
                    putExtra("isSave", data!!.getIntExtra("isSave", 0))
                }
                setResult(Activity.RESULT_OK, result)
            }else {
                setResult(Activity.RESULT_OK)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        category = intent.getIntExtra("category", 0)

        viewDataBinding.actAllCategoryLlTopContainer.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true;
            }
        })


        setRv()
        refreshRv()
        setButton()

        viewModel.clickedFiledPositionLeft = 0
        viewModel.clickedFieldPositionRight = 0
        viewModel.isLeftHeaderClicked = true
    }

    override fun onResume() {
        super.onResume()

        viewModel.getRefreshedPoster()
    }


    private fun refreshRv(){
        (viewDataBinding.actAllCategoryRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.clearAll()

        curPage = 0

        if(interestNum == "") {
            viewModel.getAllPosterCategory(curPage)
        }else{
            Log.e("gkdnl", "하위")
            viewModel.getAllPosterField(curPage, category, interestNum)
        }

    }

    private fun setRv() {

        // categoryList 설정
        viewModel.setCategoryType(category)
        //viewModel.getAllPosterCategory(0)

        viewDataBinding.actAllCategoryRv.apply {
            adapter =
                object : BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>() {
                    override val layoutResID: Int
                        get() = R.layout.item_all_posters
                    override val bindingVariableId: Int
                        get() = BR.posterDetail
                    override val listener: OnItemClickListener?
                        get() = this@AllCategoryActivity

                }
            (this.itemAnimator as SimpleItemAnimator).run {
                changeDuration = 0
                supportsChangeAnimations = false
            }


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {

                            if (itemCount > 0 && (10 * (curPage+1) - 2 < position)) {
                                curPage = (position + 1) / 10
                                if(interestNum == "") {
                                    viewModel.getAllPosterCategory(curPage)
                                }
                                else {
                                    viewModel.getAllPosterField(curPage, category, interestNum)
                                }
                            }
                            if(viewModel.posters.value != null)
                                notifyItemRangeChanged(0, curPage*100 + viewModel.posters.value!!.size)
                        }

                    }
                }

            })

            layoutManager = WrapContentLinearLayoutManager()
        }

        viewModel.posters.observe(this, Observer {
            (viewDataBinding.actAllCategoryRv.adapter as BaseRecyclerViewAdapter<PosterDetail, *>).apply {
                Log.e("posters!!!", it.toString())
                replaceAll(it)
                notifyDataSetChanged()
            }
        })

        viewModel.sortType.observe(this, Observer {
            refreshRv()
        })

        viewModel.refreshedPoster.observe(this, Observer { value ->
            (viewDataBinding.actAllCategoryRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                if(this.itemCount > viewModel.refreshedPosterPosition) {
                    refreshItem(value, viewModel.refreshedPosterPosition)
                    notifyItemChanged(viewModel.refreshedPosterPosition)
                }
            }
        })

        viewModel.empty.observe(this, Observer { value ->
            if(curPage == 0 && value){
                viewDataBinding.actAllCategoryRlEmpty.visibility = View.VISIBLE
                viewDataBinding.actAllCategoryRv.visibility = View.GONE
            }else{
                viewDataBinding.actAllCategoryRlEmpty.visibility = View.GONE
                viewDataBinding.actAllCategoryRv.visibility = View.VISIBLE
            }
        })

    }

    override fun onItemClicked(item: Any?, position: Int?) {
        var posterIdx = (item as PosterDetail).posterIdx

        val intent = Intent(this, CalendarDetailActivity::class.java)
        intent.apply{
            putExtra("Idx", posterIdx)
            putExtra("from","main")
            putExtra("fromDetail", "all")
        }
        requestFromDetail.launch(intent)
        viewModel.navigate(posterIdx, position!!)
    }

    private fun setButton(){
        viewDataBinding.actAllCategoryIvBack.setOnClickListener {
            finish()
        }

        viewDataBinding.actAllCategoryCvSort.setSafeOnClickListener {

            val wrapper = ContextThemeWrapper(this, R.style.popupMenuStyle)
            val popup = PopupMenu(wrapper, it)
            val inflater = popup.menuInflater
            val menu = popup.menu

            inflater.inflate(R.menu.menu_all_posters_sort, menu)

            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.menu_all_posters_sort_new -> {
                            viewModel.setSortType(2)
                        }
                        R.id.menu_all_posters_sort_deadline -> {
                            viewModel.setSortType(1)
                        }
                        R.id.menu_all_posters_sort_hot -> {
                            viewModel.setSortType(0)
                        }

                    }
                    return false
                }
            })

            popup.show()
        }

        viewDataBinding.actAllCategoryCvField.setSafeOnClickListener {
            showDialog()
        }
    }


    private fun showDialog(){

        val dialogFragment = AllCategoryFieldDialogFragment()
        val args = Bundle()
        args.putInt("category", category)

        dialogFragment.arguments = args
        dialogFragment.setOnDialogDismissedListener(dialogDismissedListener)
        dialogFragment.show(supportFragmentManager, "frag_dialog_all_category_field")

    }
    val dialogDismissedListener
            = object : AllCategoryFieldDialogFragment.OnDialogDismissedListener{
        override fun onDialogDismissed(interest: String) {
            interestNum = interest
            refreshRv()
        }

    }

}
