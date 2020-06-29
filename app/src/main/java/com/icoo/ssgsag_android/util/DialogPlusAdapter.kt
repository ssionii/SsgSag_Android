package com.icoo.ssgsag_android.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.color.MaterialColors.getColor
import com.icoo.ssgsag_android.R
import org.jetbrains.anko.textColor

class DialogPlusAdapter(
    private val context: Context,
    private val isGrid: Boolean,
    private val count: Int?,
    private val selected: Int?,
    private val category: Int?,
    val flag : Boolean = true
) : BaseAdapter() {

    private var selectedField = 0
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        if(count != null)
            return count
        else
            return 0
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var view: View? = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_dialog_plus_grid, parent, false)
            viewHolder = ViewHolder(
                view!!.findViewById(R.id.text_view),
                view!!.findViewById(R.id.image_view)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        when(category) {
            // 공모전
            0 -> {
                when (position) {
                    0 -> {
                        viewHolder.textView.text = "전체"
                    }
                    1 -> {
                        viewHolder.textView.text = "기획/아이디어"
                    }
                    2 -> {
                        viewHolder.textView.text = "광고/마케팅"
                    }
                    3 -> {
                        viewHolder.textView.text = "영상/콘텐츠"
                    }
                    4 -> {
                        viewHolder.textView.text = "디자인"
                    }
                    5 -> {
                        viewHolder.textView.text = "IT/SW"
                    }
                    6 -> {
                        viewHolder.textView.text = "문학/시나리오"
                    }
                    7 -> {
                        viewHolder.textView.text = "창업/스타트업"
                    }
                    8 -> {
                        viewHolder.textView.text = "금융/경제"
                    }
                    9 -> {
                        viewHolder.textView.text = "기타"
                    }
                }
            }

            // 대외활동
            1 -> {
                when (position) {
                    0 -> {
                        viewHolder.textView.text = "전체"
                    }
                    1-> {
                        viewHolder.textView.text = "대기업 서포터즈"
                    }
                    2 -> {
                        viewHolder.textView.text = "공사/공기업 서포터즈"
                    }
                    3 -> {
                        viewHolder.textView.text = "봉사활동"
                    }
                    4 -> {
                        viewHolder.textView.text = "리뷰/체험단"
                    }
                    5 -> {
                        viewHolder.textView.text = "해외봉사/탐방"
                    }
                    6 -> {
                        viewHolder.textView.text = "기타"
                    }
                }
            }

            // 동아리
            2,6 -> {
                when(position){
                    0 -> {
                        viewHolder.textView.text = "전체"
                    }
                    1 -> {
                        viewHolder.textView.text = "문화생활"
                    }
                    2 -> {
                        viewHolder.textView.text = "스포츠"
                    }
                    3 -> {
                        viewHolder.textView.text = "여행"
                    }
                    4 -> {
                        viewHolder.textView.text = "음악/예술"
                    }
                    5 -> {
                        viewHolder.textView.text = "봉사"
                    }
                    6 -> {
                        viewHolder.textView.text = "스터디/학회"
                    }
                    7 -> {
                        viewHolder.textView.text = "어학"
                    }
                    8 -> {
                        viewHolder.textView.text = "창업"
                    }
                    9 -> {
                        viewHolder.textView.text = "친목"
                    }
                    10 -> {
                        viewHolder.textView.text = "IT/공학"
                    }
                    11 ->{
                        viewHolder.textView.text = "기타"
                    }
                }
            }

            // 인턴
            4 -> {
                if(flag) {
                    // 기업형태
                    when (position) {
                        0 -> {
                            viewHolder.textView.text = "전체"
                        }
                        1 -> {
                            viewHolder.textView.text = "대기업"
                        }
                        2 -> {
                            viewHolder.textView.text = "공사/공기업"
                        }
                        3 -> {
                            viewHolder.textView.text = "중견기업"
                        }
                        4 -> {
                            viewHolder.textView.text = "외국계 기업"
                        }
                        5 -> {
                            viewHolder.textView.text = "스타트업"
                        }
                        6 -> {
                            viewHolder.textView.text = "중소기업"
                        }
                        7 -> {
                            viewHolder.textView.text = "기타"
                        }
                    }
                }else{
                    // 관심 직무
                    when (position) {
                        0 -> {
                            viewHolder.textView.text = "전체"
                        }
                        1 -> {
                            viewHolder.textView.text = "광고/마케팅"
                        }
                        2 -> {
                            viewHolder.textView.text = "엔지니어링/설계"
                        }
                        3 -> {
                            viewHolder.textView.text = "제조/생산"
                        }
                        4 -> {
                            viewHolder.textView.text = "디자인"
                        }
                        5 -> {
                            viewHolder.textView.text = "경영/비즈니스"
                        }
                        6 -> {
                            viewHolder.textView.text = "개발"
                        }
                        7 -> {
                            viewHolder.textView.text = "미디어"
                        }
                        8 -> {
                            viewHolder.textView.text = "영업"
                        }
                        9 -> {
                            viewHolder.textView.text = "인사/교육"
                        }
                        10 -> {
                            viewHolder.textView.text = "고객서비스/리테일"
                        }
                        11 -> {
                            viewHolder.textView.text = "기타"
                        }
                    }
                }
            }
        }

        if(position == selected)
            viewHolder.textView.setTextColor(context.resources.getColor(R.color.selectedTabColor))

        return view
    }

    fun setSelectedField(select: Int){
        selectedField = select
    }

    data class ViewHolder(val textView: TextView, val imageView: ImageView)


}