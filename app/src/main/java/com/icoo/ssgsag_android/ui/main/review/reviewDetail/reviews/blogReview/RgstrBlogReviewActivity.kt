package com.icoo.ssgsag_android.ui.main.review.club.reviews.blogReview

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityRgstrBlogReviewBinding
import com.icoo.ssgsag_android.ui.main.review.ReviewDoneActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_rgstr_blog_review.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RgstrBlogReviewActivity : BaseActivity<ActivityRgstrBlogReviewBinding, BlogReviewViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_rgstr_blog_review

    override val viewModel : BlogReviewViewModel by viewModel()

    var clubIdx = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clubIdx = intent.getIntExtra("clubIdx", -1)

        setButton()
        setEditTextChange()

        viewModel.postStatus.observe(this, Observer {
            if(it == 200){
                finish()
                val intent = Intent(this,ReviewDoneActivity::class.java)
                intent.putExtra("from", "rgstr")
                startActivity(intent)

            }else if(it !== 0){
                toast("블로그 포스트 주소를 확인해주세요")
            }
        })

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

    private fun setEditTextChange(){
        viewDataBinding.actRgstrBlogReviewEtUrl.onChange { onDataCheck() }
    }

    private fun onDataCheck(){
        if(viewDataBinding.actRgstrBlogReviewEtUrl.text.isEmpty()){
            viewDataBinding.actRgstrBlogReviewClDone.apply {
                this.backgroundColor = this.resources.getColor(R.color.grey_2)
                this.isClickable = false
            }
        }else
            viewDataBinding.actRgstrBlogReviewClDone.apply {
                this.backgroundColor = this.resources.getColor(R.color.ssgsag)
                this.isClickable = true
            }
    }

    private fun checkBlogUrl(url: String) : Boolean{
        val naverUrl = "https://blog.naver.com/"

        if(url.length >= naverUrl.length) {
            return url.substring(0, naverUrl.length) == naverUrl
        }else
            return false
    }

    private fun setButton(){
        viewDataBinding.actRgstrBlogReviewClDone.setSafeOnClickListener {
            viewDataBinding.actRgstrBlogReviewEtUrl.text.toString().let{
                if(checkBlogUrl(it)) {
                    viewModel.postClubBlogReview(clubIdx, it)
                }
                else
                    toast("블로그 포스트 주소를 확인해주세요")
            }
        }
        viewDataBinding.actRgstrBlogReviewIvCancel.setSafeOnClickListener {
            finish()
        }
    }

}