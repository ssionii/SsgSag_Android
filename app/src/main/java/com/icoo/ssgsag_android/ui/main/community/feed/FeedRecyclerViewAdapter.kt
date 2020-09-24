package com.icoo.ssgsag_android.ui.main.community.feed

import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.databinding.ItemFeedBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class FeedRecyclerViewAdapter: RecyclerView.Adapter<FeedRecyclerViewAdapter.ViewHolder>() {

    private var itemList = arrayListOf<Feed>()

    fun replaceAll(array: ArrayList<Feed>?) {
        itemList.clear()

        if (array != null) {
            for (i in array.indices) {
                this.itemList.add(array[i])
            }
        }
        notifyDataSetChanged()
    }

    fun clearAll() {
        itemList.clear()
    }

    fun addItem(array: ArrayList<Feed>?) {
        if (array != null) {
            for (i in array.indices) {
                this.itemList.add(array[i])
            }
        }
    }

    fun refreshItem(newFeed : Feed, position: Int){
        itemList[position] = newFeed
        notifyItemChanged(position)
    }

    fun removedItem(position: Int){

        val iter = itemList.iterator()
        var index = 0
        while (iter.hasNext()) {
            val i = iter.next()
            if (index == position) {
                iter.remove()
            }
            index++
        }
    }

    fun refreshBookmark(position:Int, curSave: Int){
        if(curSave == 0){
            itemList[position].isSave = 1
        }else{
            itemList[position].isSave = 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_feed, parent, false
        )

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int = itemList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.feed = itemList[position]
        holder.dataBinding.root.setSafeOnClickListener {
            listener?.onItemClicked(
                itemList[position].feedIdx, itemList[position].feedUrl,
                itemList[position].feedName, itemList[position].isSave, position
            )
        }

        holder.dataBinding.itemFeedIvBookmark.setSafeOnClickListener {
            listener?.onBookmarkClicked(itemList[position], position)
        }
    }

    override fun getItemId(position: Int): Long  = itemList[position].feedIdx.toLong()

    inner class ViewHolder(val dataBinding: ItemFeedBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    private var listener: OnFeedItemClickListener? = null

    fun setOnFeedItemClickListener(listener: OnFeedItemClickListener) {
        this.listener = listener
    }

    interface OnFeedItemClickListener {
        fun onItemClicked(feedIdx: Int, feedUrl: String, feedName: String, isSaved: Int, position: Int)
        fun onBookmarkClicked(feedItem: Feed, position: Int)
    }

}