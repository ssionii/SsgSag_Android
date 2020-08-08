package com.icoo.ssgsag_android.ui.signUp.searchUniv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.model.signUp.University
import com.icoo.ssgsag_android.databinding.ItemAllPostersBinding
import com.icoo.ssgsag_android.databinding.ItemUnivContainerBinding
import com.icoo.ssgsag_android.databinding.ItemUnivNameBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollLinearLayoutManager
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager

class UnivContainerRecyclerViewAdapter(

) : RecyclerView.Adapter<UnivContainerRecyclerViewAdapter.ViewHolder>() {

    var itemList= arrayListOf<University>()

    fun replaceAll(array: ArrayList<University>?) {
        array?.let {
            this.itemList.run {
                clear()
                addAll(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ItemUnivContainerBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_univ_container, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.univ = itemList[position]

        holder.dataBinding.itemUnivContainerRv.run{
            adapter = UnivNameRecyclerViewAdapter(itemList[position].univInfoList)
            layoutManager = NonScrollLinearLayoutManager(context)
        }
    }

    inner class ViewHolder(val dataBinding: ItemUnivContainerBinding) : RecyclerView.ViewHolder(dataBinding.root)

}