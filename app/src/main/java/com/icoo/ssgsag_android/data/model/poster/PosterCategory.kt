package com.icoo.ssgsag_android.data.model.poster

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

data class PosterCategory(
    val categoryIdx : Int,
    val categoryName: String,
    val categoryBgColor : Int,
    val categoryTextColor : Int,
    val categoryImage : Drawable
)