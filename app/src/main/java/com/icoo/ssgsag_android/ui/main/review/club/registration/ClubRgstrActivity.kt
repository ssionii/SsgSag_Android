package com.icoo.ssgsag_android.ui.main.review.club.registration

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityClubRgstrBinding
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity.ClubRgstrData.categoryList
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity.ClubRgstrData.defaultUnivList
import com.icoo.ssgsag_android.ui.main.review.club.registration.pages.ClubSimpleInfoFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL

class ClubRgstrActivity : BaseActivity<ActivityClubRgstrBinding, ClubRgstrViewModel>(){
    object ClubRgstrData {

        lateinit var defaultUnivList : String

        // 간단 정보
        var univOrLocation =""
        lateinit var clubName: String
        lateinit var oneLine: String
        var categoryList = mutableListOf<String>()

        // 상세 정보
        lateinit var activeNum: String
        lateinit var meetingTime: String
        lateinit var clubFee: String
        var clubWebsite = ""
        lateinit var introduce: String

        // 연락
        var adminEmail: String = ""
        var adminCall: String = ""

        var adminName = ""
    }

    override val layoutResID: Int
        get() = R.layout.activity_club_rgstr
    override val viewModel: ClubRgstrViewModel by viewModel()

    lateinit var clubRgstrPagerAdapter: ClubRgstrPagerAdapter
    lateinit var thread : Thread

    private var threadCondition = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getIntExtra("clubIdx", -1).run{
            if(this != -1)
                viewModel.clubIdx = this
        }

        viewDataBinding.vm = viewModel
        //getJsonList()
        setVp()
    }

    // 학교 리스트 가져오기
    fun getJsonList() {
        thread = Thread(Runnable {
            while(threadCondition) {
                try {
                    val inputStream =
                        URL("http://ssgsag-alb-2141317761.ap-northeast-2.elb.amazonaws.com/validUnivList").openStream()
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

                    var str: String? = ""
                    val buffer = StringBuffer()

                    str = bufferedReader.readLine()
                    while (str != null) {
                        buffer.append(str)
                        str = bufferedReader.readLine()
                    }

                    defaultUnivList = buffer.toString()


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

        thread.start()

    }


    private fun setVp(){
        clubRgstrPagerAdapter = ClubRgstrPagerAdapter(supportFragmentManager, 4)
        viewDataBinding.actClubRgstrVp.adapter = clubRgstrPagerAdapter
    }

    fun toNextPage(curPage :Int){
        if(curPage < clubRgstrPagerAdapter.fragmentCount - 1) {
            viewDataBinding.actClubRgstrVp.setCurrentItem(curPage + 1)
        }
    }

    fun toPrevPage(curPage: Int){
        if(curPage > 0) {
            viewDataBinding.actClubRgstrVp.setCurrentItem(curPage - 1)
        }
    }

    fun hideKeyboard(et: EditText){

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(et.getWindowToken(), 0)

    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.clearPhotos()
        categoryList.clear()
        defaultUnivList = ""
        threadCondition = false
    }
}