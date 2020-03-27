package com.icoo.ssgsag_android.ui.main.myPage.career

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.icoo.ssgsag_android.data.model.career.Career
import com.icoo.ssgsag_android.util.listener.IItemClickListener
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class CareerRecyclerViewAdapter(val ctx: Context, val list: ArrayList<Career>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<CareerRecyclerViewAdapter.Holder>() {

    private var listener: IItemClickListener? = null

    fun setOnItemClickListener(listener: IItemClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.rv_career_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = list[position].careerName
        if(list[position].careerDate2 != "")
            holder.time.text = list[position].careerDate1 + "~" + list[position].careerDate2
        else
            holder.time.text = list[position].careerDate1
        holder.content.text = list[position].careerContent

        holder.view.setSafeOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(list[position], list[position].careerType, list[position].careerIdx)
            }
        }
        holder.delete.setSafeOnClickListener {
            if (listener != null) {
                listener!!.onItemDelete(list[position].careerIdx)
            }
        }

    }
    inner class Holder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        val view: androidx.cardview.widget.CardView = itemView.findViewById(R.id.rv_career_cv) as androidx.cardview.widget.CardView

        val title : TextView = itemView.findViewById(R.id.rv_career_tv_career_card_title) as TextView
        val time : TextView = itemView.findViewById(R.id.rv_career_tv_career_card_time) as TextView
        val content : TextView = itemView.findViewById(R.id.rv_career_tv_career_card_content) as TextView

        val delete : ImageView = itemView.findViewById(R.id.rv_career_iv_delete) as ImageView
    }
}