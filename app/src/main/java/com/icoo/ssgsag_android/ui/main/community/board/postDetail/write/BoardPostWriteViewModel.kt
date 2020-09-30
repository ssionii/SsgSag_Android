package com.icoo.ssgsag_android.ui.main.community.board.postDetail.write

import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder

class BoardPostWriteViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    private var _postDetail = MutableLiveData<BoardPostDetail>()
    val postDetail : LiveData<BoardPostDetail> = _postDetail

    var writeStatus = MutableLiveData<Int>()
    var editStatus = MutableLiveData<Int>()
    var photoUrl = MutableLiveData<String>()

    init {
        _isProgress.value = GONE
    }

    fun getPostDetail(postIdx : Int){
        addDisposable(repository.getBoardPostDetail(postIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("get post detail error", it.message)
            }
            .subscribe({
                _postDetail.value = it
                it.community.photoUrlList?.apply {
                    photoUrl.value = this
                }

            }) {
                Log.e("get post detail error", it.message)
            })
    }

    fun writeBoardPost(jsonObject: JSONObject){

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.writeBoardPost(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it.status == 200)
                    Toast.makeText(context, "업로드 완료!", Toast.LENGTH_SHORT).show()
                writeStatus.value = it.status
            }, {

            })
        )

    }

    fun getPhotoUrl(imgUri : String){
        val file  = File(imgUri)
        val requestfile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-detailData"), file)

        val data: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", URLEncoder.encode(file.name, "UTF-8"), requestfile)

        addDisposable(repository.getPhotoUrl(data)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                photoUrl.value = it
            }, {

            })
        )
    }

    fun editBoardPost(jsonObject: JSONObject){

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.editBoardPost(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it.status == 200)
                    Toast.makeText(context, "수정 완료!", Toast.LENGTH_SHORT).show()
                editStatus.value = it.status
            }, {

            })
        )

    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.GONE
    }
}