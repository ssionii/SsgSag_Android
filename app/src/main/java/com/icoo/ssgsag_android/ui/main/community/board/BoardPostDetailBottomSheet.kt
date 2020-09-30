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
import com.icoo.ssgsag_android.base.dialogFragment.BaseDialogFragment
import com.icoo.ssgsag_android.base.dialogFragment.TwoButtonDialogFragment
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

    lateinit var twoButtonDialog : TwoButtonDialogFragment

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
            "comment", "reply" -> {
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
            when(from){
                "post" -> twoButtonDialog = TwoButtonDialogFragment.newInstance("게시글을 삭제하시겠어요?\n삭제한 글은 복구할 수 없습니다.", "취소", "확인")
                "comment" -> twoButtonDialog = TwoButtonDialogFragment.newInstance("댓글을 삭제하시겠어요?\n삭제한 댓글은 복구할 수 없습니다.", "취소", "확인")
                "reply" -> twoButtonDialog = TwoButtonDialogFragment.newInstance("답글을 삭제하시겠어요?\n삭제한 답글은 복구할 수 없습니다.", "취소", "확인")
            }

            twoButtonDialog.setDialogDismissListener(dialogDismissedListener)
            twoButtonDialog.show(requireFragmentManager(), null)
        }

        viewDataBinding.bottomSheetPostDetailCvReport.setSafeOnClickListener {
            Log.e("report ", "click")
        }

        viewDataBinding.bottomSheetPostDetailCvCancel.setSafeOnClickListener {
            dismiss()
        }
    }

    val dialogDismissedListener = object : TwoButtonDialogFragment.TwoButtonDialogDismissListener{
        override fun onLeftButtonClick() {
            twoButtonDialog.dismiss()
        }

        override fun onRightButtonClick() {
            when(from) {
                "post"-> viewModel.deletePost(idx)
                "comment" -> viewModel.deleteComment(idx)
                "reply" -> viewModel.deleteReply(idx)
            }
        }

        override fun onDialogDismissed() {
        }
    }


    private fun setObserve(){
        viewModel.deleteStatus.observe(this, Observer {
            if(it == 200) {
                when(from){
                    "post" ->{
                        listener.onPostDeleted()
                        twoButtonDialog.dismiss()
                        dismiss()
                    }
                    "comment", "reply" ->{
                        listener.onCommentDeleted()
                        twoButtonDialog.dismiss()
                        dismiss()
                    }
                }

            }
        })
    }

    fun setOnSheetDismissedListener(listener: OnSheetDismissedListener) {
        this.listener = listener
    }

    interface OnSheetDismissedListener {
        fun onPostEdited()
        fun onPostDeleted()
        fun onCommentDeleted()
    }

    companion object {
        const val REQUEST_CODE = 2001
        const val FEATURES_KEY = "features"
    }
}

