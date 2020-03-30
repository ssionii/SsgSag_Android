package com.icoo.ssgsag_android.ui.main.review.club.write.pages

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewWriteStartBinding
import com.icoo.ssgsag_android.ui.main.review.club.registration.NonRgstrClubActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import org.jetbrains.anko.support.v4.toast


class ReviewWriteStartFragment :  BaseFragment<FragmentReviewWriteStartBinding, ReviewWriteViewModel>() {
    override val layoutResID: Int
        get() = R.layout.fragment_review_write_start
    override val viewModel: ReviewWriteViewModel by viewModel()

    val position = 1

    private val univList = ArrayList<String>()
    private val actPlaceList = ArrayList<String>()
    private val clubNameList = ArrayList<String>()
    private val yearList = ArrayList<String>()
    private val monthList = ArrayList<String>()

    private var isDone = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        viewModel.clubType.observe(this, Observer { value ->
            if (value == 1) {
                viewDataBinding.fragWriteReviewStartLlUniv.visibility = View.VISIBLE
                viewDataBinding.fragWriteReviewStartLlActPlace.visibility = View.GONE
            } else {
                viewDataBinding.fragWriteReviewStartLlUniv.visibility = View.GONE
                viewDataBinding.fragWriteReviewStartLlActPlace.visibility = View.VISIBLE
            }
        })

        viewModel.isNotRgstr.observe(this, Observer { value ->
            if (value) {
                (activity as ReviewWriteActivity).toNextPage(position)
                viewModel.setIsNotRgstr(false)
            }
        })


