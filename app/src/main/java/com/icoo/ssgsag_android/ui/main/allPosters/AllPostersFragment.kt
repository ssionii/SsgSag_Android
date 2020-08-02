package com.icoo.ssgsag_android.ui.main.allPosters

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.PosterCategory
import com.icoo.ssgsag_android.databinding.FragmentAllPosterBinding
import com.icoo.ssgsag_android.databinding.ItemAllPosterCategoryBinding
import com.icoo.ssgsag_android.ui.main.allPosters.category.AllCategoryActivity
import com.icoo.ssgsag_android.ui.main.allPosters.collection.AllPosterCollectionRecyclerViewAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllPostersFragment : BaseFragment<FragmentAllPosterBinding, AllPostersViewModel>(),
    CardViewPagerAdapter.OnItemClickListener, BaseRecyclerViewAdapter.OnItemClickListener{

    override val layoutResID: Int
        get() = R.layout.fragment_all_poster
    override val viewModel: AllPostersViewModel by viewModel()

    lateinit private var allPosterCollectionRvAdapter : AllPosterCollectionRecyclerViewAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setTopButtonRv()
        setCollectionRv()
//        setViewPager()
        navigator()
    }

    private fun setTopButtonRv(){

        val categoryList = listOf(
            PosterCategory(1, "대외활동", resources.getColor(R.color.categoryActBg), resources.getColor(R.color.categoryActText), resources.getDrawable(R.drawable.ic_category_activity)),
            PosterCategory(0, "공모전", resources.getColor(R.color.categoryContestBg), resources.getColor(R.color.categoryContestText), resources.getDrawable(R.drawable.ic_category_contest)),
            PosterCategory(4, "인턴", resources.getColor(R.color.categoryInternBg), resources.getColor(R.color.categoryInternText), resources.getDrawable(R.drawable.ic_category_intern)),
            PosterCategory(2, "동아리", resources.getColor(R.color.categoryClubBg), resources.getColor(R.color.categoryClubText), resources.getDrawable(R.drawable.ic_category_club)),
            PosterCategory(7, "교육/강연", resources.getColor(R.color.categoryEduBg), resources.getColor(R.color.categoryEduText), resources.getDrawable(R.drawable.ic_category_edu)),
            PosterCategory(5, "기타", resources.getColor(R.color.categoryEtcBg), resources.getColor(R.color.categoryEtcText), resources.getDrawable(R.drawable.ic_category_etc))
        )

        viewDataBinding.fragAllPosterTopRv.run{
            adapter = object : BaseRecyclerViewAdapter<PosterCategory, ItemAllPosterCategoryBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_all_poster_category
                override val bindingVariableId: Int
                    get() = BR.posterCategory
                override val listener: OnItemClickListener?
                    get() = this@AllPostersFragment
            }

            layoutManager = NonScrollGridLayoutManager(activity!!, 3)


            (adapter as BaseRecyclerViewAdapter<PosterCategory, *>).run{
                replaceAll(categoryList)
                notifyDataSetChanged()
            }
        }
    }

    override fun onItemClicked(item: Any?, position: Int?) {
        val intent = Intent(activity!!, AllCategoryActivity::class.java)
        intent.putExtra("category", (item as PosterCategory).categoryIdx)

        startActivity(intent)
    }



    private fun setCollectionRv(){

        val d = resources.displayMetrics.density

        // 화면 전체 사이즈
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x / d).toInt()

        val contentDpValue = (width - 44 - 10) / 2
        val leftDpValue = 22
        val middleDpValue = 10
        val rightDpValue = width - leftDpValue - contentDpValue

        allPosterCollectionRvAdapter = AllPosterCollectionRecyclerViewAdapter()
        allPosterCollectionRvAdapter.setMargin(d, leftDpValue, middleDpValue, rightDpValue, contentDpValue)
        viewDataBinding.fragAllPosterPostersCollectionRv.run {
            adapter = allPosterCollectionRvAdapter

            layoutManager = WrapContentLinearLayoutManager()
        }

        viewModel.posterList.observe(this, Observer {
            allPosterCollectionRvAdapter.run{
                replaceAll(it)
                notifyDataSetChanged()
            }
        })



    }

    override fun onItemClick(posterIdx: Int) {
        viewModel.navigate(posterIdx)
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(activity, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            view!!.context.startActivity(intent)

        })
    }

    companion object {
        fun newInstance(): AllPostersFragment {
            val fragment = AllPostersFragment()
            return fragment
        }
    }
}