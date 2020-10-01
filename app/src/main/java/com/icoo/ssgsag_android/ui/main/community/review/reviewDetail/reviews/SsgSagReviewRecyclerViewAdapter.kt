package com.icoo.ssgsag_android.ui.main.review.reviewDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.*
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.databinding.ItemReviewSsgsagBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class SsgSagReviewRecyclerViewAdapter(
    var itemList: ArrayList<ClubPost>?
) : RecyclerView.Adapter<SsgSagReviewRecyclerViewAdapter.ViewHolder>(){

    private var listener: OnSsgSagReviewClickListener? = null
    var isMore = false

    fun setOnSsgSagReviewClickListener(listener: OnSsgSagReviewClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<ClubPost>?) {
        itemList?.clear()

        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
        notifyDataSetChanged()
    }

    fun clearAll(){
        itemList?.clear()
    }

    fun replaceItem(item: ClubPost, position: Int){
        itemList!![position] = item
        notifyItemChanged(position)
    }

    fun addItem(array: ArrayList<ClubPost>?) {
        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
    }

    fun deleteItem(position: Int){
        itemList!!.removeAt(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemReviewSsgsagBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_review_ssgsag, parent, false
        )

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() : Int {
        if(itemList == null)
            return 0
        else if(isMore)
            return itemList!!.size
        else if(itemList!!.size < 3)
            return itemList!!.size
        else
            return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isExtend = false
        if(itemList != null) {
            holder.dataBinding.ssgsagReview = itemList!![position]

            holder.dataBinding.itemReviewSsgsagTvAdvantage.maxLines = 100
            holder.dataBinding.itemReviewSsgsagTvDisadvantage.maxLines = 100

            var moreVisi = false
            // 더보기와 물퍼짐 효과가 필요한 경우 (확장 해야 하는 경우)
            if(holder.dataBinding.itemReviewSsgsagTvAdvantage.lineCount > 3
                || holder.dataBinding.itemReviewSsgsagTvDisadvantage.lineCount > 2
                || itemList!![position].honeyTip != ""){
                moreVisi = true

                holder.dataBinding.itemReviewSsgsagTvAdvantage.maxLines = 3
                holder.dataBinding.itemReviewSsgsagTvDisadvantage.maxLines = 2

                holder.dataBinding.itemReviewSsgsagTvMore.visibility = VISIBLE
                if(holder.dataBinding.itemReviewSsgsagLlTip.visibility == VISIBLE){
                    isExtend = true
                }
            }else{
                moreVisi = false
                holder.dataBinding.itemReviewSsgsagTvMore.visibility = GONE
                holder.dataBinding.itemReviewSsgsagLlUserInfo.visibility = VISIBLE
            }

            if(holder.dataBinding.itemReviewSsgsagTvMore.visibility == VISIBLE){
                holder.dataBinding.itemReviewSsgsagLlUserInfo.visibility = GONE
            }

            if(isMore) holder.dataBinding.itemReviewSsgsagLlUserInfo.visibility = VISIBLE

            holder.dataBinding.root.setOnClickListener {
                if(!isMore) {
                    if (!isExtend) {
                        holder.dataBinding.itemReviewSsgsagTvAdvantage.maxLines = 100
                        holder.dataBinding.itemReviewSsgsagTvDisadvantage.maxLines = 100

                        if(holder.dataBinding.itemReviewSsgsagTvTip.text != "") {
                            holder.dataBinding.itemReviewSsgsagLlTip.visibility = VISIBLE
                        }

                        holder.dataBinding.itemReviewSsgsagTvMore.visibility = GONE
                        holder.dataBinding.itemReviewSsgsagLlUserInfo.visibility = VISIBLE

                        isExtend = true
                    }else {
                        holder.dataBinding.itemReviewSsgsagTvAdvantage.maxLines = 3
                        holder.dataBinding.itemReviewSsgsagTvDisadvantage.maxLines = 2
                        holder.dataBinding.itemReviewSsgsagLlTip.visibility = GONE
                        holder.dataBinding.itemReviewSsgsagLlUserInfo.visibility = GONE

                        if(moreVisi) {
                            holder.dataBinding.itemReviewSsgsagTvMore.visibility = VISIBLE
                            holder.dataBinding.itemReviewSsgsagLlUserInfo.visibility = GONE
                        }else {
                            holder.dataBinding.itemReviewSsgsagTvMore.visibility = GONE
                            holder.dataBinding.itemReviewSsgsagLlUserInfo.visibility = VISIBLE
                        }
                        isExtend = false
                    }
                }
            }


            holder.dataBinding.itemReviewSsgsagClHelp.setSafeOnClickListener {
                listener?.onHelpClickListener(itemList!![position].isLike, itemList!![position].clubPostIdx, position)
            }

            if(isMore){
                holder.dataBinding.itemReviewSsgsagTvAdvantage.maxLines = 100
                holder.dataBinding.itemReviewSsgsagTvDisadvantage.maxLines = 100
                if(itemList!![position].honeyTip != "")
                    holder.dataBinding.itemReviewSsgsagLlTip.visibility = VISIBLE
                else
                    holder.dataBinding.itemReviewSsgsagLlTip.visibility = GONE
                holder.dataBinding.itemReviewSsgsagTvMore.visibility = INVISIBLE

            }

            holder.dataBinding.itemReviewSsgsagIvEdit.run {
                this.setOnClickListener {
                    val popup = PopupMenu(context, it)
                    val inflater = popup.menuInflater
                    val menu = popup.menu

                    inflater.inflate(R.menu.menu_cal_detail_comment, menu)
                    if(itemList!![position].isMine == 1) {
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
                                R.id.menu_cal_detail_edit -> listener?.onEditClicked(itemList!![position], position)
                                R.id.menu_cal_detail_delete -> listener?.onDeleteClicked(itemList!![position].clubPostIdx, position)
                                R.id.menu_cal_detail_caution -> listener?.onCautionClicked(itemList!![position].clubPostIdx)
                            }
                            return false
                        }
                    })
                    popup.show()
                }
            }
        }

    }

    override fun getItemId(position: Int): Long {
        if(itemList != null) {
            return itemList!![position].clubPostIdx.toLong()
        }else
            return 0
    }

    inner class ViewHolder(val dataBinding: ItemReviewSsgsagBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    interface OnSsgSagReviewClickListener {
        fun onHelpClickListener(isLike: Int, idx: Int, position: Int)
        fun onEditClicked(item: ClubPost, position: Int)
        fun onDeleteClicked(idx: Int, position:Int)
        fun onCautionClicked(idx: Int)
    }
}