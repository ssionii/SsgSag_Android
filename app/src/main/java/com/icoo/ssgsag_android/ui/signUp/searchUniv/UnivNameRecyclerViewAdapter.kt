package com.icoo.ssgsag_android.ui.signUp.searchUniv

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.signUp.UnivInfo
import com.icoo.ssgsag_android.data.model.signUp.University
import com.icoo.ssgsag_android.databinding.ItemUnivContainerBinding
import com.icoo.ssgsag_android.databinding.ItemUnivNameBinding
import com.icoo.ssgsag_android.ui.main.review.club.edit.ClubReviewEditDialogFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.listener.INoticeItemClickListener

class UnivNameRecyclerViewAdapter(
    var itemList : ArrayList<UnivInfo>
): RecyclerView.Adapter<UnivNameRecyclerViewAdapter.ViewHolder>() {

    lateinit var listener : OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<UnivInfo>?) {
        array?.let {
            this.itemList.run {
                clear()
                addAll(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemUnivNameBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_univ_name, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.univName = itemList[position].univName

        holder.dataBinding.root.setSafeOnClickListener {
            listener?.onItemClicked(itemList[position])
        }
    }

    inner class ViewHolder(val dataBinding: ItemUnivNameBinding) : RecyclerView.ViewHolder(dataBinding.root)

    interface OnItemClickListener {
        fun onItemClicked(univInfo: UnivInfo)
    }
}