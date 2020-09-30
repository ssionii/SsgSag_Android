package com.icoo.ssgsag_android.ui.main.myPage.myBoard

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.user.myBoard.MyComment
import com.icoo.ssgsag_android.data.model.user.myBoard.MyPost
import com.icoo.ssgsag_android.databinding.*
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardType
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailActivity
import com.icoo.ssgsag_android.ui.main.community.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.myPage.MyPageViewModel
import com.icoo.ssgsag_android.ui.main.review.club.ReviewDetailActivity
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyBoardPageFragment : BaseFragment<FragmentMyBoardPageBinding, MyPageViewModel>(){

    override val layoutResID: Int
        get() = R.layout.fragment_my_board_page

    override val viewModel: MyPageViewModel by viewModel()

    // myPost, myComment, bookmarkPost, bookmarkNews
    var myBoardType = 0

    var curPage = 0
    var pageSize = 10

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel

        myBoardType = requireArguments().getInt("myBoardType")

        setRv()
        refreshRv()
        setPullToRefresh()

    }

    private fun setRv(){
        when(myBoardType){

            MyBoardType.MY_POST -> {

                viewModel.getMyPost(curPage, pageSize)

                viewDataBinding.actMyBoardPageRv.run{
                    adapter = object :BaseRecyclerViewAdapter<MyPost, ItemMyPostBinding>(){
                        override val layoutResID: Int
                            get() = R.layout.item_my_post
                        override val bindingVariableId: Int
                            get() = BR.myPost
                        override val listener: OnItemClickListener?
                            get() = object : OnItemClickListener{
                                override fun onItemClicked(item: Any?, position: Int?) {

                                    val post = item as MyPost

                                    when(post.category1){

                                        "COMMUNITY" -> {
                                            val intent = Intent(requireActivity(), BoardPostDetailActivity::class.java)
                                            when(post.category2){
                                                "FREE" -> intent.putExtra("type", CommunityBoardType.TALK)
                                                else -> intent.putExtra("type", CommunityBoardType.COUNSEL)
                                            }
                                            intent.putExtra("postIdx", post.contentIdx)
                                            startActivity(intent)
                                        }

                                        "CLUB_POST"-> {
                                            val intent = Intent(requireActivity(), ReviewDetailActivity::class.java)
                                            intent.putExtra("clubIdx", post.contentIdx)
                                            startActivity(intent)
                                        }
                                    }

                                }
                            }
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }

                viewDataBinding.actMyBoardPageTvEmpty.text = "표시할 정보가 없습니다."


            }

            MyBoardType.MY_COMMENT -> {

                viewModel.getMyComment(curPage, pageSize)

                viewDataBinding.actMyBoardPageRv.run{
                    adapter = object :BaseRecyclerViewAdapter<MyComment, ItemMyCommentBinding>(){
                        override val layoutResID: Int
                            get() = R.layout.item_my_comment
                        override val bindingVariableId: Int
                            get() = BR.myComment
                        override val listener: OnItemClickListener?
                            get() = object : OnItemClickListener{
                                override fun onItemClicked(item: Any?, position: Int?) {

                                    val comment = item as MyComment

                                    val intent = Intent(requireActivity(), BoardPostDetailActivity::class.java)
                                    when(comment.community.category){
                                        "FREE" -> intent.putExtra("type", CommunityBoardType.TALK)
                                        else -> intent.putExtra("type", CommunityBoardType.COUNSEL)
                                    }
                                    intent.putExtra("postIdx", comment.community.communityIdx)

                                    startActivity(intent)

                                }
                            }
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }

                viewDataBinding.actMyBoardPageTvEmpty.text = "표시할 정보가 없습니다."


            }

            MyBoardType.BOOKMARK_POST -> {
                viewModel.getBookmarkedPost(curPage, pageSize)

                viewDataBinding.actMyBoardPageRv.run{
                    adapter = object :BaseRecyclerViewAdapter<PostInfo, ItemBoardBinding>(){
                        override val layoutResID: Int
                            get() = R.layout.item_board
                        override val bindingVariableId: Int
                            get() = BR.postInfo
                        override val listener: OnItemClickListener?
                            get() = object : OnItemClickListener{
                                override fun onItemClicked(item: Any?, position: Int?) {

                                    val post = item as PostInfo

                                    val intent = Intent(requireActivity(), BoardPostDetailActivity::class.java)
                                    when(post.category){
                                        "FREE" -> intent.putExtra("type", CommunityBoardType.TALK)
                                        else -> intent.putExtra("type", CommunityBoardType.COUNSEL)
                                    }
                                    intent.putExtra("postIdx", post.communityIdx)

                                    startActivity(intent)

                                }
                            }
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }

                viewDataBinding.actMyBoardPageTvEmpty.text = "스크랩된 정보가 없습니다."

            }

            MyBoardType.BOOKMARK_NEWS -> {
                viewModel.getBookmarkedFeed(curPage)

                viewDataBinding.actMyBoardPageRv.run{
                    adapter = object :BaseRecyclerViewAdapter<Feed, ItemFeedBinding>(){
                        override val layoutResID: Int
                            get() = R.layout.item_feed
                        override val bindingVariableId: Int
                            get() = BR.feed
                        override val listener: OnItemClickListener?
                            get() = object : OnItemClickListener{
                                override fun onItemClicked(item: Any?, position: Int?) {

                                    val feed = item as Feed

                                    val intent = Intent(requireActivity(), FeedWebActivity::class.java)
                                    feed.let{
                                        intent.apply{
                                            putExtra("from","feed")
                                            putExtra("idx",it.feedIdx)
                                            putExtra("url", it.feedUrl)
                                            putExtra("title",it.feedName)
                                            putExtra("isSave",it.isSave)
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                        }
                                    }

                                    startActivity(intent)
                                }
                            }
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }

                viewDataBinding.actMyBoardPageTvEmpty.text = "스크랩된 정보가 없습니다."

            }


        }

        viewDataBinding.actMyBoardPageRv.run{
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {

                        var position = (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {

                            if (itemCount > 0 && (10 * (curPage+1) - 2 < position)) {
                                curPage = (position + 1) / 10

                                when(myBoardType){
                                    MyBoardType.MY_COMMENT -> viewModel.getMyComment(curPage, pageSize)
                                    MyBoardType.BOOKMARK_POST -> viewModel.getBookmarkedPost(curPage, pageSize)
                                }

                            }
                        }
                    }
                }

            })
        }

    }

    private fun refreshRv(){

        viewModel.myPostList.observe(viewLifecycleOwner, Observer {
            (viewDataBinding.actMyBoardPageRv.adapter as BaseRecyclerViewAdapter<MyPost, *>).run{
                addItem(it)
                notifyDataSetChanged()
            }
        })

        viewModel.myCommentList.observe(viewLifecycleOwner, Observer {
            (viewDataBinding.actMyBoardPageRv.adapter as BaseRecyclerViewAdapter<MyComment, *>).run{
                addItem(it)
                notifyDataSetChanged()
            }
        })

        viewModel.bookmarkedPostList.observe(viewLifecycleOwner, Observer {
            (viewDataBinding.actMyBoardPageRv.adapter as BaseRecyclerViewAdapter<PostInfo, *>).run{
                addItem(it)
                notifyDataSetChanged()
            }
        })

        viewModel.bookmarkedFeedList.observe(viewLifecycleOwner, Observer {
            (viewDataBinding.actMyBoardPageRv.adapter as BaseRecyclerViewAdapter<Feed, *>).run{
                addItem(it)
                notifyDataSetChanged()
            }
        })
    }

    private fun setPullToRefresh(){
        viewDataBinding.fragMyBoardPageSrl.run{
            setOnRefreshListener {
                (viewDataBinding.actMyBoardPageRv.adapter as BaseRecyclerViewAdapter<*, *>).clearAll()
                curPage = 0

                when(myBoardType){
                    MyBoardType.MY_POST -> viewModel.getMyPost(curPage, pageSize)
                    MyBoardType.MY_COMMENT -> viewModel.getMyComment(curPage, pageSize)
                    MyBoardType.BOOKMARK_POST -> viewModel.getBookmarkedPost(curPage, pageSize)
                    MyBoardType.BOOKMARK_NEWS -> viewModel.getBookmarkedFeed(curPage)
                }
                viewDataBinding.fragMyBoardPageSrl.isRefreshing = false
            }
        }
    }

    companion object {

        fun newInstance(myBoardType: Int): MyBoardPageFragment {
            val frg = MyBoardPageFragment()
            val bundle = Bundle()
            bundle.putInt("myBoardType", myBoardType)
            frg.arguments = bundle
            return frg
        }
    }

    object MyBoardType {
        const val MY_POST = 0
        const val MY_COMMENT = 1
        const val BOOKMARK_POST = 2
        const val BOOKMARK_NEWS = 3

    }

}