package com.icoo.ssgsag_android.ui.main.community.board

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.icoo.ssgsag_android.databinding.BottomSheetPostDetailBinding
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailViewModel
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.write.BoardCounselPostWriteActivity
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.write.BoardTalkPostWriteActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardPostDetailBottomSheet (
    private val idx : Int,
    private val from: String,
    private val type : Int,
    private val isMine : Boolean
) : BottomSheetDialogFragment() {

    lateinit var viewDataBinding: BottomSheetPostDetailBinding
    val viewModel: BoardPostDetailViewModel by viewModel()

    val editPostRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {
            if(data!!.getBooleanExtra("isEdited", false)) {
                listener.onPostEdited()
            }
            dismiss()

        }
    }


    lateinit var listener: OnSheetDismissedListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = BottomSheetPostDetailBinding.inflate(layoutInflater, container, false)


        setUI()
        setButton()
        setObserve()

        return viewDataBinding.root
    }

    private fun setUI(){
        when(from){
            "post" -> {
                if(isMine){
                    viewDataBinding.bottomSheetPostDetailCvEdit.visibility = VISIBLE
                    viewDataBinding.bottomSheetPostDetailCvDelete.visibility = VISIBLE
                }else{
                    viewDataBinding.bottomSheetPostDetailCvReport.visibility = VISIBLE
                }
            }
            "comment" -> {
                if(isMine){
                    viewDataBinding.bottomSheetPostDetailCvDelete.visibility = VISIBLE
                }else{
                    viewDataBinding.bottomSheetPostDetailCvReport.visibility = VISIBLE
                }
            }
        }
    }

    private fun setButton(){
        viewDataBinding.bottomSheetPostDetailCvEdit.setSafeOnClickListener {
            when(type){
                CommunityBoardType.COUNSEL ->{
                    val intent = Intent(activity, BoardCounselPostWriteActivity::class.java)
                    intent.putExtra("postIdx", idx)
                    intent.putExtra("postWriteType", PostWriteType.EDIT)
                    editPostRequest.launch(intent)
                }
                CommunityBoardType.TALK ->{
                    val intent = Intent(activity, BoardTalkPostWriteActivity::class.java)
                    intent.putExtra("postIdx", idx)
                    intent.putExtra("postWriteType", PostWriteType.EDIT)
                    editPostRequest.launch(intent)
                }
            }

        }

        viewDataBinding.bottomSheetPostDetailCvDelete.setSafeOnClickListener {
            viewModel.deletePost(idx)
        }

        viewDataBinding.bottomSheetPostDetailCvReport.setSafeOnClickListener {
            Log.e("report ", "click")
        }

        viewDataBinding.bottomSheetPostDetailCvCancel.setSafeOnClickListener {
            dismiss()
        }
    }


    private fun setObserve(){
        viewModel.deleteStatus.observe(this, Observer {
            if(it == 200) {
                listener.onSheetDismissed()
                dismiss()
            }
        })
    }

    fun setOnSheetDismissedListener(listener: OnSheetDismissedListener) {
        this.listener = listener
    }

    interface OnSheetDismissedListener {
        fun onSheetDismissed()
        fun onPostEdited()
    }

    companion object {
        const val REQUEST_CODE = 2001
        const val FEATURES_KEY = "features"
    }
}

