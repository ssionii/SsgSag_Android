package com.icoo.ssgsag_android.ui.main.community.board

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.databinding.FragmentBoardListPageBinding
import com.icoo.ssgsag_android.databinding.ItemBoardBinding
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardListPageFragment : BaseFragment<FragmentBoardListPageBinding, CommunityBoardViewModel>(), BaseRecyclerViewAdapter.OnItemClickListener{

    override val layoutResID: Int
        get() = R.layout.fragment_board_list_page

    override val viewModel: CommunityBoardViewModel by viewModel()

    var category = "ALL"
    var communityBoardType = CommunityBoardType.COUNSEL

    var curPage = 0
    var pageSize = 10

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        category = requireArguments().getString("category")!!
        communityBoardType = requireArguments().getInt("communityBoardType")

        viewModel.getCounselList(category, curPage, pageSize)
        setRv()

    }


    private fun setRv(){

        viewDataBinding.actCommunityBoardRv.run{
            adapter = object : BaseRecyclerViewAdapter<BoardPostDetail, ItemBoardBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_board
                override val bindingVariableId: Int
                    get() = BR.postDetail
                override val listener: OnItemClickListener?
                    get() = this@BoardListPageFragment
            }

            layoutManager = LinearLayoutManager(requireContext())
        }

        val emptyView = viewDataBinding.actCommunityBoardLlEmpty
        val recyclerView = viewDataBinding.actCommunityBoardRv

        viewModel.postList.observe(this, Observer {
            (viewDataBinding.actCommunityBoardRv.adapter as BaseRecyclerViewAdapter<BoardPostDetail, *>).run{
                replaceAll(it)
                notifyDataSetChanged()
                if(it.size > 0) {
                    emptyView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }else{
                    emptyView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }

            }
        })
    }

    override fun onItemClicked(item: Any?, position: Int?) {
        val intent = Intent(requireContext(), BoardPostDetailActivity::class.java)
        intent.putExtra("type", communityBoardType)
        startActivity(intent)
    }


    companion object {

        fun newInstance(category: String, communityBoardType : Int): BoardListPageFragment {
            val frg = BoardListPageFragment()
            val bundle = Bundle()
            bundle.putString("category", category)
            bundle.putInt("communityBoardType", communityBoardType)
            frg.arguments = bundle
            return frg
        }
    }

}