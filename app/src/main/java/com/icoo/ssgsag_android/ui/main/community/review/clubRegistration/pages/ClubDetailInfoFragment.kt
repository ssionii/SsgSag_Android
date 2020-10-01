package com.icoo.ssgsag_android.ui.main.review.club.registration.pages

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.content.CursorLoader
import android.graphics.Point
import android.media.MediaScannerConnection
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.databinding.FragmentClubDetailInfoBinding
import com.icoo.ssgsag_android.ui.main.review.club.info.ClubInfoPhotoRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity.ClubRgstrData
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrViewModel
import com.icoo.ssgsag_android.ui.main.community.review.photoViewPager.PhotoViewPagerActivity
import com.icoo.ssgsag_android.util.dataBinding.replaceAll
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_club_detail_info.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.util.regex.Pattern


class ClubDetailInfoFragment : BaseFragment<FragmentClubDetailInfoBinding, ClubRgstrViewModel>(){
    override val layoutResID: Int
        get() = R.layout.fragment_club_detail_info
    override val viewModel: ClubRgstrViewModel by viewModel()

    val position = 2
    
    var isDone = false

    val REQUEST_CODE_SELECT_IMAGE: Int = 1004
    val MY_PERMISSIONS_REQUEST_MULTI: Int = 7777

