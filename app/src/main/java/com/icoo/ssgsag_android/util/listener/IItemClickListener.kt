package com.icoo.ssgsag_android.util.listener

import com.icoo.ssgsag_android.data.model.career.Career

interface IItemClickListener {
    fun onDataListCheck()
    fun onItemClick(data: Career, section: Int, idx: Int)
    fun onItemDelete(idx: Int)
}