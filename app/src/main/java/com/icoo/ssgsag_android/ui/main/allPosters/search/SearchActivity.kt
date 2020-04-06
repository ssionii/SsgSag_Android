package com.icoo.ssgsag_android.ui.main.allPosters.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.databinding.ActivitySearchBinding
import com.icoo.ssgsag_android.databinding.ItemAllPostersBinding
import com.icoo.ssgsag_android.ui.main.review.ReviewListRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubManagerCheckActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import com.igaworks.v2.core.AdBrixRm
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>(),
    BaseRecyclerViewAdapter.OnItemClickListener {

    override val layoutResID: Int
        get() = R.layout.activity_search
    override val viewModel: SearchViewModel by viewModel()

    private var reviewListRecyclerViewAdapter : ReviewListRecyclerViewAdapter? = null

    private var curPage = 0
    private var from = "main"
    private var clubType = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        from = intent.getStringExtra("from")
        clubType = intent.getIntExtra("clubType", -1)

        setButton()
        if(from == "main"){
            setPosterRv()
            setPosterRefresh()
        }else if(from =="club"){
            setClubRv()
        }
        setEtHintText()
        navigator()

    }

    override fun onResume() {
        super.onResume()
        if(from =="main")
            viewModel.getRefreshedPoster()
    }

    private fun setButton(){
        viewDataBinding.actSearchIvSearch.setSafeOnClickListener {
            hideKeyboard(viewDataBinding.actSearchEtSearch)
            curPage = 0
            viewDataBinding.actSearchRv.apply{
                if(from == "main") {
                    (adapter as BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>).clearAll()
                    viewModel.getSearchedPosters(viewDataBinding.actSearchEtSearch.text.toString(), curPage)
                }else if(from == "club"){
                    reviewListRecyclerViewAdapter?.clearAll()
                    viewModel.getSearchedClubs(viewDataBinding.actSearchEtSearch.text.toString(), clubType, curPage)
                }
            }



        }

        viewDataBinding.actSearchIvBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actSearchEtSearch.setOnEditorActionListener { v, actionId, event ->
            hideKeyboard(viewDataBinding.actSearchEtSearch)
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                curPage = 0
                viewDataBinding.actSearchRv.apply{
                    if(from == "main") {
                        (adapter as BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>).clearAll()
                        viewModel.getSearchedPosters(viewDataBinding.actSearchEtSearch.text.toString(), curPage)
                    }else if(from == "club"){
                        reviewListRecyclerViewAdapter?.clearAll()
                        viewModel.getSearchedClubs(viewDataBinding.actSearchEtSearch.text.toString(), clubType, curPage)
                    }
                }

                true
            }else
                false
        }

        viewDataBinding.actSearchLlRgstrClub.setSafeOnClickListener {
            finish()
            startActivity<ClubManagerCheckActivity>()
        }
    }

    private fun setPosterRv() {
        //RecyclerView
        viewDataBinding.actSearchRv.apply {
            adapter =
                object : BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>() {
                    override val layoutResID: Int
                        get() = R.layout.item_all_posters
                    override val bindingVariableId: Int
                        get() = BR.posterDetail
                    override val listener: OnItemClickListener?
                        get() = this@SearchActivity
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
                                viewModel.getSearchedPosters(viewDataBinding.actSearchEtSearch.text.toString(), curPage)
                            }
                            notifyDataSetChanged()
                        }

                    }
                }

            })

            layoutManager = WrapContentLinearLayoutManager()
        }
    }

    private fun setPosterRefresh(){
        viewModel.refreshedPoster.observe(this, Observer { value ->
            (viewDataBinding.actSearchRv.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run{
                refreshItem(value, viewModel.refreshedPosterPosition)
                notifyItemChanged(viewModel.refreshedPosterPosition)
            }

        })

        viewModel.posterResult.observe(this, Observer { value ->
            (viewDataBinding.actSearchRv.adapter as BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>).apply{
                addItem(value)
                notifyDataSetChanged()
            }
        })
    }



    private fun setClubRv() {
        //RecyclerView
        viewModel.clubResult.observe(this, Observer { value ->
            if (reviewListRecyclerViewAdapter != null) {
                reviewListRecyclerViewAdapter!!.apply {
                    addItem(value)
                    notifyDataSetChanged()

                }
            } else {
                reviewListRecyclerViewAdapter =
                    ReviewListRecyclerViewAdapter(value)
                reviewListRecyclerViewAdapter!!.run {
                    setOnReviewClickListener(onReviewClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.actSearchRv.apply {
                    adapter = reviewListRecyclerViewAdapter

                    (itemAnimator as SimpleItemAnimator).run {
                        changeDuration = 0
                        supportsChangeAnimations = false
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }
            }

            if(value.size == 0)
                viewDataBinding.actSearchLlRgstrClub.visibility = VISIBLE
            else
                viewDataBinding.actSearchLlRgstrClub.visibility = GONE

        })

        viewDataBinding.actSearchRv.apply{

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {

                            if (itemCount > 0 && (10 * (curPage+1) - 2 < position)) {
                                curPage = (position + 1) / 10
                                viewModel.getSearchedClubs(viewDataBinding.actSearchEtSearch.text.toString(), clubType, curPage)
                            }
                            notifyDataSetChanged()
                        }

                    }
                }

            })

        }
    }


    override fun onItemClicked(item: Any?, position: Int?) {
        viewModel.navigate((item as PosterDetail).posterIdx, position!!, from)
        //adbrix
        AdBrixRm.event(
            "touchUp_PosterDetail",
            AdBrixRm.AttrModel().setAttrs("posterIdx", (item as PosterDetail).posterIdx.toLong())
        )
    }

    val onReviewClickListener = object : ReviewListRecyclerViewAdapter.OnReviewClickListener{
        override fun onItemClickListener(clubIdx: Int) {
            viewModel.navigate(clubIdx, 0, from)
        }
    }

    private fun setEtHintText(){
        when(from){
            "main" ->  viewDataBinding.actSearchEtSearch.hint = "포스터 이름으로 검색"
            "club" -> viewDataBinding.actSearchEtSearch.hint = "2자 이상의 동아리 이름으로 검색"
        }

    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(this@SearchActivity, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            startActivity(intent)
        })
    }

    fun hideKeyboard(et: EditText){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(et.getWindowToken(), 0)

    }


    companion object {
        private val TAG = "SearchActivity"
    }
}