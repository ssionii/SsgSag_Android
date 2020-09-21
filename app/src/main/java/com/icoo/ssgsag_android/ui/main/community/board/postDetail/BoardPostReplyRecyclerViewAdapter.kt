package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailCommentBinding
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailReplyBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class BoardPostReplyRecyclerViewAdapter() : RecyclerView.Adapter<BoardPostReplyRecyclerViewAdapter.ViewHolder>() {

    var itemList = arrayListOf<PostComment>()

    var commentPosition = 0

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
    }

    override fun getItemCount() = itemList.size

    override fun getItemId(position: Int): Long = itemList[position].ccommentIdx.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardPostReplyRecyclerViewAdapter.ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemBoardPostDetailReplyBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_board_post_detail_reply, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: BoardPostReplyRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.dataBinding.postComment = itemList[position]
        holder.dataBinding.itemBoardPostDetailReplyIvLike.setSafeOnClickListener {
            listener?.onLikeClick(itemList[position], commentPosition, position)
        }

        holder.dataBinding.itemBoardPostDetailReplyIvMore.setSafeOnClickListener {
            listener?.onMoreLikeClick(itemList[position],commentPosition, position)
        }

        holder.dataBinding.itemBoardPostDetailReplyTvReply.setSafeOnClickListener {
            listener?.onReplyLikeClick(itemList[position].commentIdx)
        }
    }

    inner class ViewHolder(val dataBinding: ItemBoardPostDetailReplyBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnCommentClickListener {
        fun onLikeClick(postComment: PostComment, commentPosition : Int, replyPosition: Int)
        fun onMoreLikeClick(postComment: PostComment, commentPosition : Int, replyPosition: Int)
        fun onReplyLikeClick(commentIdx: Int)
    }

    private var listener: OnCommentClickListener? = null

    fun setOnCommentClickListener(listener: OnCommentClickListener) {
        this.listener = listener
    }

}