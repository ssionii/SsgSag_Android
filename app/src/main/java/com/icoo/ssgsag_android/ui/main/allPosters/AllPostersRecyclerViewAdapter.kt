package com.icoo.ssgsag_android.ui.main.allPosters

import android.view.*
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.databinding.ItemAllPostersBinding
import com.icoo.ssgsag_android.databinding.ItemCalendarListBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class AllPostersRecyclerViewAdapter(
    var itemList: ArrayList<PosterDetail>
) : RecyclerView.Adapter<AllPostersRecyclerViewAdapter.ViewHolder>() {

    private var listener: onAllPosterItemClickListener? = null

    fun setOnAllPosterItemClickListener(listener: onAllPosterItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<PosterDetail>?) {
        array?.let {
            this.itemList.run {
                clear()
                addAll(it)
            }
        }
    }

    fun addItem(array: ArrayList<PosterDetail>?) {
        if (array != null) {
            for (i in array.indices) {
                this.itemList.add(array[i])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemAllPostersBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_all_posters, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.posterDetail = itemList[position]
        holder.dataBinding.root.setSafeOnClickListener {
            listener?.onItemClicked(itemList[position].posterIdx)
        }
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].posterIdx.toLong()
    }

    inner class ViewHolder(val dataBinding: ItemAllPostersBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface onAllPosterItemClickListener {
        fun onItemClicked(posterIdx: Int)

    }

}