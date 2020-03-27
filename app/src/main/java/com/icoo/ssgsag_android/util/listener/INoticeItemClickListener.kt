package com.icoo.ssgsag_android.util.listener

import com.icoo.ssgsag_android.data.model.notice.Notice

interface INoticeItemClickListener {
    fun onItemClick(notice: Notice)
}