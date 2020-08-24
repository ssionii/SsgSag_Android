package com.icoo.ssgsag_android.data.model.poster.allPoster

import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail

data class AllPosterResponse(
    val status: Int,
    val message: String,
    var data: ArrayList<PosterDetail>
)