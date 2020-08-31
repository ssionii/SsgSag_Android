package com.icoo.ssgsag_android.ui.main.community

import SsgSagNewsViewPagerAdapter
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentCommunityBinding
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardActivity
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardType
import com.icoo.ssgsag_android.ui.main.community.review.CommunityReviewActivity
import com.icoo.ssgsag_android.ui.main.feed.FeedViewModel
import com.icoo.ssgsag_android.ui.main.feed.adapter.FeedAnchorRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.feed.adapter.FeedCareerViewPagerAdapter
import com.icoo.ssgsag_android.ui.main.feed.category.FeedCategoryRecyclerViewAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class CommunityFragment : BaseFragment<FragmentCommunityBinding, CommunityViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_community
    override val viewModel: CommunityViewModel by viewModel()

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

        viewModel.ssgSagNewsList.observe(viewLifecycleOwner, Observer {
            val cardViewPagerAdapter = SsgSagNewsViewPagerAdapter(requireContext(), it)
            cardViewPagerAdapter.newsWidth = contentWidth

            viewDataBinding.itemSsgsagNewsAvp.apply{
                clipToPadding = false
                setPadding(leftMargin, 0, rightMargin, 0)
                pageMargin = middleMargin
                adapter = cardViewPagerAdapter
            }
        })

    }

    private fun setRv(){

        viewModel.collectionItem.observe(viewLifecycleOwner, Observer {
            viewDataBinding.fragCommunityRv.apply{
                adapter = CommunityCollectionRecyclerView(it)
                layoutManager = WrapContentLinearLayoutManager()
            }
        })

    }

    private fun setButton(){

        viewDataBinding.fragCommunityLlCounselBoard.setSafeOnClickListener {
            val intent = Intent(requireActivity(), CommunityBoardActivity::class.java)
            intent.putExtra("type", CommunityBoardType.COUNSEL)

            startActivity(intent)

        }

        viewDataBinding.fragCommunityLlTalkBoard.setSafeOnClickListener {
            val intent = Intent(requireActivity(), CommunityBoardActivity::class.java)
            intent.putExtra("type", CommunityBoardType.TALK)

            startActivity(intent)

        }

        viewDataBinding.fragCommunityLlReview.setSafeOnClickListener {
            val intent = Intent(requireActivity(), CommunityReviewActivity::class.java)
            startActivity(intent)
        }

    }

}