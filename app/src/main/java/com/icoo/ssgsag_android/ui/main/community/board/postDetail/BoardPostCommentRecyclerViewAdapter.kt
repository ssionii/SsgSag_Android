package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailCommentBinding

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


    override fun getItemCount() = itemList.size

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
            val replyAdapter = BoardPostReplyRecyclerViewAdapter()
            replyAdapter.setOnCommentClickListener(replyClickListener)

            holder.dataBinding.itemBoardPostDetailRvReply.run {
                adapter = replyAdapter
                layoutManager = LinearLayoutManager(context)
            }

            replyAdapter.replaceAll(itemList[position].communityCCommentList)
        }

    }

    val replyClickListener = object : BoardPostReplyRecyclerViewAdapter.OnCommentClickListener{
        override fun onLikeClick(commentIdx: Int) {

        }
    }

    inner class ViewHolder(val dataBinding: ItemBoardPostDetailCommentBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnCommentClickListener {
        fun onLikeClick(commentIdx: Int)
    }

    private var listener: OnCommentClickListener? = null

    fun setOnCommentClickListener(listener: OnCommentClickListener) {
        this.listener = listener
    }

}