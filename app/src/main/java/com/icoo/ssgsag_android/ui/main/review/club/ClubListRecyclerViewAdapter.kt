package com.icoo.ssgsag_android.ui.main.feed.category

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.review.club.ClubInfo
import com.icoo.ssgsag_android.databinding.ItemClubReviewBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class ClubListRecyclerViewAdapter(
    var itemList: ArrayList<ClubInfo>
) : RecyclerView.Adapter<ClubListRecyclerViewAdapter.ViewHolder>(){

    private var listener: OnReviewClickListener? = null

    fun setOnReviewClickListener(listener: OnReviewClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<ClubInfo>?) {
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

    fun addItem(array: ArrayList<ClubInfo>?) {
        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemClubReviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_club_review, parent, false
        )

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.clubInfo = itemList[position]
//        holder.dataBinding.itemReviewLlContainer.setSafeOnClickListener {
//            listener?.onItemClickListener(itemList[position].clubIdx)
//        }

        val categoryList = itemList[position].categoryList.split(",")
        for(i in 0.. categoryList.size - 1){
            when(i){
                0 ->{
                    holder.dataBinding.itemClubReviewCvCategory1.visibility = VISIBLE
                    holder.dataBinding.itemClubReviewTvCategory1.text = categoryList[i]
                }
                1 ->{
                    holder.dataBinding.itemClubReviewCvCategory2.visibility = VISIBLE
                    holder.dataBinding.itemClubReviewTvCategory2.text = categoryList[i]
                }
                2 ->{
                    holder.dataBinding.itemClubReviewCvCategory3.visibility = VISIBLE
                    holder.dataBinding.itemClubReviewTvCategory3.text = categoryList[i]
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].clubIdx.toLong()
    }

    inner class ViewHolder(val dataBinding: ItemClubReviewBinding) :
        RecyclerView.ViewHolder(dataBinding.root){
        init {
            dataBinding.root.setSafeOnClickListener {
                listener?.onItemClickListener(itemList[adapterPosition].clubIdx)
            }
        }
    }

    interface OnReviewClickListener {
        fun onItemClickListener(clubIdx: Int)
    }

}