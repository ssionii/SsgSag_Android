package com.icoo.ssgsag_android.data.model.poster

import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail

data class Poster(
    val posters : ArrayList<PosterDetail>,
    val userCnt : Int?
)

