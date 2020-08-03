package com.icoo.ssgsag_android.ui.main.allPosters.collection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.poster.allPoster.AdPosterCollection
import com.icoo.ssgsag_android.databinding.ItemAllPosterCollectionBinding
import com.icoo.ssgsag_android.ui.main.allPosters.CardViewPagerAdapter
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class AllPosterCollectionRecyclerViewAdapter : RecyclerView.Adapter<AllPosterCollectionRecyclerViewAdapter.ViewHolder>() {

    private var listener: OnAllPosterCollectionClickListener? = null
    var itemList = arrayListOf<AdPosterCollection>()

    private var density = 0f
    private var leftMargin = 0
    private var middleMargin = 0
    private var rightMargin = 0
    private var contentWidth= 0

    fun setOnAllPosterCollectionClickListener(listener: OnAllPosterCollectionClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<AdPosterCollection>?) {
        array?.let {
            this.itemList.run {
                clear()
                addAll(it)
            }
        }
    }

    fun setMargin(d : Float, left : Int, middle : Int, right : Int, content : Int){
        density = d
        leftMargin = (left * d).toInt()
        middleMargin = (middle * d).toInt()
        rightMargin = (right * d).toInt()
        contentWidth = (content * d).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemAllPosterCollectionBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_all_poster_collection, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.adPosterCollection = itemList[position]
        holder.dataBinding.itemAllPosterCollectionLlMore.setSafeOnClickListener {
            listener?.onMoreClick(itemList[position].categoryIdx)
        }

        val cardViewPagerAdapter = CardViewPagerAdapter(context, itemList[position].adViewItemList)
        cardViewPagerAdapter.apply {
            setOnItemClickListener(onPosterItemClickListener)
            rowIdx = position
            posterWidth = contentWidth
        }

        holder.dataBinding.itemAllPosterCollectionVp.run{
            clipToPadding = false
            setPadding(leftMargin, 0, rightMargin, 0)
            pageMargin = middleMargin
            adapter = cardViewPagerAdapter
        }

    }

    val onPosterItemClickListener
            = object : CardViewPagerAdapter.OnItemClickListener{
        override fun onPosterClick(posterIdx: Int, rowIdx : Int, position: Int) {
            listener?.onPosterItemClick(posterIdx, rowIdx, position)
        }

        override fun onPosterStore(posterIdx: Int, isSave: Int) {
            listener?.onManagePosterItem(posterIdx, isSave)
        }

        override fun onPosterStoreCancel(posterIdx: Int, isSave: Int) {
            listener?.onManagePosterItem(posterIdx, isSave)
        }
    }


    override fun getItemId(position: Int): Long {
        return itemList[position].categoryIdx.toLong()
    }

    inner class ViewHolder(val dataBinding: ItemAllPosterCollectionBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnAllPosterCollectionClickListener{
        fun onMoreClick(categoryIdx: Int)
        fun onPosterItemClick(posterIdx: Int, rowIdx : Int, position: Int)
        fun onManagePosterItem(posterIdx : Int, isSave : Int)
    }

}