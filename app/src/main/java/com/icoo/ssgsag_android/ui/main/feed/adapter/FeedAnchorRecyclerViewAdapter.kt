package com.icoo.ssgsag_android.ui.main.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.databinding.ItemFeedAnchorBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class FeedAnchorRecyclerViewAdapter(
    var itemList: ArrayList<Category>?
) : RecyclerView.Adapter<FeedAnchorRecyclerViewAdapter.ViewHolder>() {

    private var listener: OnFeedAnchorItemClickListener? = null

    fun setOnFeedAnchorItemClickListener(listener: OnFeedAnchorItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<Category>?) {
        itemList?.clear()

        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
    }

    fun addItem(array: ArrayList<Category>?) {
        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return itemList!![position].categoryIdx.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemFeedAnchorBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_feed_anchor, parent, false
        )

        viewDataBinding.root.minimumWidth = parent.measuredWidth/4

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int{
        if(itemList != null)
            return itemList!!.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.category = itemList!![position]
        holder.dataBinding.root.setSafeOnClickListener {
            listener?.onItemClicked(
                position, itemList!![position].categoryIdx
            )
        }
    }

    inner class ViewHolder(val dataBinding: ItemFeedAnchorBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    interface OnFeedAnchorItemClickListener {
        fun onItemClicked(position: Int, categoryIdx: Int)
    }

}