package com.icoo.ssgsag_android.ui.main.ssgSag.todaySwipePoster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.databinding.ActivityTodaySwipePosterBinding
import com.icoo.ssgsag_android.databinding.ItemAllPostersBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodaySwipePosterActivity
    : BaseActivity<ActivityTodaySwipePosterBinding, TodaySwipePosterViewModel>(), BaseRecyclerViewAdapter.OnItemClickListener{

    override val layoutResID: Int
        get() = R.layout.activity_today_swipe_poster

    override val viewModel: TodaySwipePosterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        setButton()
        setRv()
        refreshRv()
        navigator()

    }

    private fun setButton(){
        viewDataBinding.actTodaySwipePosterIvBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actTodaySwipePosterIvNo.setOnClickListener {
            if(!it.isSelected){
                it.isSelected = true

                viewDataBinding.actTodaySwipePosterIvYes.isSelected = false

            }
        }

        viewDataBinding.actTodaySwipePosterIvYes.setOnClickListener {
            if(!it.isSelected){
                it.isSelected = true

                viewDataBinding.actTodaySwipePosterIvNo.isSelected = false
            }
        }

        viewDataBinding.actTodaySwipePosterRlGoCalendar.setSafeOnClickListener {
            finish()
            (MainActivity.mainActivityContext.mainContext as MainActivity).moveFragment(2)

        }
    }

    private fun setRv(){
        viewDataBinding.actTodaySwipePosterRvSsg.apply{
            adapter = object : BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_all_posters
                override val bindingVariableId: Int
                    get() = BR.posterDetail
                override val listener: OnItemClickListener?
                    get() = this@TodaySwipePosterActivity
            }

            layoutManager = WrapContentLinearLayoutManager()
        }

        viewDataBinding.actTodaySwipePosterRvSag.apply{
            adapter = object : BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_all_posters
                override val bindingVariableId: Int
                    get() = BR.posterDetail
                override val listener: OnItemClickListener?
                    get() = this@TodaySwipePosterActivity
            }

            layoutManager = WrapContentLinearLayoutManager()
        }
    }

    override fun onItemClicked(item: Any?, position: Int?) {
        viewModel.navigate((item as PosterDetail).posterIdx)
    }

    private fun refreshRv(){
        viewModel.ssgPosters.observe(this, Observer { value ->
            (viewDataBinding.actTodaySwipePosterRvSsg.adapter as BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>).apply {
                replaceAll(value)
                notifyDataSetChanged()
            }

            if(value.size > 0)
                viewDataBinding.actTodaySwipePosterLlSsgContainer.visibility = VISIBLE
            else
                viewDataBinding.actTodaySwipePosterLlSsgContainer.visibility = GONE
        })

        viewModel.sagPosters.observe(this, Observer { value ->
            (viewDataBinding.actTodaySwipePosterRvSag.adapter as BaseRecyclerViewAdapter<PosterDetail, ItemAllPostersBinding>).apply {
                replaceAll(value)
                notifyDataSetChanged()
            }

            if(value.size > 0)
                viewDataBinding.actTodaySwipePosterLlSagContainer.visibility = VISIBLE
            else
                viewDataBinding.actTodaySwipePosterLlSagContainer.visibility = GONE
        })
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(this, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            startActivity(intent)
        })
    }
}