        setButton()
        getUnivList()
        getClubList()
        setActPlaceSpinner()
        setActDateSpinner()
        setEditTextChange()

    }

    override fun onResume() {
        super.onResume()
        onDataCheck()

        (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragReviewWriteStartAtClubName)
    }

    private fun getClubList() {
        viewModel.clubSearchResult.observe(this, Observer { value ->
            clubNameList.clear()
            for (i in 0..value.size - 1) {
                clubNameList.add(value[i].clubName)
            }

            val clubNameAdapter = ArrayAdapter(
                activity!!, android.R.layout.simple_dropdown_item_1line, clubNameList
            )

            val onItemClickListener =
                object : AdapterView.OnItemClickListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        ReviewWriteActivity.ClubReviewWriteData.clubName = clubNameList[position]
                        viewModel.isRgstrClub = true
                        viewModel.clubIdx = value[position].clubIdx
                        onDataCheck()
                    }

                }

            viewDataBinding.fragReviewWriteStartAtClubName.run{
                setAdapter(clubNameAdapter)
                setOnItemClickListener(onItemClickListener)

                dropDownVerticalOffset = dipToPixels(40f).toInt()
                //setSelection(clubNameAdapter.count)
            }

        })

    }

    private fun AutoCompleteTextView.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
                viewModel.isRgstrClub = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(ReviewWriteActivity.ClubReviewWriteData.univOrLocation.isNotEmpty()
                    && this@onChange == viewDataBinding.fragReviewWriteStartAtClubName)
                    viewModel.searchClub(ReviewWriteActivity.ClubReviewWriteData.univOrLocation, s.toString())
                else if(this@onChange == viewDataBinding.fragReviewWriteStartAtUniv){
                    val text = s.toString()
                    isDone = false
                    if (checkUnivValidate(text))
                        isDone = true
                    Log.e("checkUnivValidate", isDone.toString())
                }
            }
        })
    }


    private fun getUnivList(){

        val univObj = JSONObject(loadJSONFromAsset("majorListByUniv.json"))
        val univJsonArr = univObj.getJSONArray("content")

        for (i in 0 until univJsonArr.length()) {
            val univJsonObj = univJsonArr.getJSONObject(i)
            val univName = univJsonObj.getString("학교명")

            univList.add(univName)
        }

        val schoolAdapter = ArrayAdapter(
            activity!!, android.R.layout.simple_dropdown_item_1line, univList)
        viewDataBinding.fragReviewWriteStartAtUniv.setAdapter(schoolAdapter)
    }

    fun checkUnivValidate(text: String): Boolean {
        for (i in univList.indices) {
            if (text == univList[i])
                return true
        }
        return false
    }

    private fun setActPlaceSpinner(){
        actPlaceList.add("전국")
        actPlaceList.add("서울")
        actPlaceList.add("경기")
        actPlaceList.add("인천")
        actPlaceList.add("부산")
        actPlaceList.add("대구")
        actPlaceList.add("광주")
        actPlaceList.add("대전")
        actPlaceList.add("울산")
        actPlaceList.add("세종")
        actPlaceList.add("강원")
        actPlaceList.add("경남")
        actPlaceList.add("경북")
        actPlaceList.add("전남")
        actPlaceList.add("전북")
        actPlaceList.add("충남")
        actPlaceList.add("충북")
        actPlaceList.add("제주")
        actPlaceList.add("활동 지역을 선택해주세요.")

        val placeAdapter = CustomAdapter()
        viewDataBinding.fragWriteReviewStartSpActPlace.run{
            adapter = placeAdapter.apply{
                addAll(actPlaceList)
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
                        if(position != actPlaceList.size -1) {
                            ReviewWriteActivity.ClubReviewWriteData.univOrLocation =
                                actPlaceList[position]

                            isDone = true
                        }
                        onDataCheck()

                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(placeAdapter.count)
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
        viewDataBinding.fragWriteReviewStartSpStartYear.run{
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
                        if(position != yearList.size -1) {
                            ReviewWriteActivity.ClubReviewWriteData.startYear =
                                yearList[position]

                            Log.e("start year 클릭", "!")
                        }
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(yearAdapter.count)
        }

        viewDataBinding.fragWriteReviewStartSpEndYear.run{
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
                            ReviewWriteActivity.ClubReviewWriteData.endYear = yearList[position]
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(yearAdapter.count)
        }


        val monthAdapter = CustomAdapter()
        viewDataBinding.fragWriteReviewStartSpStartMonth.run{
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
                            ReviewWriteActivity.ClubReviewWriteData.startMonth = monthList[position]
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(monthAdapter.count)
        }

        viewDataBinding.fragWriteReviewStartSpEndMonth.run{
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
                            ReviewWriteActivity.ClubReviewWriteData.endMonth = monthList[position]
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(monthAdapter.count)
        }
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


    private fun setEditTextChange() {
        viewDataBinding.fragReviewWriteStartAtClubName.onChange { onDataCheck() }
        viewDataBinding.fragReviewWriteStartAtUniv.onChange { onDataCheck() }
    }


    private fun loadJSONFromAsset(fileName: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = activity!!.resources.assets.open(fileName)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }

    private fun getEditText() {
        if(viewModel.clubType.value == 1){
            ReviewWriteActivity.ClubReviewWriteData.univOrLocation = viewDataBinding.fragReviewWriteStartAtUniv.text.toString()
        }
        ReviewWriteActivity.ClubReviewWriteData.clubName = viewDataBinding.fragReviewWriteStartAtClubName.text.toString()


    }

    private fun onDataCheck() {
        getEditText()

       if(ReviewWriteActivity.ClubReviewWriteData.univOrLocation.isEmpty()
            || ReviewWriteActivity.ClubReviewWriteData.clubName.isEmpty() || ReviewWriteActivity.ClubReviewWriteData.startYear.isEmpty() || ReviewWriteActivity.ClubReviewWriteData.startMonth.isEmpty()
            || ReviewWriteActivity.ClubReviewWriteData.endYear.isEmpty() || ReviewWriteActivity.ClubReviewWriteData.endMonth.isEmpty()
            || !isDone){

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

    private fun setButton() {
        viewDataBinding.fragWriteReviewStartClDone.setSafeOnClickListener {
            (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragReviewWriteStartAtClubName)

            ReviewWriteActivity.ClubReviewWriteData.clubName.run {

                if((ReviewWriteActivity.ClubReviewWriteData.startYear.substring(0,2).toInt() > ReviewWriteActivity.ClubReviewWriteData.endYear.substring(0,2).toInt())
                    || ((ReviewWriteActivity.ClubReviewWriteData.startYear.substring(0,2).toInt() == ReviewWriteActivity.ClubReviewWriteData.endYear.substring(0,2).toInt())
                            && (ReviewWriteActivity.ClubReviewWriteData.startMonth.substring(0,
                        ReviewWriteActivity.ClubReviewWriteData.startMonth.length-1).toInt() > ReviewWriteActivity.ClubReviewWriteData.endMonth.substring(0,
                        ReviewWriteActivity.ClubReviewWriteData.endMonth.length-1).toInt()))){
                    toast("활동시기 입력이 잘못되었습니다.")
                }else {
                    if (!viewModel.isRgstrClub) {
                        // 등록되어 있지 않는 동아리면 NonRgstrClubActivity 띄우기
                        val intent = Intent(activity, NonRgstrClubActivity::class.java)
                        intent.putExtra("clubName", this)
                        startActivity(intent)
                    } else {
                        (activity as ReviewWriteActivity).toNextPage(position)
                    }
                }
            }

        }

        viewDataBinding.fragWriteReviewStartIvBack.setSafeOnClickListener {
            (activity as ReviewWriteActivity).toPrevPage(position)
            (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragReviewWriteStartAtClubName)
        }
    }

}
