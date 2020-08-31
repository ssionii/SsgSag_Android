package com.icoo.ssgsag_android.ui.main.community.review

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.review.club.ClubInfo
import com.icoo.ssgsag_android.databinding.ItemReviewBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class ReviewListRecyclerViewAdapter(
    var itemList: ArrayList<ClubInfo>
) : RecyclerView.Adapter<ReviewListRecyclerViewAdapter.ViewHolder>(){

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
        val viewDataBinding = DataBindingUtil.inflate<ItemReviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_review, parent, false
        )

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.clubInfo = itemList[position]

        if(itemList[position].clubType != 0 && itemList[position].clubType != 1){
            holder.dataBinding.itemReviewLlBox.visibility = GONE
        }

        val categoryList = itemList[position].categoryList.split(",")
        for(i in 0.. categoryList.size - 1){
            when(i){
                0 ->{
                    if(categoryList[i] != "") {
                        holder.dataBinding.itemClubReviewCvCategory1.visibility = VISIBLE
                        holder.dataBinding.itemClubReviewTvCategory1.text = categoryList[i]
                    }else{
                        holder.dataBinding.itemClubReviewCvCategory1.visibility = GONE
                    }
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

    inner class ViewHolder(val dataBinding: ItemReviewBinding) :
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