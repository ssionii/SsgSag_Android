package com.icoo.ssgsag_android.ui.main.allPosters

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.PosterCategory
import com.icoo.ssgsag_android.databinding.FragmentAllPosterBinding
import com.icoo.ssgsag_android.databinding.ItemAllPosterCategoryBinding
import com.icoo.ssgsag_android.ui.main.allPosters.category.AllCategoryActivity
import com.icoo.ssgsag_android.ui.main.allPosters.collection.AllPosterCollectionRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.allPosters.collection.AllPosterEventCardViewPagerAdapter
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.review.main.AutoScrollAdapter
import com.icoo.ssgsag_android.util.view.CircleAnimIndicator
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllPostersFragment : BaseFragment<FragmentAllPosterBinding, AllPostersViewModel>(),
    BaseRecyclerViewAdapter.OnItemClickListener{

    override val layoutResID: Int
        get() = R.layout.fragment_all_poster
    override val viewModel: AllPostersViewModel by viewModel()

    private lateinit var allPosterCollectionRvAdapter : AllPosterCollectionRecyclerViewAdapter
    private lateinit var allPosterEventCardViewPagerAdapter : AllPosterEventCardViewPagerAdapter

    var requestRowIdx = 0
    var requestPosition = 0

    val requestFromDetail = prepareCall(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {
            allPosterCollectionRvAdapter.itemList[requestRowIdx].adViewItemList[requestPosition].isSave =
                data!!.getIntExtra("isSave", 0)

            allPosterCollectionRvAdapter.apply {
                if(requestRowIdx >= 2) requestRowIdx++
                notifyItemChanged(requestRowIdx)
            }
        }
    }

    val requestFromMore = prepareCall(ActivityResultContracts.StartActivityForResult()) {activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {

            val posterIdx = data!!.getIntExtra("posterIdx", 0)

            for(item in allPosterCollectionRvAdapter.itemList[requestRowIdx].adViewItemList){
                if(item.contentIdx == posterIdx){
                    item.isSave = data.getIntExtra("isSave", 0)
                }
            }
            allPosterCollectionRvAdapter.apply{
                if(requestRowIdx >= 2) requestRowIdx++
                notifyItemChanged(requestRowIdx)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        val d = resources.displayMetrics.density

        // 화면 전체 사이즈
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x / d).toInt()

        setTopBanner(d, width)
        setTopButtonRv()
        setCollectionRv()
        setEventVp(d, width)
        navigator()
    }

    private fun setTopBanner(d : Float, width: Int){

        val vpAdapter = AutoScrollAdapter(requireActivity())
        vpAdapter.setOnItemClickListener(onBannerItemClickListener)

        viewDataBinding.fragAllPosterAsvpBanner.run{
            adapter = vpAdapter
            setInterval(3000)
            setCycle(true)

            layoutParams.height = ( width * 0.5 * d ).toInt()

            addOnPageChangeListener(onBannerPageChangeListener)
        }

        viewModel.mainAdList.observe(viewLifecycleOwner, Observer {
            if(it.size > 0){
                vpAdapter.apply{
                    replaceAll(it[0].adViewItemList)
                    notifyDataSetChanged()
                }
                viewDataBinding.fragAllPosterCai.createDotPanel(it[0].adViewItemList.size, R.drawable.dot_2, R.drawable.dot_1)

                viewDataBinding.fragAllPosterAsvpBanner.startAutoScroll()
            }else{
                viewDataBinding.fragAllPosterAsvpBanner.visibility = GONE
            }

        })
    }

    val onBannerItemClickListener
            = object : AutoScrollAdapter.OnItemClickListener{
        override fun onItemClick(url: String?) {

            val intent = Intent(context, FeedWebActivity::class.java)
            intent.putExtra("url",url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val bundle = Bundle().apply {
                putString("url", url)
            }

            ContextCompat.startActivity(activity!!, intent, bundle)
        }
    }

    val onBannerPageChangeListener
            = object : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) { }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            viewDataBinding.fragAllPosterCai.selectDot(position)
        }
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

            layoutManager = NonScrollGridLayoutManager(requireActivity(), 3)


            (adapter as BaseRecyclerViewAdapter<PosterCategory, *>).run{
                replaceAll(categoryList)
                notifyDataSetChanged()
            }
        }
    }

    override fun onItemClicked(item: Any?, position: Int?) {
        val intent = Intent(requireActivity(), AllCategoryActivity::class.java)
        intent.putExtra("category", (item as PosterCategory).categoryIdx)

        startActivity(intent)
    }

    private fun setCollectionRv(){

        val d = resources.displayMetrics.density

        // 화면 전체 사이즈
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x / d).toInt()

        val contentDpValue = (width - 44 - 10) / 2
        val leftDpValue = 22
        val middleDpValue = 10
        val rightDpValue = width - leftDpValue - contentDpValue

        allPosterCollectionRvAdapter = AllPosterCollectionRecyclerViewAdapter(this.lifecycle)
        allPosterCollectionRvAdapter.apply {
            setOnAllPosterCollectionClickListener(onPosterCollectionClickListener)
            setMargin(d, leftDpValue, middleDpValue, rightDpValue, contentDpValue)
        }
        viewDataBinding.fragAllPosterPostersCollectionRv.run {
            adapter = allPosterCollectionRvAdapter

            layoutManager = WrapContentLinearLayoutManager()

            (this.itemAnimator as SimpleItemAnimator).run {
                changeDuration = 0
                supportsChangeAnimations = false
            }
        }

        viewModel.posterList.observe(viewLifecycleOwner, Observer {
            allPosterCollectionRvAdapter.run{
                replaceAll(it)
                notifyDataSetChanged()
            }
        })
    }

    private val onPosterCollectionClickListener
            = object : AllPosterCollectionRecyclerViewAdapter.OnAllPosterCollectionClickListener{
        override fun onPosterItemClick(posterIdx: Int, rowIdx : Int, position: Int) {

            requestRowIdx = rowIdx
            requestPosition = position

            val intent = Intent(requireActivity(), CalendarDetailActivity::class.java)
            intent.putExtra("Idx", posterIdx)
            intent.putExtra("from", "main")
            intent.putExtra("from", "what")

            requestFromDetail.launch(intent)
        }

        override fun onMoreClick(categoryIdx: Int, rowIdx : Int) {

            requestRowIdx = rowIdx

            val intent = Intent(activity!!, AllCategoryActivity::class.java)
            intent.putExtra("category", categoryIdx)

            requestFromMore.launch(intent)
        }

        override fun onManagePosterItem(posterIdx: Int, isSave : Int) {
            viewModel.managePoster(posterIdx, isSave)
        }
    }

    private fun setEventVp(d : Float, width: Int){

        val leftMargin = (19 * d).toInt()
        val middleMargin = (7 * d).toInt()
        val rightMargin = (65 * d).toInt()
        val contentWidth = ((width - 19 - 7 - 65) * d).toInt()

        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            if(it.size > 0){
                allPosterEventCardViewPagerAdapter =
                    AllPosterEventCardViewPagerAdapter(
                        requireActivity(),
                        it[0].adViewItemList,
                        d
                    )
                allPosterEventCardViewPagerAdapter.apply{
                    eventWidth = contentWidth
                    setOnItemClickListener(eventItemClickListener)
                }

                viewDataBinding.fragAllPosterEventVp.run{
                    clipToPadding = false
                    setPadding(leftMargin, 0, rightMargin, 0)
                    pageMargin = middleMargin
                    adapter = allPosterEventCardViewPagerAdapter
                }
            }
        })

    }

    val eventItemClickListener
            = object : AllPosterEventCardViewPagerAdapter.OnItemClickListener{
            override fun onEventClick(adUrl: String?, title: String) {
                if(adUrl != null ){
                    val intent = Intent(activity!!, FeedWebActivity::class.java)
                    intent.putExtra("url", adUrl)
                    intent.putExtra("title", title)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                }
            }
    }

    private fun navigator() {
        viewModel.activityToStart.observe(viewLifecycleOwner, Observer { value ->
            val intent = Intent(activity, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            requireView().context.startActivity(intent)

        })
    }

    companion object {
        fun newInstance(): AllPostersFragment {
            val fragment = AllPostersFragment()
            return fragment
        }
    }
}