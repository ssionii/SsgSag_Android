package com.icoo.ssgsag_android.ui.signUp.searchUniv

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.model.signUp.UnivInfo
import com.icoo.ssgsag_android.data.model.signUp.University
import com.icoo.ssgsag_android.databinding.ItemAllPostersBinding
import com.icoo.ssgsag_android.databinding.ItemUnivContainerBinding
import com.icoo.ssgsag_android.databinding.ItemUnivNameBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollLinearLayoutManager
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager

class UnivContainerRecyclerViewAdapter : RecyclerView.Adapter<UnivContainerRecyclerViewAdapter.ViewHolder>(), Filterable {

    // 전체 data 보관
    var itemList= arrayListOf<University>()
    // 검색에 따른 data
    var filteredItemList = arrayListOf<University>()

    fun replaceAll(array: ArrayList<University>?) {
        array?.let {
            this.filteredItemList.run {
                clear()
                addAll(it)
            }

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

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if(charString.isEmpty()){
                    filteredItemList = itemList
                }else {
                    val filteringItemList = arrayListOf<University>()
                    for(item in itemList){
                        for(univInfo in item.univInfoList){
                            if(univInfo.univName.contains(charString)){
                                var isContain = false
                                for(i in filteringItemList.indices){
                                    // filteringItem에 이미 head가 있는 경우
                                    if(filteringItemList[i].head == item.head){
                                        isContain = true
                                        filteringItemList[i].univInfoList.add(univInfo)
                                    }
                                }

                                // filteringItem에 head가 없는 경우
                                if(!isContain) {
                                    filteringItemList.add(University(item.head, arrayListOf(univInfo)))
                                }
                            }
                        }
                    }

                    filteredItemList = filteringItemList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredItemList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItemList = results?.values as ArrayList<University>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = filteredItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.univ = filteredItemList[position]

        holder.dataBinding.itemUnivContainerRv.run{
            adapter = UnivNameRecyclerViewAdapter(filteredItemList[position].univInfoList)
            layoutManager = NonScrollLinearLayoutManager(context)
        }
    }

    inner class ViewHolder(val dataBinding: ItemUnivContainerBinding) : RecyclerView.ViewHolder(dataBinding.root)



}