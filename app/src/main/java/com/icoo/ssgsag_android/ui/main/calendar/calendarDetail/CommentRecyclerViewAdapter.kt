package com.icoo.ssgsag_android.ui.main.calendar.calendarDetail

import android.content.Context
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.poster.posterDetail.comment.Comment
import com.icoo.ssgsag_android.databinding.ItemCalDetailCommentBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class CommentRecyclerViewAdapter(
    val ctx: Context,
    val items: ArrayList<Comment>
) : RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder>() {

    private var listener: OnCommentItemClickListener? = null

    fun setOnCommentItemClickListener(listener: OnCommentItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(items: ArrayList<Comment>?) {
        items?.let {
            this.items.run {
                clear()
                addAll(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemCalDetailCommentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cal_detail_comment, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.comment = items[position]

        holder.dataBinding.itemCalDetailCommentIvLike.setSafeOnClickListener {
            listener?.onLikeClicked(items[position].commentIdx, items[position].isLike)
        }
        holder.dataBinding.itemCalDetailCommentIvMore.let {
            it.setSafeOnClickListener {
                val popup = PopupMenu(ctx, it)
                val inflater = popup.menuInflater
                val menu = popup.menu

                inflater.inflate(R.menu.menu_cal_detail_comment, menu)
                if(items[position].isMine == 1) {
                    menu.findItem(R.id.menu_cal_detail_edit).setVisible(true)
                    menu.findItem(R.id.menu_cal_detail_delete).setVisible(true)
                    menu.findItem(R.id.menu_cal_detail_caution).setVisible(false)
                } else {
                    menu.findItem(R.id.menu_cal_detail_edit).setVisible(false)
                    menu.findItem(R.id.menu_cal_detail_delete).setVisible(false)
                    menu.findItem(R.id.menu_cal_detail_caution).setVisible(true)
                }

                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.menu_cal_detail_edit -> listener?.onEditClicked(items[position].commentIdx, position)
                            R.id.menu_cal_detail_delete -> listener?.onDeleteClicked(items[position].commentIdx, position)
                            R.id.menu_cal_detail_caution -> listener?.onCautionClicked(items[position].commentIdx)
                        }
                        return false
                    }
                })
                popup.show()
            }
        }
    }

    inner class ViewHolder(val dataBinding: ItemCalDetailCommentBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnCommentItemClickListener {
        fun onEditClicked(commentIdx: Int, position: Int)
        fun onDeleteClicked(commentIdx: Int, position: Int)
        fun onLikeClicked(commentIdx: Int, like: Int)
        fun onCautionClicked(commentIdx: Int)
    }


}