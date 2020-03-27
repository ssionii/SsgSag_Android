package com.icoo.ssgsag_android.ui.main.myPage.notice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import com.icoo.ssgsag_android.ui.main.myPage.notice.noticeHolder.NoticeHolderActivity
import com.icoo.ssgsag_android.data.model.notice.Notice
import com.icoo.ssgsag_android.data.model.notice.NoticeResponse
import com.icoo.ssgsag_android.util.listener.INoticeItemClickListener
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.R
import kotlinx.android.synthetic.main.activity_notice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity : AppCompatActivity(), INoticeItemClickListener {
    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }
    val noticeList : ArrayList<Notice> by lazy {
        ArrayList<Notice>()
    }
    var noticeTitleRecyclerViewAdapter: NoticeTitleRecyclerViewAdapter? = null
    private var noticeTitleRecyclerView : androidx.recyclerview.widget.RecyclerView? = null
    private var thisObject = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        init()
        getInterestResponse()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun init() {
        //툴바
        setSupportActionBar(act_notice_tl_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        //뷰
        noticeTitleRecyclerView = findViewById(R.id.act_notice_rv)
    }

    private fun getInterestResponse() {
        val getInterestResponse =
            networkService.getNoticeResponse()
        getInterestResponse.enqueue(object : Callback<NoticeResponse> {
            override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<NoticeResponse>, response: Response<NoticeResponse>) {
                if (response.isSuccessful) {
                    noticeList.addAll(response.body()!!.data)

                    noticeTitleRecyclerViewAdapter =
                        NoticeTitleRecyclerViewAdapter(
                            thisObject,
                            noticeList
                        )
                    noticeTitleRecyclerView!!.adapter = noticeTitleRecyclerViewAdapter
                    noticeTitleRecyclerView!!.layoutManager =
                        androidx.recyclerview.widget.LinearLayoutManager(thisObject)
                    noticeTitleRecyclerViewAdapter!!.setOnItemClickListener(thisObject)
                }
            }
        })
    }

    override fun onItemClick(notice: Notice) {
        val intent = Intent(this@NoticeActivity, NoticeHolderActivity::class.java)
        intent.putExtra("title", notice.noticeName)
        intent.putExtra("content", notice.noticeContent)
        intent.putExtra("regDate", notice.noticeRegDate)
        startActivity(intent)
    }
}
