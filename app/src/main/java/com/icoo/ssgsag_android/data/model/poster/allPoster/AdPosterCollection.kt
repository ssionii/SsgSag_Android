package com.icoo.ssgsag_android.data.model.poster.allPoster

import com.icoo.ssgsag_android.data.model.ads.AdItem

data class AdPosterCollection(
    val categoryIdx : Int,
    val categoryName : String,
    val categoryOrder : Int,
    val categoryDetail : String,
    val adViewItemList : ArrayList<AdItem>
)