package com.icoo.ssgsag_android.ui.main.review.club.info

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.databinding.ItemSquarePhotoBinding
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import java.io.File


class ClubInfoPhotoRecyclerViewAdapter(
    var itemList: MutableList<String>?
) : RecyclerView.Adapter<ClubInfoPhotoRecyclerViewAdapter.ViewHolder>(){

    private var listener: OnReviewPhotoClickListener? = null
    private var size = 0
    var isRgstr = false

    fun setOnReviewPhotoClickListener(listener: OnReviewPhotoClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: MutableList<String>?) {
        itemList?.clear()

        if (array != null) {
            for (i in array.indices) {
                this.itemList?.add(array[i])
            }
        }
        notifyDataSetChanged()
    }


    fun setPhotoSize(displayWidth: Int){

        val paddingDp = 24
        val middleDp = 8
        val d = context.resources.displayMetrics.density
        val horizontalPadding = (paddingDp * d).toInt()
        val middleMargin = (middleDp * d).toInt()

        size = (displayWidth - (horizontalPadding*2) - (middleMargin*2)) / 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemSquarePhotoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_square_photo, parent, false
        )

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() : Int {
        if(itemList == null)
            return 0
        else if(itemList!!.size < 7 && !isRgstr){
            return itemList!!.size
        }else if(!isRgstr){
            return 6
        }else if(itemList!!.size < 10 && isRgstr){
            return itemList!!.size
        }else{
            return 9
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(isRgstr)
            holder.dataBinding.itemSquarePhotoIvRemove.visibility = VISIBLE
        else
            holder.dataBinding.itemSquarePhotoIvRemove.visibility = INVISIBLE

        if(itemList != null) {


            holder.dataBinding.photo = itemList!![position]

            if(itemList!!.size >= 7 && position == 5 && !isRgstr){
                holder.dataBinding.itemSquarePhotoIv.setColorFilter(Color.parseColor("#80000000"))

                holder.dataBinding.itemSquarePhotoTv.visibility = VISIBLE
                holder.dataBinding.itemSquarePhotoTv.text = "+" + (itemList!!.size - 5).toString() + "장"
            }

            holder.dataBinding.root.layoutParams.height = size
            holder.dataBinding.root.requestLayout()

            holder.dataBinding.itemSquarePhotoIv.layoutParams.width = size  // 아이템 뷰의 가로 길이를 구한 길이로 변경
            holder.dataBinding.itemSquarePhotoIv.layoutParams.height = size  // 아이템 뷰의 세로 길이를 구한 길이로 변경
            holder.dataBinding.itemSquarePhotoIv.scaleType = ImageView.ScaleType.CENTER_CROP

            holder.dataBinding.itemSquarePhotoIv.requestLayout()

            holder.dataBinding.root.setSafeOnClickListener {
                listener?.onItemClickListener(itemList!![position].toUri(), position)
            }

            holder.dataBinding.itemSquarePhotoIvRemove.setSafeOnClickListener {
                listener?.onItemRemoveClickListener(itemList!![position])
            }

        }

    }

    override fun getItemId(position: Int): Long {
        if(itemList != null) {
            return itemList!![position].toString().toLong()
        }else
            return 0
    }

    inner class ViewHolder(val dataBinding: ItemSquarePhotoBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    interface OnReviewPhotoClickListener {
        fun onItemClickListener(url: Uri, position: Int)
        fun onItemRemoveClickListener(url: String)
    }

}