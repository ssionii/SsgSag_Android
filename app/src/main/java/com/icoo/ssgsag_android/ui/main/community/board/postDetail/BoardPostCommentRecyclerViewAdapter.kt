package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailCommentBinding
import com.icoo.ssgsag_android.ui.main.community.board.BoardPostDetailBottomSheet
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class BoardPostCommentRecyclerViewAdapter() : RecyclerView.Adapter<BoardPostCommentRecyclerViewAdapter.ViewHolder>() {

    private var itemList = arrayListOf<PostComment>()

    fun replaceAll(array: ArrayList<PostComment>?) {
        array?.let {
            this.itemList.run {
                clear()
                addAll(it)
            }
        }
    }

    fun replaceItem(item : PostComment, position : Int){
        itemList[position] = item
        notifyItemChanged(position)
    }

    fun replaceSubItem(item : PostComment, commentPosition : Int, replyPosition : Int){
        val tempSubList = itemList[commentPosition]
        if(tempSubList.communityCCommentList != null) {
            tempSubList.communityCCommentList!![replyPosition] = item
        }

        itemList[commentPosition] = tempSubList

        notifyItemChanged(commentPosition)

        (replyAdapterList[commentPosition] as BoardPostReplyRecyclerViewAdapter).replaceItem(item, replyPosition)
    }

    val replyAdapterList = hashMapOf<Int, BoardPostReplyRecyclerViewAdapter>()

    override fun getItemCount() = itemList.size

    override fun getItemId(position: Int) = itemList[position].commentIdx.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardPostCommentRecyclerViewAdapter.ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemBoardPostDetailCommentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_board_post_detail_comment, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: BoardPostCommentRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.dataBinding.postComment = itemList[position]

        if(itemList[position].communityCCommentList != null) {

            replyAdapterList[position] = BoardPostReplyRecyclerViewAdapter()
            val replyAdapter =  replyAdapterList[position]!!

            replyAdapter.apply {
                setOnCommentClickListener(replyClickListener)
                setHasStableIds(true)
                commentPosition = position
            }

            holder.dataBinding.itemBoardPostDetailRvReply.run {
                adapter = replyAdapter
                layoutManager = LinearLayoutManager(context)
            }

            replyAdapter.replaceAll(itemList[position].communityCCommentList)
        }

        holder.dataBinding.itemBoardPostDetailCommentIvLike.setSafeOnClickListener {
            listener?.onLikeClick(itemList[position], position)
        }

        holder.dataBinding.itemBoardPostDetailCommentIvMore.setSafeOnClickListener {
            listener?.onMoreLikeClick(itemList[position], position)
        }

        holder.dataBinding.itemBoardPostDetailCommentTvReply.setSafeOnClickListener {
            listener?.onReplyClick(itemList[position].commentIdx)
        }

    }

    val replyClickListener = object : BoardPostReplyRecyclerViewAdapter.OnCommentClickListener{
        override fun onLikeClick(postComment: PostComment, commentPosition : Int, replyPosition: Int) {
            listener?.onReplyLikeClick(postComment, commentPosition, replyPosition)
        }

        override fun onMoreLikeClick(postComment: PostComment, commentPosition : Int, replyPosition: Int) {
           listener?.onReplyMoreLikeClick(postComment, commentPosition, replyPosition)
        }

        override fun onReplyLikeClick(commentIdx: Int) {
            listener?.onReplyReplyClick(commentIdx)
        }
    }

    inner class ViewHolder(val dataBinding: ItemBoardPostDetailCommentBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnCommentClickListener {
        fun onLikeClick(postComment: PostComment, position: Int)
        fun onMoreLikeClick(postComment: PostComment, position: Int)
        fun onReplyClick(commentIdx: Int)

        fun onReplyLikeClick(postComment: PostComment, commentPosition : Int, replyPosition: Int)
        fun onReplyMoreLikeClick(postComment: PostComment, commentPosition : Int, replyPosition: Int)
        fun onReplyReplyClick(commentIdx: Int)
    }

    private var listener: OnCommentClickListener? = null

    fun setOnCommentClickListener(listener: OnCommentClickListener) {
        this.listener = listener
    }

}