package com.icoo.ssgsag_android.ui.main.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.BoardMain
import com.icoo.ssgsag_android.data.model.community.CommunityMainCollection
import com.icoo.ssgsag_android.data.model.community.RecruitTeamMain
import com.icoo.ssgsag_android.data.model.community.ReviewMain
import com.icoo.ssgsag_android.data.model.poster.allPoster.AdPosterCollection
import com.icoo.ssgsag_android.databinding.*
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.allPosters.collection.AllPosterCardViewPagerAdapter
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class CommunityCollectionRecyclerView(private var itemList : CommunityMainCollection): RecyclerView.Adapter<CommunityCollectionRecyclerView.ViewHolder>() {

    private var listener: OnCommunityCollectionClickListener? = null

    fun setOnCommunityCollectionClickListener(listener: OnCommunityCollectionClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: CommunityMainCollection?) {
        array?.let {
            itemList = it
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityCollectionRecyclerView.ViewHolder {

        val viewDataBinding = DataBindingUtil.inflate<ItemCommunityCollectionBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_community_collection, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() : Int = 4

    override fun getItemViewType(position: Int): Int {
        when(position){
            0 -> return CommunityViewType.RECRUIT
            1, 2 -> return CommunityViewType.BOARD
            else -> return CommunityViewType.REVIEW
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            CommunityViewType.RECRUIT -> {
                holder.dataBinding.itemCommunityCollectionTvTitle.text = "팀원 모집중"
                holder.dataBinding.itemCommunityCollectionRv.apply {
                    adapter = object :
                        BaseRecyclerViewAdapter<RecruitTeamMain, ItemCommunityRecruitBinding>() {
                        override val layoutResID: Int
                            get() = R.layout.item_community_recruit
                        override val bindingVariableId: Int
                            get() = BR.recruitTeam
                        override val listener: OnItemClickListener?
                            get() = null
                    }
                    layoutManager = LinearLayoutManager(context)
                }

                (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<RecruitTeamMain, *>).run {
                    replaceAll(itemList.recruitTeamList)
                    notifyDataSetChanged()
                }
            }
            CommunityViewType.BOARD -> {

                holder.dataBinding.itemCommunityCollectionRv.apply {
                    adapter = object :
                        BaseRecyclerViewAdapter<BoardMain, ItemCommunityBoardBinding>() {
                        override val layoutResID: Int
                            get() = R.layout.item_community_board
                        override val bindingVariableId: Int
                            get() = BR.boardMain
                        override val listener: OnItemClickListener?
                            get() = null
                    }
                    layoutManager = LinearLayoutManager(context)
                }

                when(position){
                    1 -> {
                        holder.dataBinding.itemCommunityCollectionTvTitle.text = "고민 상담 Talk"
                        (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<BoardMain, *>).run {
                            replaceAll(itemList.counselList)
                            notifyDataSetChanged()
                        }
                    }
                    2 -> {
                        holder.dataBinding.itemCommunityCollectionTvTitle.text = "자유 수다 Talk"
                        (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<BoardMain, *>).run {
                            replaceAll(itemList.talkList)
                            notifyDataSetChanged()
                        }
                    }
                }
            }
            else -> {
                holder.dataBinding.itemCommunityCollectionTvTitle.text = "최신 후기"
                holder.dataBinding.itemCommunityCollectionRv.apply {
                    adapter = object :
                        BaseRecyclerViewAdapter<ReviewMain, ItemCommunityReviewBinding>() {
                        override val layoutResID: Int
                            get() = R.layout.item_community_review
                        override val bindingVariableId: Int
                            get() = BR.reviewMain
                        override val listener: OnItemClickListener?
                            get() = null
                    }
                    layoutManager = LinearLayoutManager(context)
                }

                (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<ReviewMain, *>).run {
                    replaceAll(itemList.reviewList)
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class ViewHolder(val dataBinding: ItemCommunityCollectionBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnCommunityCollectionClickListener{
        fun onMoreClick(categoryIdx: Int)
        fun onPosterItemClick(posterIdx: Int, rowIdx : Int, position: Int)
        fun onManagePosterItem(posterIdx : Int, isSave : Int)
    }
}



object CommunityViewType {
    const val RECRUIT = 0
    const val BOARD = 1
    const val REVIEW = 2
}
