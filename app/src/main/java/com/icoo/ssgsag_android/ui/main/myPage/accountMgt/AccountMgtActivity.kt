package com.icoo.ssgsag_android.ui.main.myPage.accountMgt

import android.app.Activity
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.ui.main.myPage.accountMgt.secession.SecessionActivity
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfo
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.myPage.accountMgt.logout.LogoutDialogFragment
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.ServiceInfoActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_account_mgt.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.IndexOutOfBoundsException
import java.lang.ref.WeakReference
import java.net.URL
import java.net.URLEncoder
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AccountMgtActivity : AppCompatActivity() {

    val univMap = HashMap<String, List<String>>()
    val univList = ArrayList<String>()

    internal val disposable = CompositeDisposable()

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    fun checkUnivValidate(text: String): Boolean {
        for (i in univList.indices) {
            if (text == univList[i])
                return true
        }
        return false
    }

    val REQUEST_CODE_SELECT_IMAGE: Int = 1004
    val My_READ_STORAGE_REQUEST_CODE: Int = 7777

    val networkService: NetworkService by lazy {
        SsgSagApplication.instance.networkService
    }

    private var userInfoList: UserInfo? = null
    private var mImageURI: String? = null

    private var isClickable = false
    private var checkNickName = false
    private var checkSchoolList = false
    private var checkMajorList = false
    private var checkGrade = false
    private var checkSid = false

    lateinit var nickname: String
    lateinit var school: String
    lateinit var major: String

    lateinit var yearAdapter: ArrayAdapter<String>
    lateinit var gradeAdapter: ArrayAdapter<String>

    var grade = ""
    var sid = ""

    val now = Calendar.getInstance()
    val year = now.get(Calendar.YEAR)
    val freshman = year.toString().substring(2)

    val gradeList = ArrayList<String>()
    val admissionYearList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_mgt)

        init()
        getJsonList()
        setGradeListAndAdmissionYearList()
        getInfoResponse()
        setEditTextChange()
        setOnClickListener()
        act_account_at_school.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                act_account_at_major.setText("")
                val text = s.toString()
                if (checkUnivValidate(text))
                    onMajorSetByUniv()
            }

            override fun afterTextChanged(s: Editable) {}
        })


    }

    inner class CustomAdapter: ArrayAdapter<String>(this@AccountMgtActivity, R.layout.item_spinner) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent)
            if (position == count) {
                //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count)
            }
            return v
        }

        override fun getCount(): Int {
            //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
            return super.getCount() - 1
        }
    }

    private fun setGradeListAndAdmissionYearList() {


        yearAdapter = CustomAdapter()
        gradeAdapter = CustomAdapter()


        for (i in freshman.toInt() - 10..freshman.toInt()) {
            if (i < 10) {
                admissionYearList.add("~ 0" + i.toString())
            } else
                admissionYearList.add(i.toString())
        }
        admissionYearList.add("학번")
        for (i in 1..5)
            gradeList.add(i.toString())
        gradeList.add("학년")

        act_account_sn_student_number.run {
            adapter = yearAdapter.apply {
                addAll(admissionYearList)
            }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        onDataCheck()
                        sid =  (position + freshman.toInt() - 10).toString()
                        if(position == 0)
                            sid = "09"
                    }
                }
        }

        act_account_sn_grade.run {
            adapter = gradeAdapter.apply {
                addAll(gradeList)
            }
            dropDownVerticalOffset = dipToPixels(40f).toInt()
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        onDataCheck()
                        grade = (position + 1).toString()
                    }
                }
        }
    }

    private fun getInfoResponse() {
        disposable.add(networkService.getInfoResponse(SharedPreferenceController.getAuthorization(this))
            .map { it.data }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userData ->
                userData.run {
                    Glide.with(act_account_civ_profile)
                        .load(userProfileUrl)
                        .into(act_account_civ_profile)
                    act_account_et_nickname.setText(userNickname)
                    act_account_at_school.setText(userUniv)
                    act_account_at_major.setText(userMajor)
                    try {
                        if(userGrade == null || userGrade == 0){
                            act_account_sn_grade.setSelection(0)
                            grade = "1"
                        }else{
                            act_account_sn_grade.setSelection(userGrade - 1)
                            grade = userGrade.toString()
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        act_account_sn_grade.setSelection(gradeAdapter.count)
                    }

                    try {

                        // 범위안에 드는 사람 (4글자)
                        if(userStudentNum.toInt() > year - 10 && userStudentNum.toInt() < year + 1) {
                            act_account_sn_student_number.setSelection((userStudentNum.toInt()-2000) - (freshman.toInt() - 10))
                            sid = (userStudentNum.toInt() - 2000).toString()
                        } // 범위 안에 드는 사람 (2글자)
                        else if(userStudentNum.toInt() > (freshman.toInt() -10) && userStudentNum.toInt() < (freshman.toInt()+1)){
                            act_account_sn_student_number.setSelection(userStudentNum.toInt() - (freshman.toInt() - 10))
                            sid = userStudentNum
                        } // 범위 안에 없는 사람 (4글자)
                        else if(userStudentNum.toInt() <= year - 10) {
                            act_account_sn_student_number.setSelection(0)
                            sid = "09"
                        } // 범위 안에 없는 사람 (2글자)
                        else if(userStudentNum.toInt() <= (freshman.toInt() -10)){
                            act_account_sn_student_number.setSelection(0)
                            sid = "09"
                        }

                        Log.e("sid", sid)
                    } catch (e: IndexOutOfBoundsException) {
                        act_account_sn_student_number.setSelection(yearAdapter.count)
                    }
                }
            }) {
                toast("네트워크 상태를 확인해주세요.")
            })
    }

    private fun setOnClickListener() {
        act_account_iv_camera.setSafeOnClickListener {
            requestReadExternalStoragePermission()
        }

        act_account_rl_logout.setSafeOnClickListener {
            val firstDlg: LogoutDialogFragment =
                LogoutDialogFragment()
            firstDlg.show(supportFragmentManager, "logout dialog")
        }

        act_account_rl_withdrawal.setSafeOnClickListener {
            startActivity<SecessionActivity>()
        }

        act_account_tv_finish.setSafeOnClickListener {
            if (isClickable) {
                if (checkValidity()) {
                    if (!mImageURI.isNullOrEmpty())
                        postPhotoResponse()
                    putInfoResponse()
//                    editUserInfo()


                }
            } else
                toast("빈칸을 모두 채워주세요!")
        }

    }

    private fun loadJSONFromAsset(fileName: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open(fileName)
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
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setEditTextChange() {
        act_account_et_nickname.onChange { onDataCheck() }
        act_account_at_school.onChange { onDataCheck() }
        act_account_at_major.onChange { onDataCheck() }
        //act_account_et_grad.onChange { onDataCheck() }
        //act_account_et_sid.onChange { onDataCheck() }
    }

    private fun onDataCheck() {
        getEditText()
        if (nickname.isEmpty() || school.isEmpty() || major.isEmpty() || grade == "" || sid == "") {
            isClickable = false
        } else if (nickname.isNotEmpty() && school.isNotEmpty() && major.isNotEmpty() && grade.isNotEmpty() && sid.isNotEmpty()) {
            isClickable = true
        }
    }

    private fun getEditText() {
        nickname = act_account_et_nickname.text.toString()
        school = act_account_at_school.text.toString()
        major = act_account_at_major.text.toString()
        //grade = act_account_et_grad.text.toString()
        //sid = act_account_et_sid.text.toString()
    }

    private fun checkValidity(): Boolean {
        val patternNickName: Pattern = Pattern.compile("^[a-zA-Zㄱ-ㅣ가-힣0-9]*\$")
        val filtersNickName = arrayOf(InputFilter.LengthFilter(10))

        val patternGrade: Pattern = Pattern.compile("^[0-9]*\$")
        val filtersGrade = arrayOf(InputFilter.LengthFilter(10))

        val patternSid: Pattern = Pattern.compile("^[0-9]*\$")
        val filtersSid = arrayOf(InputFilter.LengthFilter(20))

        act_account_et_nickname.filters = filtersNickName
        //act_account_et_grad.filters = filtersGrade
        //act_account_et_sid.filters = filtersSid


        if (patternNickName.matcher(R.id.act_account_et_nickname.toString()).matches()) {
            checkNickName = true
        } else {
            checkNickName = false
            toast("닉네임은 1자에서 10자 사이로 한글, 영문, 숫자로만 입력해주세요")
        }

        for (schoolName in univList) {
            if (schoolName == school) {
                checkSchoolList = true
                break
            } else
                checkSchoolList = false
        }

        if (!checkSchoolList) {
            toast("학교 이름을 리스트에서 골라 주세요")
        }

        univMap.get(act_account_at_school.text.toString())?.let {
            for (majorName in it) {
                if (majorName == major) {
                    checkMajorList = true
                    break
                } else
                    checkMajorList = false
            }
        }

        if (!checkMajorList) {
            toast("학과 이름을 리스트 에서 골라 주세요")
        }

        if (patternGrade.matcher(grade).matches()) {
            checkGrade = true
        } else {
            checkGrade = false
            toast("학년은 1자에서 10자 사이로 숫자로만 입력해주세요")
        }

        return checkNickName && checkSchoolList && checkMajorList && checkGrade
    }

    private fun postPhotoResponse() {
        act_account_mgt_rl_loading.visibility = View.VISIBLE
        val file: File = File(mImageURI)
        val requestfile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-detailData"), file)
        val data: MultipartBody.Part = MultipartBody.Part.createFormData("profile", URLEncoder.encode(file.name, "UTF-8"), requestfile)

        val postPhotoResponse =
            networkService.postPhotoResponse(SharedPreferenceController.getAuthorization(this), data)

        postPhotoResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                Log.e("write fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                if (response.isSuccessful) {
                    toast("저장되었습니다.")
                    val intent: Intent = Intent()
                    intent.putExtra("result", "0")
                    setResult(Activity.RESULT_OK, intent)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                act_account_mgt_rl_loading.visibility = View.GONE
            }
        })
    }

    private fun putInfoResponse() {

        val jsonObject = JSONObject()
        jsonObject.put("userNickname", nickname)
        jsonObject.put("userUniv", school)
        jsonObject.put("userMajor", major)
        jsonObject.put("userStudentNum", sid)
        jsonObject.put("userGrade", grade)

        Log.e("put sid", sid)
        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val putInfoResponse =
            networkService.putInfoResponse(
                "application/json",
                SharedPreferenceController.getAuthorization(this), gsonObject
            )

        putInfoResponse.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                Log.e("user info fail", t.toString())
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                if (response.isSuccessful) {
                    if (mImageURI.isNullOrEmpty()) {
                        toast("저장되었습니다.")
                        val intent: Intent = Intent()
                        intent.putExtra("result", "0")
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })
    }

    // 학교 리스트 가져오기
    fun getJsonList() {
        val thread = Thread(Runnable {
            try {
                val inputStream= URL("http://ssgsag-alb-2141317761.ap-northeast-2.elb.amazonaws.com/validUnivList").openStream()
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

                var str : String? = ""
                val buffer = StringBuffer()

                str = bufferedReader.readLine()
                while(str != null){
                    buffer.append(str)
                    Log.e("buffer", buffer.toString())
                    str = bufferedReader.readLine()
                }

                val bundle = Bundle()
                bundle.putString("UnivListJson", buffer.toString())

                val msg = getUnivListHandler.obtainMessage()
                msg.data = bundle
                getUnivListHandler.sendMessage(msg)


            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()

    }

    private val getUnivListHandler= GetUnivListHandler(this)

    // 핸들러 객체 만들기
    private class GetUnivListHandler(activity: AccountMgtActivity) : Handler() {

        private val accountMgtActivityWeakReference: WeakReference<AccountMgtActivity>

        init {
            accountMgtActivityWeakReference = WeakReference<AccountMgtActivity>(activity)
        }

        override fun handleMessage(msg: Message) {
            val activity = accountMgtActivityWeakReference.get()
            if (activity != null) {
                activity.handleMessage(msg)
                // 핸들메세지로 결과값 전달
            }
        }
    }

    private fun handleMessage(msg: Message){
        val bundle = msg.data

        val univObj = JSONObject(bundle.getString("UnivListJson"))
        val univJsonArr = univObj.getJSONArray("data")

        for (i in 0 until univJsonArr.length()) {
            val univJsonObj = univJsonArr.getJSONObject(i)

            val univName = univJsonObj.getString("학교명")
            val majorName = univJsonObj.getString("학부·과(전공)명")
            val majorArr = majorName
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .split(",")

            univList.add(univName)
            univMap.put(univName, majorArr)
        }

        setAutoCompleteTextList()

    }


//    fun getJsonList() {
//        val univObj = JSONObject(loadJSONFromAsset("majorListByUniv.json"))
//        val univJsonArr = univObj.getJSONArray("content")
//
//        for (i in 0 until univJsonArr.length()) {
//            val univJsonObj = univJsonArr.getJSONObject(i)
//
//            val univName = univJsonObj.getString("학교명")
//            val majorName = univJsonObj.getString("학부·과(전공)명")
//            val majorArr = majorName
//                .replace("[", "")
//                .replace("]", "")
//                .replace("\"", "")
//                .split(",")
//
//            univList.add(univName)
//            univMap.put(univName, majorArr)
//        }
//        setAutoCompleteTextList()
//        //Log.d("TAG", univMap.toString())
//    }


    private fun setAutoCompleteTextList() {

        val schoolAdapter = ArrayAdapter<String>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            univList // Array
        )
        act_account_at_school.setAdapter(schoolAdapter)
    }

    private fun onMajorSetByUniv() {
        val test = act_account_at_school.text.toString()
        Log.d("testest", univMap.get(act_account_at_school.text.toString()).toString())
        val majorAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            univMap.get(
                act_account_at_school.text.toString()
            ) // Array
        )
        act_account_at_major.setAdapter(majorAdapter)
    }

    //region 사진
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == My_READ_STORAGE_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAlbum()
            } else {
                toast("권한을 허용해주세요.")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //REQUEST_CODE_SELECT_IMAGE를 통해 앨범에서 보낸 요청에 대한 Callback인지를 체크!!!
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            //앨범 사진 선택에 대한 Callback이 RESULT_OK인지 체크!!
            if (resultCode == Activity.RESULT_OK) {
                //detailData.data에는 앨범에서 선택한 사진의 Uri가 들어있습니다!! 그러니까 제대로 선택됐는지 null인지 아닌지를 체크!!!
                if (data != null) {
                    val selectedImageUri: Uri = data.data
                    //Uri를 getRealPathFromURI라는 메소드를 통해 절대 경로를 알아내고, 인스턴스 변수인 imageURI에 String으로 넣어줍니다!
                    mImageURI = getRealPathFromURI(selectedImageUri)

                    //Glide를 통해 imageView에 우리가 선택한 이미지를 띄워 줍시다!(무엇을 선택했는지는 알아야 좋겠죠?!)
                    Glide.with(this)
                        .load(selectedImageUri)
                        .thumbnail(0.1f)
                        .into(act_account_civ_profile)

                    //여기에 서버통신하는 거 넣기
                }
            }
        }
    }

    private fun getRealPathFromURI(content: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, content, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_idx)
        cursor.close()
        return result
    }

    private fun requestReadExternalStoragePermission() {
        //첫번째 if문을 통해 과거에 이미 권한 메시지에 대한 OK를 했는지 아닌지에 대해 물어봅니다!
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                //사용자에게 권한을 왜 허용해야되는지에 메시지를 주기 위한 대한 로직을 추가하려면 이 블락에서 하면됩니다!!
                //하지만 우리는 그냥 비워놓습니다!! 딱히 줄말 없으면 비워놔도 무관해요!!! 굳이 뭐 안넣어도됩니다!
            } else {
                //아래 코드는 권한을 요청하는 메시지를 띄우는 기능을 합니다! 요청에 대한 결과는 callback으로 onRequestPermissionsResult 메소드에서 받습니다!!!
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    My_READ_STORAGE_REQUEST_CODE
                )
            }
        } else {
            //첫번째 if문의 else로써, 기존에 이미 권한 메시지를 통해 권한을 허용했다면 아래와 같은 곧바로 앨범을 여는 메소드를 호출해주면됩니다!!
            showAlbum()
        }
    }

    private fun showAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }
    //endregion

    private fun init() {
        //툴바
        setSupportActionBar(act_account_tb_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }
}