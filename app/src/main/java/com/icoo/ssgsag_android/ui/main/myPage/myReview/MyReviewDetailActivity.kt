package com.icoo.ssgsag_android.ui.main.myPage.myReview

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.databinding.ActivityMyReviewDetailBinding
import com.icoo.ssgsag_android.ui.main.review.club.edit.ClubReviewEditActivity
import com.icoo.ssgsag_android.ui.main.review.club.edit.ClubReviewEditDialogFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyReviewDetailActivity : BaseActivity<ActivityMyReviewDetailBinding, MyReviewViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_my_review_detail

    override val viewModel: MyReviewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setMyReviewDetail(intent.getSerializableExtra("myReview") as ClubPost)
        viewDataBinding.vm = viewModel

        setButton()
        setEdit()

    }

    private fun setButton(){
        viewDataBinding.actMyReviewIvBack.setSafeOnClickListener {
            finish()
        }
    }

    private fun setEdit(){
        viewDataBinding.itemReviewSsgsagIvEdit.setOnClickListener {
            val popup = PopupMenu(this, it)
            val inflater = popup.menuInflater
            val menu = popup.menu

            inflater.inflate(R.menu.menu_cal_detail_comment, menu)
                menu.findItem(R.id.menu_cal_detail_edit).setVisible(true)
                menu.findItem(R.id.menu_cal_detail_delete).setVisible(true)
                menu.findItem(R.id.menu_cal_detail_caution).setVisible(false)

            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.menu_cal_detail_edit -> {

                            val editDialog = ClubReviewEditDialogFragment()
                            editDialog.apply{
                                setOnDialogDismissedListener(dialogDismissedListener)
                                usage = "edit"
                                this.item = viewModel.myReviewDetail.value!!
                            }
                            editDialog.show(supportFragmentManager!!, null)
                        }
                        R.id.menu_cal_detail_delete -> {
                            val editDialog = ClubReviewEditDialogFragment()
                            editDialog.apply{
                                setOnDialogDismissedListener(dialogDismissedListener)
                                usage = "delete"
                                this.idx = viewModel.myReviewDetail.value!!.clubPostIdx
                            }
                            editDialog.show(supportFragmentManager!!, null)
                        }
                    }
                    return false
                }
            })
            popup.show()
        }
    }

    val dialogDismissedListener
            = object : ClubReviewEditDialogFragment.OnDialogDismissedListener{
        override fun onDeleteDialogDismissed(idx: Int, position: Int) {
            viewModel.deleteReview(idx)
            finish()
        }

        override fun onEditDialogDismissed(item: ClubPost?, position: Int) {
            val intent = Intent(this@MyReviewDetailActivity, ClubReviewEditActivity::class.java)
            intent.putExtra("clubReview", item)

            startActivity(intent)
            finish()
        }
    }


}