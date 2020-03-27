package com.icoo.ssgsag_android.data.model.poster

import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail

data class TodaySsgSag(
    val ssgSummaryPosterList : ArrayList<PosterDetail>,
    val sagSummaryPosterList : ArrayList<PosterDetail>
)