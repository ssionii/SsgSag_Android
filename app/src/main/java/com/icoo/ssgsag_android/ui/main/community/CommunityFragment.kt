package com.icoo.ssgsag_android.ui.main.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentCommunityBinding
import com.icoo.ssgsag_android.databinding.FragmentFeedPageBinding
import com.icoo.ssgsag_android.ui.main.feed.FeedViewModel
import com.icoo.ssgsag_android.ui.main.feed.adapter.FeedAnchorRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.feed.adapter.FeedCareerViewPagerAdapter
import com.icoo.ssgsag_android.ui.main.feed.category.FeedCategoryRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class CommunityFragment : BaseFragment<FragmentCommunityBinding, CommunityViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_community
    override val viewModel: CommunityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}