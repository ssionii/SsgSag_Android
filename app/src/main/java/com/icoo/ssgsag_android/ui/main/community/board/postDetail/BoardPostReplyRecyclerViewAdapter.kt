package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailCommentBinding
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailReplyBinding

class BoardPostReplyRecyclerViewAdapter() : RecyclerView.Adapter<BoardPostReplyRecyclerViewAdapter.ViewHolder>() {

    private var itemList = arrayListOf<PostComment>()

    fun replaceAll(array: ArrayList<PostComment>?) {
        array?.let {
            this.itemList.run {
                clear()
                addAll(it)
            }
        }
    }


    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardPostReplyRecyclerViewAdapter.ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemBoardPostDetailReplyBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_board_post_detail_reply, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: BoardPostReplyRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.dataBinding.postComment = itemList[position]

    }

    inner class ViewHolder(val dataBinding: ItemBoardPostDetailReplyBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnCommentClickListener {
        fun onLikeClick(commentIdx: Int)
    }

    private var listener: OnCommentClickListener? = null

    fun setOnCommentClickListener(listener: OnCommentClickListener) {
        this.listener = listener
    }

}