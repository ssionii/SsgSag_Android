package com.icoo.ssgsag_android.ui.main.community

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.CommunityMainCollection
import com.icoo.ssgsag_android.data.model.community.RecruitTeamMain
import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.databinding.*
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class CommunityCollectionRecyclerView(private var itemList : CommunityMainCollection): RecyclerView.Adapter<CommunityCollectionRecyclerView.ViewHolder>() {

    private var collectionListener: OnCommunityCollectionClickListener? = null

    fun setOnCommunityCollectionClickListener(listener: OnCommunityCollectionClickListener) {
        this.collectionListener = listener
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
            1 -> return CommunityViewType.BOARD_COUNSEL
            2 -> return CommunityViewType.BOARD_TALK
            else -> return CommunityViewType.REVIEW
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val viewType = getItemViewType(position)

        when(viewType) {
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
                            get() = object : OnItemClickListener {
                                override fun onItemClicked(item: Any?, position: Int?) {
                                    collectionListener?.onItemClick(viewType, (item as PostInfo).communityIdx)
                                }
                            }
                    }
                    layoutManager = LinearLayoutManager(context)
                }

                if(itemList.recruitTeamList != null && itemList.recruitTeamList!!.size > 0) {
                    (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<RecruitTeamMain, *>).run {
                        replaceAll(itemList.recruitTeamList)
                        notifyDataSetChanged()
                    }
                }else{
                    holder.dataBinding.root.layoutParams.height = 0
                }
            holder.dataBinding.itemCommunityCollectionLlMore.setSafeOnClickListener {
                collectionListener?.onMoreClick(CommunityViewType.RECRUIT)
            }

            }
            CommunityViewType.BOARD_COUNSEL-> {

                holder.dataBinding.itemCommunityCollectionRv.apply {
                    adapter = object :
                        BaseRecyclerViewAdapter<PostInfo, ItemCommunityBoardBinding>() {
                        override val layoutResID: Int
                            get() = R.layout.item_community_board
                        override val bindingVariableId: Int
                            get() = BR.postInfo
                        override val listener: OnItemClickListener?
                            get() = object : OnItemClickListener {
                                override fun onItemClicked(item: Any?, position: Int?) {
                                    collectionListener?.onItemClick(viewType, (item as PostInfo).communityIdx)
                                }
                            }
                    }
                    layoutManager = LinearLayoutManager(context)
                }

                if(itemList.worryCommunityList != null && itemList.worryCommunityList!!.size > 0) {

                    val tempList = itemList.worryCommunityList
                    for(i in 0 until tempList!!.size){
                        tempList[i].isBest = 1
                    }

                    holder.dataBinding.itemCommunityCollectionTvTitle.text = "고민 상담톡"
                    (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<PostInfo, *>).run {
                        replaceAll(tempList)
                        notifyDataSetChanged()
                    }
                }else {
                    holder.dataBinding.root.layoutParams.height = 0
                }

                holder.dataBinding.itemCommunityCollectionLlMore.setSafeOnClickListener {
                    collectionListener?.onMoreClick(viewType)
                }
            }

            CommunityViewType.BOARD_TALK -> {
                holder.dataBinding.itemCommunityCollectionRv.apply {
                    adapter = object :
                        BaseRecyclerViewAdapter<PostInfo, ItemCommunityBoardBinding>() {
                        override val layoutResID: Int
                            get() = R.layout.item_community_board
                        override val bindingVariableId: Int
                            get() = BR.postInfo
                        override val listener: OnItemClickListener?
                            get() = object : OnItemClickListener {
                                override fun onItemClicked(item: Any?, position: Int?) {
                                    collectionListener?.onItemClick(viewType, (item as PostInfo).communityIdx)
                                }
                            }
                    }
                    layoutManager = LinearLayoutManager(context)
                }

                if(itemList.freeCommunityList != null && itemList.freeCommunityList!!.size > 0) {

                    val tempList = itemList.freeCommunityList
                    for(i in 0 until tempList!!.size){
                        tempList[i].isBest = 1
                    }

                    holder.dataBinding.itemCommunityCollectionTvTitle.text = "자유 수다톡"
                    (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<PostInfo, *>).run {
                        replaceAll(tempList)
                        notifyDataSetChanged()
                    }
                }else {
                    holder.dataBinding.root.layoutParams.height = 0
                }

                holder.dataBinding.itemCommunityCollectionLlMore.setSafeOnClickListener {
                    collectionListener?.onMoreClick(viewType)
                }
            }

            else -> {
                holder.dataBinding.itemCommunityCollectionTvTitle.text = "최신 후기"
                holder.dataBinding.itemCommunityCollectionRv.apply {
                    adapter = object :
                        BaseRecyclerViewAdapter<ClubPost, ItemCommunityReviewBinding>() {
                        override val layoutResID: Int
                            get() = R.layout.item_community_review
                        override val bindingVariableId: Int
                            get() = BR.reviewMain
                        override val listener: OnItemClickListener?
                            get() = object : OnItemClickListener {
                                override fun onItemClicked(item: Any?, position: Int?) {
                                    collectionListener?.onItemClick(viewType, (item as ClubPost).clubIdx!!)
                                }
                            }
                    }
                    layoutManager = LinearLayoutManager(context)
                }

                if(itemList.clubAndClubPostList != null && itemList.clubAndClubPostList!!.size > 0) {

                    (holder.dataBinding.itemCommunityCollectionRv.adapter as BaseRecyclerViewAdapter<ClubPost, *>).run {
                        replaceAll(itemList.clubAndClubPostList)
                        notifyDataSetChanged()
                    }
                }else{
                    holder.dataBinding.root.layoutParams.height = 0
                }

                holder.dataBinding.itemCommunityCollectionLlMore.setSafeOnClickListener {
                    collectionListener?.onMoreClick(viewType)
                }
            }
        }
    }


    inner class ViewHolder(val dataBinding: ItemCommunityCollectionBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnCommunityCollectionClickListener{
        fun onMoreClick(communityType: Int)
        fun onItemClick(communityType: Int, idx : Int)
    }
}



object CommunityViewType {
    const val RECRUIT = 0
    const val BOARD_COUNSEL = 1
    const val BOARD_TALK = 2
    const val REVIEW = 3
}
