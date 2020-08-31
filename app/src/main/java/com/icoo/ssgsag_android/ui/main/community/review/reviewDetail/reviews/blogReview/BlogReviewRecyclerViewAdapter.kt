package com.icoo.ssgsag_android.ui.main.review.reviewDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.review.club.BlogReview
import com.icoo.ssgsag_android.databinding.ItemReviewBlogBinding
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class BlogReviewRecyclerViewAdapter(
    var itemList: ArrayList<BlogReview>?
) : RecyclerView.Adapter<BlogReviewRecyclerViewAdapter.ViewHolder>(){

    private var listener: OnBlogReviewClickListener? = null
    private var size = 0
    var isMore = false

    fun setOnBlogReviewClickListener(listener: OnBlogReviewClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<BlogReview>?) {
        itemList?.clear()

        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
        notifyDataSetChanged()
    }

    fun addItem(array: ArrayList<BlogReview>?) {
        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
    }

    fun setPhotoSize(displayWidth: Int){

        val paddingDp = 24
        val middleDp = 6
        val d = context.resources.displayMetrics.density
        val horizontalPadding = (paddingDp * d).toInt()
        val middleMargin = (middleDp * d).toInt()

        size = (displayWidth - (horizontalPadding*2) - (middleMargin*2)) / 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemReviewBlogBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_review_blog, parent, false
        )

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() : Int {
        if(itemList == null)
            return 0
        else if(itemList!!.size < 3 || isMore)
            return itemList!!.size
        else
            return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemList != null) {
            holder.dataBinding.blogReview = itemList!![position]

            holder.dataBinding.root.setSafeOnClickListener {
                listener?.onItemClickListener(itemList!![position].blogUrl, itemList!![position].blogTitle)
            }


        }

    }

    override fun getItemId(position: Int): Long {
        if(itemList != null) {
            return itemList!![position].clubBlogIdx.toLong()
        }else
            return 0
    }

    inner class ViewHolder(val dataBinding: ItemReviewBlogBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    interface OnBlogReviewClickListener {
        fun onItemClickListener(url: String, title : String)
    }

}