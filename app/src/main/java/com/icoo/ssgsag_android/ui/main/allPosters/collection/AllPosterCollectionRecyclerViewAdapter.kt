package com.icoo.ssgsag_android.ui.main.allPosters.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.poster.allPoster.AdPosterCollection
import com.icoo.ssgsag_android.databinding.ItemAllPosterCollectionBinding
import com.icoo.ssgsag_android.databinding.ItemGoogleAdsBinding
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class AllPosterCollectionRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == ViewType.SSGSAG_ADS){
            val viewDataBinding = DataBindingUtil.inflate<ItemAllPosterCollectionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_all_poster_collection, parent, false
            )
            return SsgSagAdsViewHolder(viewDataBinding)
        }else {
            val viewDataBinding = DataBindingUtil.inflate<ItemGoogleAdsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_google_ads, parent, false
            )
            return GoogleAdsViewHolder(viewDataBinding)
        }
    }

    override fun getItemCount() = itemList.size + 1

    override fun getItemViewType(position: Int): Int {
        if(position == 2){
            return ViewType.GOOGLE_ADS
        }else{
            return ViewType.SSGSAG_ADS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SsgSagAdsViewHolder) {
            var tempPosition = 0
            if(position >= 3) tempPosition = position - 1
            else tempPosition = position
            holder.dataBinding.adPosterCollection = itemList[tempPosition]
            holder.dataBinding.itemAllPosterCollectionLlMore.setOnClickListener {
                listener?.onMoreClick(itemList[tempPosition].categoryIdx, tempPosition)
            }

            val cardViewPagerAdapter =
                AllPosterCardViewPagerAdapter(
                    context,
                    itemList[tempPosition].adViewItemList
                )
            cardViewPagerAdapter.apply {
                setOnItemClickListener(onPosterItemClickListener)
                rowIdx = tempPosition
                posterWidth = contentWidth
            }

            holder.dataBinding.itemAllPosterCollectionVp.run {
                clipToPadding = false
                setPadding(leftMargin, 0, rightMargin, 0)
                pageMargin = middleMargin
                adapter = cardViewPagerAdapter
            }
        }else if(holder is GoogleAdsViewHolder){

            MobileAds.initialize(context, context.resources.getString(R.string.admob_app_id))
            val adRequest = AdRequest.Builder().build()
            holder.dataBinding.itemGooglAdsAv.loadAd(adRequest)
        }

    }

    val onPosterItemClickListener
            = object : AllPosterCardViewPagerAdapter.OnItemClickListener{
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

    interface OnAllPosterCollectionClickListener{
        fun onMoreClick(categoryIdx: Int , rowIdx : Int)
        fun onPosterItemClick(posterIdx: Int, rowIdx : Int, position: Int)
        fun onManagePosterItem(posterIdx : Int, isSave : Int)
    }
}

class SsgSagAdsViewHolder(val dataBinding: ItemAllPosterCollectionBinding) : RecyclerView.ViewHolder(dataBinding.root)
class GoogleAdsViewHolder(val dataBinding: ItemGoogleAdsBinding) : RecyclerView.ViewHolder(dataBinding.root)

object ViewType {
    const val SSGSAG_ADS = 0
    const val GOOGLE_ADS = 1
}
