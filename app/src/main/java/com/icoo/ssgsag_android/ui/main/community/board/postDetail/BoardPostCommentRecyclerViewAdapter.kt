package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.community.board.PostComment
import com.icoo.ssgsag_android.databinding.ItemAllPosterCollectionBinding
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailCommentBinding
import com.icoo.ssgsag_android.databinding.ItemBoardPostDetailReplyBinding
import com.icoo.ssgsag_android.databinding.ItemKakaoAdsBinding
import com.icoo.ssgsag_android.ui.main.allPosters.collection.GoogleAdsViewHolder
import com.icoo.ssgsag_android.ui.main.allPosters.collection.SsgSagAdsViewHolder
import com.icoo.ssgsag_android.ui.main.allPosters.collection.ViewType
import com.icoo.ssgsag_android.ui.main.community.board.BoardPostDetailBottomSheet
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class BoardPostCommentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun getItemCount() = itemList.size

    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == PostCommentViewType.COMMENT) {
            val viewDataBinding = DataBindingUtil.inflate<ItemBoardPostDetailCommentBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_board_post_detail_comment, parent, false
            )
            return CommentViewHolder(viewDataBinding)
        } else {
            val viewDataBinding = DataBindingUtil.inflate<ItemBoardPostDetailReplyBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_board_post_detail_reply, parent, false
            )
            return ReplyViewHolder(viewDataBinding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is CommentViewHolder) {
            holder.dataBinding.postComment = itemList[position]

            holder.dataBinding.itemBoardPostDetailCommentIvLike.setSafeOnClickListener {
                listener?.onLikeClick(itemList[position], position)
            }

            holder.dataBinding.itemBoardPostDetailCommentIvMore.setSafeOnClickListener {
                listener?.onMoreLikeClick(itemList[position], position)
            }

            holder.dataBinding.itemBoardPostDetailCommentTvReply.setSafeOnClickListener {
                listener?.onReplyClick(itemList[position].commentIdx)
            }
        } else if(holder is ReplyViewHolder){
            holder.dataBinding.postComment = itemList[position]

            holder.dataBinding.itemBoardPostDetailReplyIvLike.setSafeOnClickListener {
                listener?.onReplyLikeClick(itemList[position], position)
            }

            holder.dataBinding.itemBoardPostDetailReplyIvMore.setSafeOnClickListener {
                listener?.onReplyMoreLikeClick(itemList[position], position)
            }

            holder.dataBinding.itemBoardPostDetailReplyTvReply.setSafeOnClickListener {
                listener?.onReplyReplyClick(itemList[position].commentIdx)
            }
        }

    }


    interface OnCommentClickListener {
        fun onLikeClick(postComment: PostComment, position: Int)
        fun onMoreLikeClick(postComment: PostComment, position: Int)
        fun onReplyClick(commentIdx: Int)

        fun onReplyLikeClick(postComment: PostComment,position : Int)
        fun onReplyMoreLikeClick(postComment: PostComment, position : Int)
        fun onReplyReplyClick(commentIdx: Int)
    }

    private var listener: OnCommentClickListener? = null

    fun setOnCommentClickListener(listener: OnCommentClickListener) {
        this.listener = listener
    }
}

class CommentViewHolder(val dataBinding: ItemBoardPostDetailCommentBinding) : RecyclerView.ViewHolder(dataBinding.root)
class ReplyViewHolder(val dataBinding: ItemBoardPostDetailReplyBinding) : RecyclerView.ViewHolder(dataBinding.root)

object PostCommentViewType {
    const val COMMENT = 0
    const val REPLY = 1
}