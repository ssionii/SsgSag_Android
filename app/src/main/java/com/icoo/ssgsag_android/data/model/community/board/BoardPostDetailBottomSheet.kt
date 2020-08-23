package com.icoo.ssgsag_android.data.model.community.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.icoo.ssgsag_android.databinding.BottomSheetPostDetailBinding
import com.icoo.ssgsag_android.databinding.BottomSheetPosterBookmarkBinding
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailDeletePosterDialogFragment
import com.icoo.ssgsag_android.ui.main.calendar.posterBookmark.PosterAlarmData
import com.icoo.ssgsag_android.ui.main.calendar.posterBookmark.PosterBookmarkRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.calendar.posterBookmark.PosterBookmarkViewModel
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardPostDetailBottomSheet (
    private val idx : Int,
    private val from: String,
    private val isMine : Boolean
) : BottomSheetDialogFragment() {

    lateinit var viewDataBinding: BottomSheetPostDetailBinding
    val viewModel: BoardPostDetailViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = BottomSheetPostDetailBinding.inflate(layoutInflater, container, false)


        setUI()
        setButton()

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
                    viewDataBinding.bottomSheetPostDetailCvRecomment.visibility = VISIBLE
                    viewDataBinding.bottomSheetPostDetailCvReport.visibility = VISIBLE
                }
            }
        }
    }

    private fun setButton(){
        viewDataBinding.bottomSheetPostDetailCvEdit.setSafeOnClickListener {
            Log.e("edit ", "click")
        }

        viewDataBinding.bottomSheetPostDetailCvDelete.setSafeOnClickListener {
            Log.e("delete ", "click")
        }

        viewDataBinding.bottomSheetPostDetailCvReport.setSafeOnClickListener {
            Log.e("report ", "click")
        }

        viewDataBinding.bottomSheetPostDetailCvRecomment.setSafeOnClickListener {
            Log.e("recomment ", "click")
        }

        viewDataBinding.bottomSheetPostDetailCvCancel.setSafeOnClickListener {
            dismiss()
        }
    }


    companion object {
        const val REQUEST_CODE = 2001
        const val FEATURES_KEY = "features"
    }
}

