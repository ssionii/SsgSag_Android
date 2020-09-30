package com.icoo.ssgsag_android.ui.main.userNotice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.user.userNotice.UserNotice
import com.icoo.ssgsag_android.databinding.FragmentUserNoticeBinding
import com.icoo.ssgsag_android.databinding.ItemUserNoticeBinding
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.community.board.CommunityBoardType
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailActivity
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserNoticeFragment : BaseFragment<FragmentUserNoticeBinding,UserNoticeViewModel>(){

    override val layoutResID: Int
        get() = R.layout.fragment_user_notice

    override val viewModel: UserNoticeViewModel by viewModel()

    var curPage = 0
    var pageSize = 10


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.fragUserNoticeToolbar.toolbarTitleTv.text = "알림"

        setRv()
        refreshRv()
        setPullToRefresh()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            viewModel.getUserNotice(curPage, pageSize)
        }
    }


    private fun setRv(){
        viewDataBinding.fragUserNoticeRv.run{
            adapter = object : BaseRecyclerViewAdapter<UserNotice, ItemUserNoticeBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_user_notice

                override val bindingVariableId: Int
                    get() = BR.userNotice

                override val listener: OnItemClickListener?
                    get() = object : OnItemClickListener{
                        override fun onItemClicked(item: Any?, position: Int?) {
                            val notice = item as UserNotice
                            when(notice.category){
                                "COMMUNITY" -> {
                                    val intent = Intent(requireActivity(), BoardPostDetailActivity::class.java)
                                    intent.putExtra("type", CommunityBoardType.COUNSEL)
                                    intent.putExtra("postIdx", notice.contentIdx)

                                    startActivity(intent)
                                }

                                "POSTER_END" -> {
                                    val intent = Intent(requireActivity(), CalendarDetailActivity::class.java)
                                    intent.putExtra("Idx", notice.contentIdx)

                                    startActivity(intent)
                                }
                            }
                        }
                    }
            }

            layoutManager = WrapContentLinearLayoutManager()

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {
                            if (this.itemCount > 0 && (10 * (curPage + 1) - 2 < position)) {
                                curPage = (position + 1) / 10
                                viewModel.getUserNotice(curPage, pageSize)
                            }
                        }

                    }
                }

            })

        }
    }

    private fun setPullToRefresh(){
        viewDataBinding.fragUserNoticeSrl.run{
            setOnRefreshListener {
                (viewDataBinding.fragUserNoticeRv.adapter as BaseRecyclerViewAdapter<*,*>).clearAll()
                curPage = 0
                viewModel.getUserNotice(curPage, pageSize)
                viewDataBinding.fragUserNoticeSrl.isRefreshing = false
            }
        }
    }

    private fun refreshRv(){
        viewModel.noticeList.observe(viewLifecycleOwner, Observer {
            (viewDataBinding.fragUserNoticeRv.adapter as BaseRecyclerViewAdapter<UserNotice, *>).run{
                addItem(it)
                notifyDataSetChanged()
            }
        })
    }
    
}