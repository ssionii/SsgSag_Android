package com.icoo.ssgsag_android.ui.main.myPage.career.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icoo.ssgsag_android.ui.main.myPage.career.careerEdit.CareerEditActivity
import com.icoo.ssgsag_android.ui.main.myPage.career.CareerRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.career.Career
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.career.CareerResponse
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.ui.main.myPage.career.CareerDeleteDialogFragment
import com.icoo.ssgsag_android.util.listener.IItemClickListener
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.fragment_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityFragment : androidx.fragment.app.Fragment(), IItemClickListener {
    private var mView: Int = 0

    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }
    lateinit var careerRecyclerViewAdapter: CareerRecyclerViewAdapter
    val careerList : ArrayList<Career> by lazy {
        ArrayList<Career>()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getCareerReferenceResponse()
        //setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        getCareerReferenceResponse()
        setOnClickListener()
    }

    private fun getCareerReferenceResponse() {
        val getCareerReferenceResponse =
            networkService.getCareerReferenceResponse(SharedPreferenceController.getAuthorization(activity!!), 0)
        getCareerReferenceResponse.enqueue(object : Callback<CareerResponse> {
            override fun onFailure(call: Call<CareerResponse>, t: Throwable) {
                //toast("fail")
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<CareerResponse>, response: Response<CareerResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.data.size != 0) {
                        frag_act_rl_empty_career.visibility = View.GONE
                        frag_act_rv.visibility = View.VISIBLE
                        careerList.clear()
                        careerList.addAll(response.body()!!.data)
                        setRecyclerView()
                        //toast("if")
                    }
                    else {
                        frag_act_rl_empty_career.visibility = View.VISIBLE
                        frag_act_rv.visibility = View.GONE
                        //toast("else")
                    }
                }
            }
        })
    }

    fun deleteCareerResponse(careerIdx: Int) {
        val deleteCareerResponse =
            networkService.deleteCareerResponse(SharedPreferenceController.getAuthorization(activity!!), careerIdx)

        deleteCareerResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                if (response.isSuccessful) {
                    getCareerReferenceResponse()
                }
            }
        })
    }

    private fun setRecyclerView() {
        careerRecyclerViewAdapter =
            CareerRecyclerViewAdapter(activity!!, careerList)
        frag_act_rv.adapter = careerRecyclerViewAdapter
        frag_act_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity!!)
        frag_act_rv.setNestedScrollingEnabled(false);
        careerRecyclerViewAdapter.setOnItemClickListener(this)
        frag_act_rv.adapter!!.notifyDataSetChanged()//추가한부분
    }

    override fun onDataListCheck() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClick(data: Career, section: Int, idx: Int) {
        val intent = Intent(activity, CareerEditActivity::class.java)
        intent.putExtra("idx", data.careerIdx)
        intent.putExtra("careerName", data.careerName)
        intent.putExtra("careerDate1", data.careerDate1)
        intent.putExtra("careerDate2", data.careerDate2)
        intent.putExtra("careerContent", data.careerContent)
        intent.putExtra("section", section)
        intent.putExtra("isAdd", false)
        startActivityForResult(intent, 3000)
        //Toast.makeText(activity!!, String.format("ItemClick:%d", position),Toast.LENGTH_SHORT).show();
    }

    override fun onItemDelete(idx: Int) {
        val firstDlg: CareerDeleteDialogFragment =
            CareerDeleteDialogFragment()
        firstDlg.category = 1
        firstDlg.idx = idx
        firstDlg.show(childFragmentManager,"career delete dialog")
        //deleteCareerResponse(idx)
    }

    private fun setOnClickListener() {
        frag_act_rl_add_activity.setSafeOnClickListener {
            val intent = Intent(activity, CareerEditActivity::class.java)
            intent.putExtra("section", 0)
            intent.putExtra("isAdd", true)
            startActivityForResult(intent, 3000)
        }
    }
}