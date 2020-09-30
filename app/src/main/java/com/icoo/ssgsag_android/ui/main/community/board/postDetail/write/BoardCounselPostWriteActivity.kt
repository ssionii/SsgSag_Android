package com.icoo.ssgsag_android.ui.main.community.board.postDetail.write

import android.Manifest
import android.app.Activity
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.CounselBoardCategory
import com.icoo.ssgsag_android.databinding.ActivityBoardCounselPostWriteBinding
import com.icoo.ssgsag_android.databinding.ItemBoardCounselPostWriteCategoryBinding
import com.icoo.ssgsag_android.ui.main.community.board.PostWriteType
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import com.icoo.ssgsag_android.util.view.SpacesItemDecoration
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class  BoardCounselPostWriteActivity: BaseActivity<ActivityBoardCounselPostWriteBinding, BoardPostWriteViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_board_counsel_post_write


    override val viewModel: BoardPostWriteViewModel by viewModel()

    var postIdx = 0
    var postWriteType = 0
    val REQUEST_CODE_SELECT_IMAGE: Int = 1004

    var uploadButtonClickable = false

    val categoryList = arrayListOf(
        CounselBoardCategory("취업/진로", "CAREER", false),
        CounselBoardCategory("학교생활", "UNIV", false),
        CounselBoardCategory("기타", "THE_OTHERS", false)
    )

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
//                showAlbum()
            } else {
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    var selectedCategory : CounselBoardCategory? = null
    var photoURI : String = ""
    var title : String = ""
    var content : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        postIdx = intent.getIntExtra("postIdx", 0)
        postWriteType = intent.getIntExtra("postWriteType", PostWriteType.WRITE)

        setCategoryRv()
        when(postWriteType){
            PostWriteType.WRITE -> viewDataBinding.actBoardPostWriteToolbar.toolbarCancelTvTitle.text = "고민 상담톡 작성"
            PostWriteType.EDIT -> {
                viewDataBinding.actBoardPostWriteToolbar.toolbarCancelTvTitle.text = "고민 상담톡 수정"
                setEditType()
            }
        }

        setEditTextChange()
        setButton()
        setObserve()

    }

    fun setEditType(){

        viewModel.getPostDetail(postIdx)

        viewModel.postDetail.observe(this, Observer {
            for(c in categoryList){
                if(it.community.category == c.category){
                    c.click = true
                    selectedCategory = c
                }
            }

            title = it.community.title
            content = it.community.content

            viewDataBinding.actBoardPostWriteEtTitle.setText(title)
            viewDataBinding.actBoardPostWriteEtDescription.setText(content)
            (viewDataBinding.actBoardCounselPostWriteRvCategory.adapter as BaseRecyclerViewAdapter<CounselBoardCategory, *>).run{

                replaceAll(categoryList)
                notifyDataSetChanged()
            }

            onDataCheck()
        })

    }

    fun setCategoryRv(){

        val d = resources.displayMetrics.density

        viewDataBinding.actBoardCounselPostWriteRvCategory.apply{
            adapter = object : BaseRecyclerViewAdapter<CounselBoardCategory, ItemBoardCounselPostWriteCategoryBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_board_counsel_post_write_category
                override val bindingVariableId: Int
                    get() = BR.category
                override val listener: OnItemClickListener?
                    get() = onCategoryClickListener
            }

            layoutManager = NonScrollGridLayoutManager(this@BoardCounselPostWriteActivity, 3)
            addItemDecoration(SpacesItemDecoration(3, (8 * d).toInt()))
        }

        (viewDataBinding.actBoardCounselPostWriteRvCategory.adapter as BaseRecyclerViewAdapter<CounselBoardCategory, *>).run{
            replaceAll(categoryList)
            notifyDataSetChanged()
        }
    }

    val onCategoryClickListener = object : BaseRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClicked(item: Any?, position: Int?) {
            for(category in categoryList){
                category.click = false
            }
            categoryList[position!!].click = true

            (viewDataBinding.actBoardCounselPostWriteRvCategory.adapter as BaseRecyclerViewAdapter<CounselBoardCategory, *>).run{
                replaceAll(categoryList)
                notifyDataSetChanged()
            }

            selectedCategory = item as CounselBoardCategory
            onDataCheck()
        }
    }

    private fun setButton(){
        viewDataBinding.actBoardPostWriteToolbar.toolbarCancelClCancel.setSafeOnClickListener {

            val result = Intent().apply {
                putExtra("isEdited", false)
            }
            setResult(Activity.RESULT_OK, result)
            finish()
        }

        viewDataBinding.actBoardPostWriteCvUploadPhoto.setSafeOnClickListener {
            requestReadExternalStoragePermission()
        }

        viewDataBinding.actBoardPostWriteIvPhotoDelete.setSafeOnClickListener {
           viewDataBinding.actBoardPostWriteLlUploadPhoto.visibility = VISIBLE
            viewDataBinding.actBoardPostWriteClPhoto.visibility = GONE

            photoURI = ""
        }

        viewDataBinding.actBoardPostWriteClUpload.setSafeOnClickListener {

            if(postWriteType == PostWriteType.WRITE) {
                if (uploadButtonClickable) {
                    val jsonObject = JSONObject()
                    jsonObject.put("category", selectedCategory!!.category)
                    jsonObject.put("title", title)
                    jsonObject.put("content", content)
                    if (photoURI != "") {
                        jsonObject.put("photoUrlList", photoURI)
                    }

                    viewModel.writeBoardPost(jsonObject)
                }
            }else{
                if (uploadButtonClickable) {
                    val jsonObject = JSONObject()
                    jsonObject.put("communityIdx", postIdx)
                    jsonObject.put("category", selectedCategory!!.category)
                    jsonObject.put("title", title)
                    jsonObject.put("content", content)
                    if (photoURI != "") {
                        jsonObject.put("photoUrlList", photoURI)
                    }

                    Log.e("edited category", selectedCategory!!.category)

                    viewModel.editBoardPost(jsonObject)
                }
            }

        }
    }

    private fun setObserve(){

        viewModel.writeStatus.observe(this, Observer {
            if(it == 200){
                val result = Intent().apply {
                    putExtra("type", postWriteType)
                }
                setResult(Activity.RESULT_OK, result)

                finish()
            }
        })

        viewModel.editStatus.observe(this, Observer {
            if(it == 200) {
                val result = Intent().apply {
                    putExtra("isEdited", true)
                }
                setResult(Activity.RESULT_OK, result)

                finish()
            }
        })

        viewModel.photoUrl.observe(this, Observer {

            viewDataBinding.actBoardPostWriteLlUploadPhoto.visibility = GONE
            viewDataBinding.actBoardPostWriteClPhoto.visibility = VISIBLE

            this.photoURI = it
        })
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when(this@onChange){
                    viewDataBinding.actBoardPostWriteEtTitle -> title = s.toString()
                    viewDataBinding.actBoardPostWriteEtDescription -> content = s.toString()
                }
            }
        })
    }

    private fun setEditTextChange(){
        viewDataBinding.actBoardPostWriteEtTitle.onChange { onDataCheck() }
        viewDataBinding.actBoardPostWriteEtDescription.onChange { onDataCheck() }
    }

    private fun onDataCheck(){
        if(selectedCategory != null && title != "" && content != ""){
            viewDataBinding.actBoardPostWriteClUpload.run{
                uploadButtonClickable = true
            }
        }else{
            viewDataBinding.actBoardPostWriteClUpload.run{
                uploadButtonClickable = false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val selectedImageUri: Uri = data.data
                photoURI = getRealPathFromURI(selectedImageUri)

                viewModel.getPhotoUrl(photoURI)
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            showAlbum()
        }
    }

    private fun showAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }
    //endregion

}
