package com.icoo.ssgsag_android.ui.main.calendar.supported

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.icoo.ssgsag_android.data.model.date.Date
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.schedule.ScheduleResponse
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.listener.ISwipeLayoutItemClickListener
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import kotlinx.android.synthetic.main.activity_supported.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class SupportedActivity : AppCompatActivity(), ISwipeLayoutItemClickListener {
    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }
    private val completeTodoDataList : ArrayList<Schedule> by lazy {
        ArrayList<Schedule>()
    }
    private var completeRecyclerView : androidx.recyclerview.widget.RecyclerView? = null
    private var supportedRecyclerViewAdapter : SupportedRecyclerViewAdapter? = null
    private val parentObject = this
    var isDeleted: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supported)

        init()
        getCompleteTodoResponse()
    }

    private fun init() {
        //툴바
        setSupportActionBar(act_cal_complete_tb_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        //뷰
        completeRecyclerView = findViewById(R.id.act_cal_complete_rv) as androidx.recyclerview.widget.RecyclerView

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                if(isDeleted) {
                    setResult(1004)
                    isDeleted = false
                }
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if(isDeleted) {
            setResult(1004)
            isDeleted = false
        }
        finish()
    }

    override fun onItemClick() {
        isDeleted = true
        getCompleteTodoResponse()
    }

    override fun onItemClick(date: Date, isCalRefresh: Boolean) {

    }

    override fun onViewClick(posterIdx:Int) {
        val intent = Intent(this, CalendarDetailActivity::class.java)
        intent.putExtra("posterIdx", posterIdx)
        intent.putExtra("from","calendar")
        startActivity(intent)
    }

    fun getCompleteTodoResponse() {

        val getCompleteTodoResponse =
            networkService.getCompleteTodoResponse(SharedPreferenceController.getAuthorization(this))
        getCompleteTodoResponse.enqueue(object : Callback<ScheduleResponse> {
            override fun onFailure(call: Call<ScheduleResponse>, t : Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<ScheduleResponse>, response : Response<ScheduleResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.data.size != 0) {
                        act_cal_complete_ll_empty.visibility = View.GONE
                        act_cal_complete_rv.visibility = View.VISIBLE
                        completeTodoDataList.clear()
                        completeTodoDataList.addAll(response.body()!!.data)
                        supportedRecyclerViewAdapter =
                            SupportedRecyclerViewAdapter(
                                parentObject,
                                completeTodoDataList
                            )
                        completeRecyclerView!!.adapter = supportedRecyclerViewAdapter
                        completeRecyclerView!!.layoutManager =
                            androidx.recyclerview.widget.LinearLayoutManager(parentObject)
                        supportedRecyclerViewAdapter!!.setOnItemClickListener(parentObject)
                    }
                    else {
                        act_cal_complete_ll_empty.visibility = View.VISIBLE
                        act_cal_complete_rv.visibility = View.GONE
                    }

                }
            }
        })
    }
}