    lateinit var photoAdapter: ClubInfoPhotoRecyclerViewAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setButton()
        setEditTextTouch()
        setEditTextChange()
//        setPhotoRv()
    }

    override fun onResume() {
        super.onResume()
        onDataCheck()

        (activity as ClubRgstrActivity).hideKeyboard(viewDataBinding.fragClubDetailInfoPersonnel)
    }


    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if((this@onChange != viewDataBinding.fragClubDetailInfoUrl) && (this@onChange != viewDataBinding.fragClubDetailInfoEtIntro)
                    && s!!.length > 20){
                    this@onChange.setText(s.substring(0, 20))
                    toast( "20자 이내로 입력해주세요.")
                }
            }
        })
    }

    private fun setEditTextChange() {
        viewDataBinding.fragClubDetailInfoPersonnel.onChange { onDataCheck() }
        viewDataBinding.fragClubDetailInfoMeetingTime.onChange { onDataCheck() }
        viewDataBinding.fragClubDetailInfoDues.onChange { onDataCheck() }
        viewDataBinding.fragClubDetailInfoEtIntro.onChange { onDataCheck() }
        viewDataBinding.fragClubDetailInfoUrl.onChange { onDataCheck() }
    }


    private fun getEditText() {
        ClubRgstrData.activeNum = viewDataBinding.fragClubDetailInfoPersonnel.text.toString()
        ClubRgstrData.meetingTime = viewDataBinding.fragClubDetailInfoMeetingTime.text.toString()
        ClubRgstrData.clubFee = viewDataBinding.fragClubDetailInfoDues.text.toString()
        ClubRgstrData.clubWebsite = viewDataBinding.fragClubDetailInfoUrl.text.toString()
        ClubRgstrData.introduce = viewDataBinding.fragClubDetailInfoEtIntro.text.toString()
    }

    private fun onDataCheck() {
        getEditText()

        if(ClubRgstrData.activeNum.isEmpty() || ClubRgstrData.meetingTime.isEmpty() || ClubRgstrData.clubFee.isEmpty() || ClubRgstrData.introduce.length < 10){
            viewDataBinding.fragClubDetailInfoClNext.apply{
                backgroundColor = Color.parseColor("#aaaaaa")
                isClickable = false
            }
        }else{
            viewDataBinding.fragClubDetailInfoClNext.apply{
                backgroundColor = Color.parseColor("#656ef0")
                isClickable = true
            }
        }
    }

    private fun setButton(){
        viewDataBinding.actClubManagerContactIvBack.setSafeOnClickListener {
            (activity as ClubRgstrActivity).toPrevPage(position)
        }

        viewDataBinding.fragClubDetailInfoClNext.setSafeOnClickListener {

            if(checkUrl(ClubRgstrData.clubWebsite)) {
                (activity as ClubRgstrActivity).toNextPage(position)
//                postPhotoResponse()
                (activity as ClubRgstrActivity).hideKeyboard(viewDataBinding.fragClubDetailInfoPersonnel)
            } else
                toast("사이트 주소 형식을 확인해주세요.")


        }

        viewDataBinding.fragClubDetailInfoCvPhotoUpload.setSafeOnClickListener {
            requestReadExternalStoragePermission()
        }
    }


    private fun checkUrl(input: String) : Boolean {

        if(input != "") {
            return (Pattern.matches(
                "^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$", input
            ))
        }else
            return true
    }

    private fun EditText.onTouch(){
        this.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                frag_club_detail_info_nsv.requestDisallowInterceptTouchEvent(true)
                if(event?.action == MotionEvent.ACTION_UP)
                    frag_club_detail_info_nsv.requestDisallowInterceptTouchEvent(false)


                return false
            }
        })
    }

    private fun setEditTextTouch(){
        viewDataBinding.fragClubDetailInfoEtIntro.onTouch()
    }


    /************ 사진 업로드 **************/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_MULTI) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAlbum()
            } else {
                toast("권한을 허용해주세요.")
            }
        }
    }


    private fun requestReadExternalStoragePermission() {

        val permissions = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add( android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add( android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(permissions.size > 0){
            var reqPermissionArray = arrayOfNulls<String>(permissions.size)
            reqPermissionArray = permissions.toArray(reqPermissionArray)
            ActivityCompat.requestPermissions(activity!!, reqPermissionArray, MY_PERMISSIONS_REQUEST_MULTI
            )
        }else{
            showAlbum()
        }
    }

    private fun showAlbum(){
//        FilePickerBuilder.instance.setMaxCount(9)
//            .setActivityTheme(R.style.LibAppTheme)
//            .enableVideoPicker(false)
//            .setActivityTitle("사진 선택")
//            .enableCameraSupport(false)
//            .pickPhoto(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

       when(requestCode) {
//           FilePickerConst.REQUEST_CODE_PHOTO-> {
//               if (resultCode == Activity.RESULT_OK && data != null) {
//
//                   var datas = data!!.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
//                   for (i in 0 until datas.size) {
//                            if (viewModel.photoList.size < 9) {
//                                // viewModel.addPhoto(File(datas[i]).toUri())
//                                viewModel.uploadPhoto1(datas[i])
//                                viewModel.addPath(datas[i])
//                            }
//                        }
//
//               }
//
//               }

       }

    }

    private fun setPhotoRv(){

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x

        viewModel.uploadPhotos.observe(this, Observer { value ->

            value?.let{
                Log.e("uploadPhoto", value.toString())
            }


            viewDataBinding.fragClubDetailInfoPhotoRv.run{
                Log.e("photo size~!", this.adapter?.itemCount.toString())
                if(this.adapter != null){
                    photoAdapter.apply{
                        this.replaceAll(value)
                        notifyDataSetChanged()
                    }
                }else{
                    photoAdapter = ClubInfoPhotoRecyclerViewAdapter(value)
                    photoAdapter.apply {
                        setPhotoSize(width)
                        isRgstr = true
                        setOnReviewPhotoClickListener(onItemClickListener)
                    }

                    this.adapter = photoAdapter
                    layoutManager = GridLayoutManager(activity, 3)
                    addItemDecoration(SpacesItemDecoration(3, 24))
                }
            }

            value?.let{
                if(it.size > 9){
                    viewDataBinding.fragClubDetailInfoCvPhotoUpload.apply{
                        backgroundColor = resources.getColor(R.color.grey_3)
                        isDone = false
                    }
                }else{
                    viewDataBinding.fragClubDetailInfoCvPhotoUpload.apply{
                        backgroundColor = resources.getColor(R.color.ssgsag)
                        isDone = true
                    }
                }
            }

        })
    }

    private val onItemClickListener = object : ClubInfoPhotoRecyclerViewAdapter.OnReviewPhotoClickListener{
        override fun onItemClickListener(url: Uri, position: Int) {
            val intent = Intent(activity!!, PhotoViewPagerActivity::class.java)
            intent.putExtra("photoList", viewModel.uploadPhotos.value?.toTypedArray())
            intent.putExtra("clickedPosition", position)
            startActivity(intent)
        }

        override fun onItemRemoveClickListener(url: String) {
            viewModel.removePhoto(url)
        }
    }

    private fun getRealPathFromURI(content: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(activity!!, content, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        var result = ""
        if(cursor.getString(column_idx) != null)
            result = cursor.getString(column_idx)
        else
            result = ""

        cursor.close()
        return result
    }

    private fun getRealPathFromURI2(content: Uri) : String{
        val wholeID = DocumentsContract.getDocumentId(content)
        val id = wholeID.split(":")[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor : Cursor? = context!!.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, arrayOf(id), null)

        var filePath = ""
        val columnIdx = cursor!!.getColumnIndex(column[0])

        if(cursor!!.moveToFirst()){
            filePath = cursor.getString(columnIdx)
        }

        cursor.close()
        return filePath

    }

    private fun postPhotoResponse() {

        var mImageURI = arrayListOf<String>()
        for(i in 0.. viewModel.photoPathList.size - 1){
            mImageURI.add(viewModel.photoPathList[i])
            Log.e("post path",viewModel.photoPathList[i])
        }
        //viewModel.uploadPhoto(mImageURI)
    }


}