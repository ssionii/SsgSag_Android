package com.icoo.ssgsag_android.ui.main.review.club.registration.pages

import android.graphics.Color
import com.icoo.ssgsag_android.databinding.FragmentClubSimpleInfoBinding
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.databinding.ItemClubCategoryBinding
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity.ClubRgstrData
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL
import java.util.ArrayList
import java.util.regex.Pattern

class ClubSimpleInfoFragment : BaseFragment<FragmentClubSimpleInfoBinding, ClubRgstrViewModel>(){
    override val layoutResID: Int
        get() = R.layout.fragment_club_simple_info
    override val viewModel: ClubRgstrViewModel by viewModel()

    val position = 1

    val univList = ArrayList<String>()
    private val actPlaceList = ArrayList<String>()
    private val clubCategoryList = ArrayList<String>()
    var isUnivSelected = false
    var isUnivAdapterSet = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        viewModel.clubType.observe(this, Observer { value ->
            if(value == 1){
                viewDataBinding.fragClubSimpleInfoLlUniv.visibility = VISIBLE
                viewDataBinding.fragClubSimpleInfoLlActPlace.visibility = GONE
            }else{
                viewDataBinding.fragClubSimpleInfoLlUniv.visibility = GONE
                viewDataBinding.fragClubSimpleInfoLlActPlace.visibility = VISIBLE
            }
        })

        setButton()
        getUnivList()
        setActPlaceSpinner()
        setClubCategorySpinner()
        setClubCategoryRv()
        setEditTextChange()

    }
//
//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if(isVisibleToUser) {
//            if(!isUnivAdapterSet){
//                setUnivList()
//                isUnivAdapterSet = true
//            }
//        }
//    }



    override fun onResume() {
        super.onResume()
        onDataCheck()

        (activity as ClubRgstrActivity).hideKeyboard(viewDataBinding.fragClubSimpleInfoEtIntroduce)
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
        viewDataBinding.fragClubSimpleInfoAtUniv.setAdapter(schoolAdapter)
    }

    fun setUnivList(){

        val univObj = JSONObject(ClubRgstrData.defaultUnivList)
        val univJsonArr = univObj.getJSONArray("data")

        for (i in 0 until univJsonArr.length()) {
            val univJsonObj = univJsonArr.getJSONObject(i)

            val univName = univJsonObj.getString("학교명")
            univList.add(univName)
        }
        val schoolAdapter = ArrayAdapter(
            activity!!, android.R.layout.simple_dropdown_item_1line, univList)
        viewDataBinding.fragClubSimpleInfoAtUniv.setAdapter(schoolAdapter)
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
        viewDataBinding.fragClubSimpleInfoSnActPlace.run{
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
                        if(position != actPlaceList.size -1)
                            ClubRgstrData.univOrLocation = actPlaceList[position]
                        onDataCheck()


                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(placeAdapter.count)
        }
    }

    private fun setClubCategorySpinner(){
        clubCategoryList.add("문화생활")
        clubCategoryList.add("스포츠")
        clubCategoryList.add("여행")
        clubCategoryList.add("음악/예술")
        clubCategoryList.add("봉사")
        clubCategoryList.add("스터디/학회")
        clubCategoryList.add("IT/공학")
        clubCategoryList.add("어학")
        clubCategoryList.add("창업")
        clubCategoryList.add("친목")
        clubCategoryList.add("기타")
        clubCategoryList.add("선택")

        val categoryAdapter = CustomAdapter()
        viewDataBinding.fragClubSimpleInfoSnCategory.run{
            adapter = categoryAdapter.apply{
                addAll(clubCategoryList)
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
                        (viewDataBinding.fragClubSimpleInfoRvCategory.adapter as BaseRecyclerViewAdapter<String, ItemClubCategoryBinding>).run{
                            if(position != clubCategoryList.size -1 && this.items.size < 3){
                                addItem(listOf(clubCategoryList[position]))
                                ClubRgstrData.categoryList = this.items
                            }else if(this.items.size == 3){
                                toast("카테고리 설정은 최대 3개까지 가능합니다")
                            }
                        }
                        onDataCheck()
                    }
                }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            setSelection(categoryAdapter.count)
        }
    }

    private fun setClubCategoryRv(){
        viewDataBinding.fragClubSimpleInfoRvCategory.run{
            adapter = object : BaseRecyclerViewAdapter<String, ItemClubCategoryBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_club_category
                override val bindingVariableId: Int
                    get() = BR.clubCategory
                override val listener: OnItemClickListener?
                    get() = clubCategoryItemClickListener
            }
        }
    }

    private fun setButton(){
        viewDataBinding.fragClubSimpleInfoClubNext.setSafeOnClickListener {
            (activity as ClubRgstrActivity).toNextPage(position)
        }

        viewDataBinding.actClubManagerContactIvBack.setSafeOnClickListener {
            (activity as ClubRgstrActivity).toPrevPage(position)
            (activity as ClubRgstrActivity).hideKeyboard(viewDataBinding.fragClubSimpleInfoEtIntroduce)
        }
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

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if((this@onChange == viewDataBinding.fragClubSimpleInfoEtIntroduce || this@onChange == viewDataBinding.fragClubSimpleInfoClubName)&& s!!.length > 20){
                    this@onChange.setText(s.substring(0, 20))
                    toast( "20자 이내로 입력해주세요.")
                }
            }
        })
    }

    private fun setEditTextChange() {
        viewDataBinding.fragClubSimpleInfoAtUniv.onChange { onDataCheck() }
        viewDataBinding.fragClubSimpleInfoClubName.onChange { onDataCheck() }
        viewDataBinding.fragClubSimpleInfoEtIntroduce.onChange { onDataCheck() }
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


    private val clubCategoryItemClickListener
            = object : BaseRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClicked(item: Any?, position: Int?) {
            (viewDataBinding.fragClubSimpleInfoRvCategory.adapter as BaseRecyclerViewAdapter<String, ItemClubCategoryBinding>).run {
                removeItem(position!!)
                notifyDataSetChanged()

                onDataCheck()
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


    private fun getEditText() {
        if(viewModel.clubType.value == 1)
            ClubRgstrData.univOrLocation = viewDataBinding.fragClubSimpleInfoAtUniv.text.toString()

        ClubRgstrData.clubName = viewDataBinding.fragClubSimpleInfoClubName.text.toString()
        ClubRgstrData.oneLine = viewDataBinding.fragClubSimpleInfoEtIntroduce.text.toString()
    }

    private fun onDataCheck() {
        getEditText()

        if(ClubRgstrData.univOrLocation.isEmpty() || ClubRgstrData.clubName.isEmpty() || ClubRgstrData.oneLine.isEmpty() || ClubRgstrData.categoryList.size == 0) {
            viewDataBinding.fragClubSimpleInfoClubNext.apply {
                backgroundColor = resources.getColor(R.color.grey_2)
                isClickable = false
            }
        }else{
            viewDataBinding.fragClubSimpleInfoClubNext.apply{
                backgroundColor = resources.getColor(R.color.ssgsag)
                isClickable = true
            }
        }
    }
}