package com.icoo.ssgsag_android.ui.main.calendar.supported

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.util.listener.ISwipeLayoutItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupportedRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<Schedule>) : androidx.recyclerview.widget.RecyclerView.Adapter<SupportedRecyclerViewAdapter.Holder>() {
    private val api: NetworkService = NetworkService.create()
    var view:View ? = null

    private var listener: ISwipeLayoutItemClickListener? = null

    fun setOnItemClickListener(listener: ISwipeLayoutItemClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        view = LayoutInflater.from(ctx).inflate(R.layout.rv_todo_item, parent, false)

        return Holder(view!!)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.title.text = dataList[position].posterName
        holder.term.text = dataList[position].documentDate

        if (dataList[position].isEnded == 1 && dataList[position].isCompleted == 0) {
            holder.category.setTextColor(ctx.resources.getColor(R.color.categoryEnd))
            holder.title.setTextColor(ctx.resources.getColor(R.color.categoryEnd))
            holder.dday.visibility = View.GONE
            holder.image.setImageResource(R.drawable.ic_circle_time_passed)
        } else {
            when (dataList[position].categoryIdx) {
                0 -> {
                    holder.category.setTextColor(ctx.resources.getColor(R.color.categoryContest))
                    holder.category.text = "공모전"
                    if (dataList[position].isCompleted == 1) {
                        holder.dday.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.ic_circle_complete)
                        holder.complete.visibility = View.GONE
                    } else {
                        holder.complete.visibility = View.VISIBLE
                        if(dataList[position].dday == 0) {
                            if (dataList[position].isFavorite == 0)
                                holder.image.setImageResource(R.drawable.bt_dday_contest_passive)
                            else
                                holder.image.setImageResource(R.drawable.bt_dday_contest_active)
                            holder.dday.visibility = View.GONE
                        } else {
                            if (dataList[position].isFavorite == 0) {
                                holder.image.setImageResource(R.drawable.bt_favorite_contest_passive)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.categoryContest))
                            } else{
                                holder.image.setImageResource(R.drawable.bt_favorite_contest_active)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.white))
                            }
                            holder.dday.visibility = View.VISIBLE
                            holder.dday.text = dataList[position].dday.toString() + "일"
                        }
                    }
                }
                1 -> {
                    holder.category.setTextColor(ctx.resources.getColor(R.color.categoryAct))
                    holder.category.text = "대외활동"
                    if (dataList[position].isCompleted == 1) {
                        holder.dday.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.ic_circle_complete)
                        holder.complete.visibility = View.GONE
                    } else {
                        holder.complete.visibility = View.VISIBLE
                        if(dataList[position].dday == 0) {
                            if (dataList[position].isFavorite == 0)
                                holder.image.setImageResource(R.drawable.bt_dday_act_passive)
                            else
                                holder.image.setImageResource(R.drawable.bt_dday_act_active)
                            holder.dday.visibility = View.GONE
                        } else {
                            if (dataList[position].isFavorite == 0) {
                                holder.image.setImageResource(R.drawable.bt_favorite_act_passive)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.categoryAct))
                            } else{
                                holder.image.setImageResource(R.drawable.bt_favorite_act_active)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.white))
                            }
                            holder.dday.visibility = View.VISIBLE
                            holder.dday.text = dataList[position].dday.toString() + "일"
                        }
                    }
                }
                2 -> {
                    holder.category.setTextColor(ctx.resources.getColor(R.color.categoryClub))
                    holder.category.text = "동아리(연합)"
                    if (dataList[position].isCompleted == 1) {
                        holder.dday.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.ic_circle_complete)
                        holder.complete.visibility = View.GONE
                    } else {
                        holder.complete.visibility = View.VISIBLE
                        if(dataList[position].dday == 0) {
                            if (dataList[position].isFavorite == 0)
                                holder.image.setImageResource(R.drawable.bt_dday_club_passive)
                            else
                                holder.image.setImageResource(R.drawable.bt_dday_club_active)
                            holder.dday.visibility = View.GONE
                        } else {
                            if (dataList[position].isFavorite == 0) {
                                holder.image.setImageResource(R.drawable.bt_favorite_club_passive)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.categoryClub))
                            } else{
                                holder.image.setImageResource(R.drawable.bt_favorite_club_active)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.white))
                            }
                            holder.dday.visibility = View.VISIBLE
                            holder.dday.text = dataList[position].dday.toString() + "일"
                        }
                    }
                }
                3 -> {
                    holder.category.setTextColor(ctx.resources.getColor(R.color.categoryNotice))
                    holder.category.text = "교내공지"
                    if (dataList[position].isCompleted == 1) {
                        holder.dday.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.ic_circle_complete)
                        holder.complete.visibility = View.GONE
                    } else {
                        holder.complete.visibility = View.VISIBLE
                        if(dataList[position].dday == 0) {
                            if (dataList[position].isFavorite == 0)
                                holder.image.setImageResource(R.drawable.bt_dday_notice_passive)
                            else
                                holder.image.setImageResource(R.drawable.bt_dday_notice_active)
                            holder.dday.visibility = View.GONE
                        } else {
                            if (dataList[position].isFavorite == 0) {
                                holder.image.setImageResource(R.drawable.bt_favorite_notice_passive)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.categoryNotice))
                            } else{
                                holder.image.setImageResource(R.drawable.bt_favorite_notice_active)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.white))
                            }
                            holder.dday.visibility = View.VISIBLE
                            holder.dday.text = dataList[position].dday.toString() + "일"
                        }
                    }
                }
                4 -> {
                    holder.category.setTextColor(ctx.resources.getColor(R.color.categoryRecruit))
                    holder.category.text = "채용"
                    if (dataList[position].isCompleted == 1) {
                        holder.dday.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.ic_circle_complete)
                        holder.complete.visibility = View.GONE
                    } else {
                        holder.complete.visibility = View.VISIBLE
                        if(dataList[position].dday == 0) {
                            if (dataList[position].isFavorite == 0)
                                holder.image.setImageResource(R.drawable.bt_dday_recruit_passive)
                            else
                                holder.image.setImageResource(R.drawable.bt_dday_recruit_active)
                            holder.dday.visibility = View.GONE
                        } else {
                            if (dataList[position].isFavorite == 0) {
                                holder.image.setImageResource(R.drawable.bt_favorite_recruit_passive)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.categoryRecruit))
                            } else{
                                holder.image.setImageResource(R.drawable.bt_favorite_recruit_active)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.white))
                            }
                            holder.dday.visibility = View.VISIBLE
                            holder.dday.text = dataList[position].dday.toString() + "일"
                        }
                    }
                }
                5 -> {
                    holder.category.setTextColor(ctx.resources.getColor(R.color.categoryEtc))
                    holder.category.text = "기타"
                    if (dataList[position].isCompleted == 1) {
                        holder.dday.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.ic_circle_complete)
                        holder.complete.visibility = View.GONE
                    } else {
                        holder.complete.visibility = View.VISIBLE
                        if(dataList[position].dday == 0) {
                            if (dataList[position].isFavorite == 0)
                                holder.image.setImageResource(R.drawable.bt_dday_etc_passive)
                            else
                                holder.image.setImageResource(R.drawable.bt_dday_etc_active)
                            holder.dday.visibility = View.GONE
                        } else {
                            if (dataList[position].isFavorite == 0) {
                                holder.image.setImageResource(R.drawable.bt_favorite_etc_passive)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.categoryEtc))
                            } else{
                                holder.image.setImageResource(R.drawable.bt_favorite_etc_active)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.white))
                            }
                            holder.dday.visibility = View.VISIBLE
                            holder.dday.text = dataList[position].dday.toString() + "일"
                        }
                    }
                }
                6 -> {
                    holder.category.setTextColor(ctx.resources.getColor(R.color.categoryClub))
                    holder.category.text = "동아리(교내)"
                    if (dataList[position].isCompleted == 1) {
                        holder.dday.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.ic_circle_complete)
                        holder.complete.visibility = View.GONE
                    } else {
                        holder.complete.visibility = View.VISIBLE
                        if(dataList[position].dday == 0) {
                            if (dataList[position].isFavorite == 0)
                                holder.image.setImageResource(R.drawable.bt_dday_club_passive)
                            else
                                holder.image.setImageResource(R.drawable.bt_dday_club_active)
                            holder.dday.visibility = View.GONE
                        } else {
                            if (dataList[position].isFavorite == 0) {
                                holder.image.setImageResource(R.drawable.bt_favorite_club_passive)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.categoryClub))
                            } else{
                                holder.image.setImageResource(R.drawable.bt_favorite_club_active)
                                holder.dday.setTextColor(ctx.resources.getColor(R.color.white))
                            }
                            holder.dday.visibility = View.VISIBLE
                            holder.dday.text = dataList[position].dday.toString() + "일"
                        }
                    }
                }
            }
        }
        holder.complete.setSafeOnClickListener {
            postCompleteTodoResponse(dataList[position].posterIdx)
        }
        holder.cancel.setSafeOnClickListener {
            deleteDeleteTodoResponse(dataList[position].posterIdx)
        }
        holder.view.setSafeOnClickListener {
            if (listener != null) {
                listener!!.onViewClick(dataList[position].posterIdx)
            }
        }
    }
    inner class Holder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        val view : RelativeLayout = itemView.findViewById(R.id.rv_todo_item_rl_visible_view) as RelativeLayout
        val category : TextView = itemView.findViewById(R.id.rv_todo_item_tv_category) as TextView
        val title : TextView = itemView.findViewById(R.id.rv_todo_item_tv_title) as TextView
        val term : TextView = itemView.findViewById(R.id.rv_todo_item_tv_term) as TextView
        val dday : TextView = itemView.findViewById(R.id.rv_todo_item_tv_dday) as TextView
        val image : ImageView = itemView.findViewById(R.id.rv_todo_item_iv_img) as ImageView
        val complete : TextView = itemView.findViewById(R.id.rv_todo_item_tv_complete) as TextView
        val cancel : TextView = itemView.findViewById(R.id.rv_todo_item_tv_cancel) as TextView
    }

    private fun postCompleteTodoResponse(posterIdx : Int) {

        val postCompleteTodoResponse =
            api.postCompleteTodoResponse(SharedPreferenceController.getAuthorization(ctx), posterIdx)
        postCompleteTodoResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t : Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response : Response<StringResponse>) {
                if (response.isSuccessful) {
                    if (listener != null) {
                        listener!!.onItemClick()
                    }
                }
            }
        })
    }

    private fun deleteDeleteTodoResponse(posterIdx : Int) {

        val deleteDeleteTodoResponse =
            api.deleteDeleteTodoResponse(SharedPreferenceController.getAuthorization(ctx), posterIdx)
        deleteDeleteTodoResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t : Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response : Response<StringResponse>) {
                if (response.isSuccessful) {
                    if (listener != null) {
                        listener!!.onItemClick()
                    }
                }
            }
        })
    }
}