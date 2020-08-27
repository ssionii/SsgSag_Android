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
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.databinding.ActivityBoardCounselPostWriteBinding
import com.icoo.ssgsag_android.databinding.ItemBoardCounselPostWriteCategoryBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import com.icoo.ssgsag_android.util.view.SpacesItemDecoration
import kotlinx.android.synthetic.main.activity_account_mgt.*
import org.jetbrains.anko.image
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.net.URI

class  BoardCounselPostWriteActivity: BaseActivity<ActivityBoardCounselPostWriteBinding, BoardPostWriteViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_board_counsel_post_write

    override val viewModel: BoardPostWriteViewModel by viewModel()

    val categoryList = arrayListOf(
        Category(0, false,"취업/진로"),
        Category(0, false,"공모전/대외활동"),
        Category(0, false,"일상/연애"),
        Category(0, false,"학교생활"),
        Category(0, false,"기타")
    )

    val REQUEST_CODE_SELECT_IMAGE: Int = 1004
    val My_READ_STORAGE_REQUEST_CODE: Int = 7777

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showAlbum()
            } else {
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    var selectedCategory : Category? = null
    var photoURI : String = ""
    var title : String = ""
    var description : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.actBoardCounselPostWriteToolbar.toolbarCancelTvTitle.text = "고민 상담톡 작성"

        setEditTextChange()
        setCategoryRv()
        setButton()

    }

    fun setCategoryRv(){

        val d = resources.displayMetrics.density

        viewDataBinding.actBoardCounselPostWriteRvCategory.apply{
            adapter = object : BaseRecyclerViewAdapter<Category, ItemBoardCounselPostWriteCategoryBinding>(){
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

        (viewDataBinding.actBoardCounselPostWriteRvCategory.adapter as BaseRecyclerViewAdapter<Category, *>).run{
            replaceAll(categoryList)
            notifyDataSetChanged()
        }
    }

    val onCategoryClickListener = object : BaseRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClicked(item: Any?, position: Int?) {
            for(category in categoryList){
                category.isChecked = false
            }
            categoryList[position!!].isChecked = true

            (viewDataBinding.actBoardCounselPostWriteRvCategory.adapter as BaseRecyclerViewAdapter<Category, *>).run{
                replaceAll(categoryList)
                notifyDataSetChanged()
            }

            selectedCategory = item as Category
            onDataCheck()
        }
    }

    private fun setButton(){
        viewDataBinding.actBoardCounselPostWriteCvUploadPhoto.setSafeOnClickListener {
            requestReadExternalStoragePermission()
        }

        viewDataBinding.actBoardCounselPostWriteIvPhotoDelete.setSafeOnClickListener {
           viewDataBinding.actBoardCounselPostWriteLlUploadPhoto.visibility = VISIBLE
            viewDataBinding.actBoardCounselPostWriteClPhoto.visibility = GONE

            photoURI = ""
        }

        viewDataBinding.actBoardCounselPostWriteClUpload.setSafeOnClickListener {
            Log.e("category", selectedCategory!!.categoryName)
            Log.e("title", title)
            Log.e("description", description)
            if(photoURI != ""){
                Log.e("photoURI", photoURI)
            }
        }
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when(this@onChange){
                    viewDataBinding.actBoardCounselPostWriteEtTitle -> title = s.toString()
                    viewDataBinding.actBoardCounselPostWriteEtDescription -> description = s.toString()
                }
            }
        })
    }

    private fun setEditTextChange(){
        viewDataBinding.actBoardCounselPostWriteEtTitle.onChange { onDataCheck() }
        viewDataBinding.actBoardCounselPostWriteEtDescription.onChange { onDataCheck() }
    }

    private fun onDataCheck(){
        if(selectedCategory != null && title != "" && description != ""){
            viewDataBinding.actBoardCounselPostWriteClUpload.run{
                isClickable = true
                setBackgroundColor(this.resources.getColor(R.color.ssgsag))
            }
        }else{
            viewDataBinding.actBoardCounselPostWriteClUpload.run{
                isClickable = false
                setBackgroundColor(this.resources.getColor(R.color.grey_2))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val selectedImageUri: Uri = data.data
                photoURI = getRealPathFromURI(selectedImageUri)

                Glide.with(this)
                    .load(selectedImageUri)
                    .centerCrop()
                    .error(R.drawable.img_default) //에러시 나올 이미지 적용
                    .into(viewDataBinding.actBoardCounselPostWriteIvPhoto)

                viewDataBinding.actBoardCounselPostWriteLlUploadPhoto.visibility = GONE
                viewDataBinding.actBoardCounselPostWriteClPhoto.visibility = VISIBLE
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