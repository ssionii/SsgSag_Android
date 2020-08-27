package com.icoo.ssgsag_android.ui.main.allPosters.category

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityAllCategoryBinding
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.DialogPlusAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.GridHolder

class AllCategoryActivity : BaseActivity<ActivityAllCategoryBinding, AllCategoryViewModel>(),
    BaseRecyclerViewAdapter.OnItemClickListener {

    override val layoutResID: Int
        get() = R.layout.activity_all_category
    override val viewModel: AllCategoryViewModel by viewModel()

    private var category = 0

    private var curPage = 0
    private var field = 0
    private var isUnion = true
    private var isEnterprise = true

    lateinit var mAdapter: DialogPlusAdapter

    val requestFromDetail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
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
    }

    override fun onResume() {
        super.onResume()

        viewModel.getRefreshedPoster()
    }


    private fun refreshRv(){
        viewModel.sortType.observe(this, Observer { value->

            if(field == 0) {
                (viewDataBinding.actAllCategoryRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                    clearAll()
                    curPage = 0
                    viewModel.getAllPosterCategory(curPage)

                    if(viewModel.posters.value != null)
                        notifyDataSetChanged()
                }
            }else{
                (viewDataBinding.actAllCategoryRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                    clearAll()
                    curPage = 0
                    viewModel.getAllPosterField(field, curPage, category, isEnterprise)

                    if(viewModel.posters.value != null)
                        notifyDataSetChanged()
                }
            }
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
                                if(field == 0) {
                                    viewModel.getAllPosterCategory(curPage)
                                }
                                else {
                                    viewModel.getAllPosterField(field, curPage, category, isEnterprise)
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
        var isChanged = false
        var internHeaderClick = false

        val holder = GridHolder(2)

        when(category){
            0 -> mAdapter = DialogPlusAdapter(this, true, 10, field, category)
            1 -> mAdapter = DialogPlusAdapter(this, true, 7, field, category)
            2,6 -> mAdapter = DialogPlusAdapter(this, true, 12, field, category)
            4 -> {
                if(isEnterprise) mAdapter =  DialogPlusAdapter(this, true, 8, field, category, isEnterprise)
                else mAdapter =  DialogPlusAdapter(this, true, 12, field, category, isEnterprise)

            }
        }

        val builder = DialogPlus.newDialog(this).apply {

            setContentHolder(holder)
            if(category == 2 || category == 6 || category == 4){

                if(category == 2 || category == 6) {
                    if (isUnion)
                        setHeader(R.layout.item_dialog_plus_club_header_union)
                    else
                        setHeader(R.layout.item_dialog_plus_club_header_campus)
                }else{
                    if(!isEnterprise)
                        setHeader(R.layout.item_dialog_plus_club_header_second)
                    else
                        setHeader(R.layout.item_dialog_plus_club_header_first)
                }



                setOnClickListener { dialog, view ->

                    val firstCv = dialog.headerView.findViewById<CardView>(R.id.item_dialog_plus_club_header_cv_first)
                    val firstTv = dialog.headerView.findViewById<TextView>(R.id.item_dialog_plus_club_header_tv_first)
                    val secondCv =  dialog.headerView.findViewById<CardView>(R.id.item_dialog_plus_club_header_cv_second)
                    val secondTv = dialog.headerView.findViewById<TextView>(R.id.item_dialog_plus_club_header_tv_second)

                    if(view.id == R.id.item_dialog_plus_club_header_cancel){
                        dialog.onBackPressed(dialog)
                    }else if(view.id == R.id.item_dialog_plus_club_header_cv_first){

                        // todo: 데이터 바인딩으로 하고 싶어,,
                        firstCv.setCardBackgroundColor(Color.parseColor("#26656ef0"))
                        firstTv.apply {
                            setTextColor(resources.getColor(R.color.ssgsag))
                            typeface =
                                ResourcesCompat.getFont(
                                    SsgSagApplication.getGlobalApplicationContext(),
                                    R.font.noto_sans_kr_medium)

                        }

                        secondCv.setCardBackgroundColor(resources.getColor(R.color.grey_4))
                        secondTv.apply {
                            setTextColor(resources.getColor(R.color.grey_1))
                            typeface =
                                ResourcesCompat.getFont(
                                    SsgSagApplication.getGlobalApplicationContext(),
                                    R.font.noto_sans_kr_regular)

                        }
                        if(category == 2) {
                            category = 6
                            viewModel.category = 6
                            viewModel.setIsUnivClub(true)
                            isUnion = false
                        }else if(category == 4){
                            isEnterprise = true
                            internHeaderClick = true
                            dialog.dismiss()

                        }
                        isChanged = true


                    }else if(view.id == R.id.item_dialog_plus_club_header_cv_second){

                        secondCv.setCardBackgroundColor(Color.parseColor("#26656ef0"))
                        secondTv.apply {
                            setTextColor(resources.getColor(R.color.ssgsag))
                            typeface =
                                ResourcesCompat.getFont(
                                    SsgSagApplication.getGlobalApplicationContext(),
                                    R.font.noto_sans_kr_medium)

                        }

                        firstCv.setCardBackgroundColor(resources.getColor(R.color.grey_4))
                        firstTv.apply {
                            setTextColor(resources.getColor(R.color.grey_1))
                            typeface =
                                ResourcesCompat.getFont(
                                    SsgSagApplication.getGlobalApplicationContext(),
                                    R.font.noto_sans_kr_regular)

                        }


                        if(category == 6) {
                            category = 2
                            viewModel.category = 2
                            viewModel.setIsUnivClub(false)
                            isUnion = true
                        }else if(category == 4){
                            isEnterprise = false
                            internHeaderClick = true
                            dialog.dismiss()
                        }

                        isChanged = true
                    }
                }

            } else{
                setHeader(R.layout.item_dialog_plus_header)

                setOnClickListener { dialog, view ->
                    if(view.id == R.id.item_dialog_plus_header_cancel){
                        dialog.onBackPressed(dialog)
                    }
                }
            }

            setCancelable(true)
            setGravity(Gravity.TOP)

            setOnItemClickListener{ dialog, item, view, position ->
                val textView = view.findViewById<TextView>(R.id.text_view)
                textView.setTextColor(context.resources.getColor(R.color.selectedTabColor))

                field = position
                isChanged = true
                curPage = 0
                dialog.dismiss()
            }

            setOnDismissListener {
                if(isChanged && !internHeaderClick) {
                    (viewDataBinding.actAllCategoryRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
                        clearAll()
                        if(field != 0)
                            viewModel.getAllPosterField(field, curPage, category, isEnterprise)
                        else
                            viewModel.getAllPosterCategory(curPage)
                        notifyItemRangeChanged(0, curPage*100 + viewModel.posters.value!!.size)
                    }
                    isChanged = false
                }

                if(category == 4 && internHeaderClick){
                    if(isEnterprise){
                        this.setAdapter(DialogPlusAdapter(context!!, true, 8, field, category, isEnterprise))
                        field = 0
                        this.setHeader(R.layout.item_dialog_plus_club_header_first)
                    }
                    else {
                        this.setAdapter(DialogPlusAdapter(context!!, true, 12, field, category, isEnterprise))
                        field = 0
                        this.setHeader(R.layout.item_dialog_plus_club_header_second)
                    }
                    this.create().show()

                    internHeaderClick = false
                }

            }

            setAdapter(mAdapter)
            setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
            setOverlayBackgroundResource(R.color.dialog_background)
            setPadding(80, 30, 0, 100)
        }
        builder.create().show()

    }



}
