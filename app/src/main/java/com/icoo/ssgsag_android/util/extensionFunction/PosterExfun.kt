package com.icoo.ssgsag_android.util.extensionFunction

import com.icoo.ssgsag_android.SsgSagApplication.Companion.ActTitle
import com.icoo.ssgsag_android.SsgSagApplication.Companion.ClubTitle
import com.icoo.ssgsag_android.SsgSagApplication.Companion.ContestTitle
import com.icoo.ssgsag_android.SsgSagApplication.Companion.RecruitTitle
import com.icoo.ssgsag_android.SsgSagApplication.Companion.eduTitle
import com.icoo.ssgsag_android.SsgSagApplication.Companion.scholarTitle
import com.icoo.ssgsag_android.data.model.item.ItemBase
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import org.json.JSONArray

fun getItemBase(poster: PosterDetail?): ArrayList<ItemBase> {
    val list: ArrayList<ItemBase> = ArrayList()
    when (poster?.categoryIdx) {
        0 -> dataInject(list, ContestTitle, poster, poster?.categoryIdx)
        1 -> dataInject(list, ActTitle, poster, poster?.categoryIdx)
        2, 6 -> dataInject(list, ClubTitle, poster, poster?.categoryIdx)
        4 -> dataInject(list, RecruitTitle, poster, poster?.categoryIdx)
        3, 5 -> dataInject(list, null, poster, poster?.categoryIdx)
        7 -> dataInject(list, eduTitle, poster, poster?.categoryIdx)
        8-> dataInject(list, scholarTitle, poster, poster?.categoryIdx)
    }
    return list
}


fun dataInject(titleContents: ArrayList<ItemBase>, titleByCate: ArrayList<String>?, poster: PosterDetail, categoryIdx: Int) {
    titleByCate?.let {
        when(categoryIdx) {
            0, 1, 4 -> {
                titleContents.add(ItemBase(it[0], poster.outline, "1"))
                titleContents.add(ItemBase(it[1], poster.target, "2"))
                titleContents.add(ItemBase(it[2], poster.benefit, "3"))
            }
            2, 6 -> {
                titleContents.add(ItemBase(it[0], poster.outline, "1"))
                titleContents.add(ItemBase(it[1], poster.period, "2"))
                titleContents.add(ItemBase(it[2], poster.benefit, "3"))
            }
            7 -> {
                titleContents.add(ItemBase(it[0], poster.outline, "1"))
                titleContents.add(ItemBase(it[1], poster.target, "2"))
                titleContents.add(ItemBase(it[2], poster.period, "3"))
            }
            8->{
                titleContents.add(ItemBase(it[0], poster.benefit, "1"))
                titleContents.add(ItemBase(it[1], poster.target, "2"))
                titleContents.add(ItemBase(it[2], poster.outline, "3"))
            }
            else->{
            }
        }

    } ?: run {
        val ja = JSONArray(poster.outline)
        for (i in 0 until ja.length()) {
            val order = ja.getJSONObject(i)
            titleContents.add(
                ItemBase(
                    order.getString("columnName"),
                    order.getString("columnContent"),
                    "${i + 1}"
                )
            )
        }
    }
}