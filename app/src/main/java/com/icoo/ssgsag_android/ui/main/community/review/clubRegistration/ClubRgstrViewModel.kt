package com.icoo.ssgsag_android.ui.main.review.club.registration

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeFilter
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class ClubRgstrViewModel(
    private val clubReviewRepository: ClubReviewRepository
    , private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    var defaultClubCategoryList = arrayListOf(
        Category(0, false, "스터디/학회"),
        Category(1, false, "어학"),
        Category(2, false, "봉사"),
        Category(3, false, "여행"),
        Category(4, false, "스포츠"),
        Category(5, false, "문화생활"),
        Category(6, false, "음악/예술"),
        Category(7, false, "IT/공학"),
        Category(8, false, "창업"),
        Category(9, false, "친목"),
        Category(10, false, "기타")
    )

    var clubIdx = -1

    var selectedClubCategoryList = arrayListOf<String>()
    var photoList = mutableListOf<String>()
    var photoPathList = arrayListOf<String>()
    var clubPhotoUrlList = arrayListOf<String>()

    private var _clubType = MutableLiveData<Int>()
    val clubType : LiveData<Int> get() = _clubType

    private var _clubCategoryList = MutableLiveData<ArrayList<Category>>()
    val clubCategoryList : LiveData<ArrayList<Category>> get() = _clubCategoryList

    private var _uploadPhotos = MutableLiveData<MutableList<String>>()
    val uploadPhotos : LiveData<MutableList<String>> get() = _uploadPhotos

    init {
        _clubCategoryList.postValue(defaultClubCategoryList)
    }

    fun setClubType(type: Int){
        _clubType.postValue(type)
    }

    fun selectCategory(position: Int){
        if(selectedClubCategoryList.size < 3 && !defaultClubCategoryList[position].isChecked){
            selectedClubCategoryList.add(defaultClubCategoryList[position].categoryName!!)

            defaultClubCategoryList[position].isChecked = !defaultClubCategoryList[position].isChecked
        }else if(defaultClubCategoryList[position].isChecked){
            selectedClubCategoryList.remove(defaultClubCategoryList[position].categoryName!!)

            defaultClubCategoryList[position].isChecked = !defaultClubCategoryList[position].isChecked
        }else if(selectedClubCategoryList.size >= 3){
            Toast.makeText(context, "카테고리는 최대 3개까지 등록 가능합니다.", Toast.LENGTH_SHORT).show()
        }

    }

    fun initCategoryList(){
        for(i in 0..defaultClubCategoryList.size - 1){
            defaultClubCategoryList[i].isChecked = false
        }
    }

    fun addPhoto(photo : Uri){
        photoList.add(photo.toString())
        _uploadPhotos.postValue(photoList)
    }

    fun addPath(path: String){
        photoPathList.add(path)
    }

    fun removePhoto(photo: String){
        photoList.remove(photo)
        _uploadPhotos.postValue(photoList)
    }

    fun rgstrClub(jsonObject : JSONObject){

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(clubReviewRepository.rgstrClub(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({

            }, {
                it.printStackTrace()
            })
        )
    }

    fun uploadPhoto(mImageURI : ArrayList<String>){
        for(i in 0.. mImageURI.size -1) {
            val file  = File(mImageURI[i])
            val requestfile: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-detailData"), file)
            val data: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestfile)

            addDisposable(clubReviewRepository.uploadPhoto(data)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({

                    try {
                        clubPhotoUrlList.add(it.data)
                        Log.e("photoUrl", it.data)
                    }catch(e: Exception){
                        e.printStackTrace()
                    }
                }, {
                    it.printStackTrace()
                })
            )
        }
    }

    fun uploadPhoto1(imgUri : String){
            val file  = File(imgUri)
            val requestfile: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-detailData"), file)
            val data: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestfile)

            addDisposable(clubReviewRepository.uploadPhoto(data)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({

                    Log.e("uploadPhoto status", it.status.toString())

                    try {
                        photoList.add(it.data)
                        Log.e("photoList size", photoList.size.toString())
                        _uploadPhotos.postValue(photoList)
                        clubPhotoUrlList.add(it.data)
                        Log.e("photoUrl", it.data)
                    }catch(e: Exception){
                        e.printStackTrace()
                    }
                }, {
                    it.printStackTrace()
                })
            )

    }

    fun clearPhotos(){
        val emptyArrayList = mutableListOf<String>()
        _uploadPhotos.postValue(null)
        photoList.clear()
        clubPhotoUrlList.clear()
    }

    override fun onCleared() {
        super.onCleared()
    }

}
