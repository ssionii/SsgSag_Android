package com.icoo.ssgsag_android.ui.main.community

import SsgSagNewsViewPagerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.databinding.FragmentCommunityBinding
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardActivity
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailActivity
import com.icoo.ssgsag_android.ui.main.community.feed.CommunityFeedActivity
import com.icoo.ssgsag_android.ui.main.community.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.community.review.CommunityReviewActivity
import com.icoo.ssgsag_android.ui.main.review.club.ReviewDetailActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class CommunityFragment : BaseFragment<FragmentCommunityBinding, CommunityViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_community
    override val viewModel: CommunityViewModel by viewModel()

    val bookmarkRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {
            Log.e("isSave 값", data!!.getIntExtra("isSave", 0).toString())
            Log.e("position 값", data!!.getIntExtra("position", 0).toString())
            (viewDataBinding.itemSsgsagNewsAvp.adapter as SsgSagNewsViewPagerAdapter)
                .refreshItem( data!!.getIntExtra("isSave", 0),  data!!.getIntExtra("position", 0))

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val d = resources.displayMetrics.density

        // 화면 전체 사이즈
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x / d).toInt()

        setSsgsagNewsVp(d, width)
        setRv()
        setButton()


    }
    private fun setSsgsagNewsVp(d : Float, width: Int){

        val leftMargin = (20 * d).toInt()
        val middleMargin = (10 * d).toInt()
        val rightMargin = (30 * d).toInt()
        val contentWidth = ((width - 20 - 10 - 30) * d).toInt()

        viewModel.feedList.observe(viewLifecycleOwner, Observer {
            val cardViewPagerAdapter = SsgSagNewsViewPagerAdapter(requireContext(), it, "main")
            cardViewPagerAdapter.apply {
                newsWidth = contentWidth
                setOnItemClickListener(feedItemClickListener)
            }

            viewDataBinding.itemSsgsagNewsAvp.apply{
                clipToPadding = false
                setPadding(leftMargin, 0, rightMargin, 0)
                pageMargin = middleMargin
                adapter = cardViewPagerAdapter
            }
        })

    }

    val feedItemClickListener = object : SsgSagNewsViewPagerAdapter.OnItemClickListener {

        override fun onItemClick(idx: Int, url: String, name: String, isSave: Int, position : Int) {
            val intent = Intent(requireActivity(), FeedWebActivity::class.java)
            intent.apply{
                putExtra("from","feed")
                putExtra("idx",idx)
                putExtra("url",url)
                putExtra("title",name)
                putExtra("isSave",isSave)
                putExtra("position",position)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

            bookmarkRequest.launch(intent)
        }

        override fun bookmark(feed: Feed, position: Int) {

        }
    }


    private fun setRv(){

        viewModel.collectionItem.observe(viewLifecycleOwner, Observer {

            val collectionAdapter = CommunityCollectionRecyclerView(it)
            collectionAdapter.apply {
                setOnCommunityCollectionClickListener(onCommunityCollectionClickListener)
                setHasStableIds(true)
            }

            viewDataBinding.fragCommunityRv.apply{
                adapter = collectionAdapter
                layoutManager = WrapContentLinearLayoutManager()
            }

        })
    }

    val onCommunityCollectionClickListener = object : CommunityCollectionRecyclerView.OnCommunityCollectionClickListener {

        override fun onMoreClick(communityType: Int) {
            when(communityType){
                CommunityViewType.BOARD_COUNSEL, CommunityViewType.BOARD_TALK -> {
                    goBoard(communityType)
                }
                CommunityViewType.REVIEW -> {
                    goReview()
                }
            }
        }

        override fun onItemClick(communityType: Int, idx: Int) {
            when(communityType){
                CommunityViewType.BOARD_COUNSEL, CommunityViewType.BOARD_TALK -> {
                    val intent = Intent(requireContext(), BoardPostDetailActivity::class.java)

                    intent.putExtra("type", communityType)
                    intent.putExtra("postIdx", idx)

                    startActivity(intent)
                }
                CommunityViewType.REVIEW -> {
                    val intent = Intent(activity!!, ReviewDetailActivity::class.java)
                    intent.putExtra("clubIdx", idx)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setButton(){

        viewDataBinding.fragCommunityLlCounselBoard.setSafeOnClickListener {
            goBoard(CommunityViewType.BOARD_COUNSEL)
        }

        viewDataBinding.fragCommunityLlTalkBoard.setSafeOnClickListener {
            goBoard(CommunityViewType.BOARD_TALK)
        }

        viewDataBinding.fragCommunityLlReview.setSafeOnClickListener {
            goReview()
        }

        viewDataBinding.fragCommunityLlFeed.setSafeOnClickListener {
            goFeed()
        }

        viewDataBinding.fragCommunityLlFeedMore.setSafeOnClickListener {
            goFeed()
        }

    }

    private fun goFeed(){
        val intent = Intent(requireActivity(), CommunityFeedActivity::class.java)
        startActivity(intent)
    }

    private fun goBoard(type : Int){
        val intent = Intent(requireActivity(), CommunityBoardActivity::class.java)
        intent.putExtra("type", type)

        startActivity(intent)

    }

    private fun goReview(){
        val intent = Intent(requireActivity(), CommunityReviewActivity::class.java)
        startActivity(intent)
    }


}