package com.icoo.ssgsag_android.ui.main.review.club.write.pages

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.model.review.ReviewWriteRelam
import com.icoo.ssgsag_android.databinding.FragmentReviewWriteStartWithNameBinding
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.realm.Realm
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ReviewWriteStartWithNameFragment : BaseFragment<FragmentReviewWriteStartWithNameBinding, ReviewWriteViewModel>(){

    override val layoutResID: Int
        get() = R.layout.fragment_review_write_start_with_name
    override val viewModel: ReviewWriteViewModel by viewModel()

    val realm = Realm.getDefaultInstance()
    lateinit var reviewWriteRealm : ReviewWriteRelam

    var position = -1

    private val yearList = ArrayList<String>()
    private val monthList = ArrayList<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        position = arguments!!.getInt("position", -1)

        setButton()
        setActDateSpinner()

        reviewWriteRealm = realm.where(ReviewWriteRelam::class.java).equalTo("id", 1 as Int).findFirst()!!

        viewDataBinding.fragReviewWriteStartWithNameTvTitle.text = "\'" + reviewWriteRealm!!.clubName + "\'에서\n활동한 시기를 알려주세요"

    }

    private fun setButton(){

        viewDataBinding.fragWriteReviewStartWithNameIvBack.setSafeOnClickListener {
            activity!!.finish()
        }
        viewDataBinding.fragWriteReviewStartClDone.setSafeOnClickListener {

            if((reviewWriteRealm.startYear.substring(0,2).toInt() > reviewWriteRealm.endYear.substring(0,2).toInt())
                || ((reviewWriteRealm.startYear.substring(0,2).toInt() == reviewWriteRealm.endYear.substring(0,2).toInt())
                        && (reviewWriteRealm.startMonth.substring(0,reviewWriteRealm.startMonth.length-1).toInt() > reviewWriteRealm.endMonth.substring(0,reviewWriteRealm.endMonth.length-1).toInt()))){
                toast("활동시기 입력이 잘못되었습니다.")
            }else{
                viewModel.isRgstrClub = true
                (activity as ReviewWriteActivity).toNextPage(position)
            }


        }
    }

    private fun setActDateSpinner(){

        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR).toString().substring(2, 4).toInt()

        for (i in year - 10..year) {
            yearList.add(i.toString() + "년")
        }

        yearList.add("20년")

        for(i in 1..12){
            monthList.add(i.toString() + "월")
        }

        monthList.add("1월")

        val yearAdapter = CustomAdapter()
        viewDataBinding.fragWriteReviewStartWithNameSpStartYear.run{
            adapter = yearAdapter.apply{
                addAll(yearList)
            }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != yearList.size -1)
                            (activity as ReviewWriteActivity).setReviewWriteStringRealm("startYear", yearList[position])
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(yearAdapter.count)
        }

        viewDataBinding.fragWriteReviewStartWithNameSpEndYear.run{
            adapter = yearAdapter
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != yearList.size -1)
                            (activity as ReviewWriteActivity).setReviewWriteStringRealm("endYear", yearList[position])
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(yearAdapter.count)
        }


        val monthAdapter = CustomAdapter()
        viewDataBinding.fragWriteReviewStartWithNameSpStartMonth.run{
            adapter = monthAdapter.apply{
                addAll(monthList)
            }
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != monthList.size -1)
                            (activity as ReviewWriteActivity).setReviewWriteStringRealm("startMonth", monthList[position])
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(monthAdapter.count)
        }

        viewDataBinding.fragWriteReviewStartWithNameSpEndMonth.run{
            adapter = monthAdapter
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != monthList.size -1)
                            (activity as ReviewWriteActivity).setReviewWriteStringRealm("endMonth", monthList[position])
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(monthAdapter.count)
        }
    }

    private fun onDataCheck() {

        if( reviewWriteRealm.startYear.isEmpty() || reviewWriteRealm.startMonth.isEmpty()
            || reviewWriteRealm.endYear.isEmpty() || reviewWriteRealm.endMonth.isEmpty()){
            viewDataBinding.fragWriteReviewStartClDone.apply{
                backgroundColor = activity!!.resources.getColor(R.color.grey_2)
                isClickable = false
            }
        }else{
            viewDataBinding.fragWriteReviewStartClDone.apply{
                backgroundColor = activity!!.resources.getColor(R.color.ssgsag)
                isClickable = true
            }
        }
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }

    inner class CustomAdapter : ArrayAdapter<String>(activity, R.layout.item_spinner) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent)
            if (position == count) {
                (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count)
            }
            return v
        }

        override fun getCount(): Int {
            return super.getCount() - 1
        }
    }

    companion object {
        fun newInstance(position: Int): ReviewWriteStartWithNameFragment {
            val fragment = ReviewWriteStartWithNameFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }


}