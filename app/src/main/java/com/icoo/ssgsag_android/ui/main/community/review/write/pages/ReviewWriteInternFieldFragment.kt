package com.icoo.ssgsag_android.ui.main.community.review.write.pages

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.model.review.ReviewWriteRelam
import com.icoo.ssgsag_android.databinding.FragmentReviewWriteInternFieldBinding
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.ui.main.review.club.write.pages.ReviewWriteSimpleFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.realm.Realm
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewWriteInternFieldFragment : BaseFragment<FragmentReviewWriteInternFieldBinding, ReviewWriteViewModel>(){

    override val layoutResID: Int
        get() = R.layout.fragment_review_write_intern_field

    override val viewModel: ReviewWriteViewModel by viewModel()

    val realm = Realm.getDefaultInstance()
    lateinit var reviewWriteRealm : ReviewWriteRelam

    var position = -1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        position = arguments!!.getInt("position", -1)

        reviewWriteRealm = realm.where(ReviewWriteRelam::class.java).equalTo("id", 1 as Int).findFirst()!!


        setEditTextChange()
        setButton()

    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setEditTextChange() {
        viewDataBinding.fragReviewWriteInternFieldEtFiled.onChange { onDataCheck() }
    }

    private fun getEditText() {
        (activity as ReviewWriteActivity).setReviewWriteStringRealm("fieldName", viewDataBinding.fragReviewWriteInternFieldEtFiled.text.toString())
    }

    private fun onDataCheck() {
        getEditText()

        if(reviewWriteRealm.fieldName.isEmpty()){
            // todo: viewModel로 해결하는 방법 찾기
            viewDataBinding.fragReviewWriteInternFieldClNext.apply{
                backgroundColor = Color.parseColor("#aaaaaa")
                isClickable = false
            }

        }else{
            viewDataBinding.fragReviewWriteInternFieldClNext.apply{
                backgroundColor = Color.parseColor("#656ef0")
                isClickable = true
            }
        }
    }

    private fun setButton(){
        viewDataBinding.fragReviewWriteInternFieldIvBack.setSafeOnClickListener {
            (activity as ReviewWriteActivity).toPrevPage(position)
            (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragReviewWriteInternFieldEtFiled)
        }

        viewDataBinding.fragReviewWriteInternFieldClNext.setSafeOnClickListener {
            (activity as ReviewWriteActivity).toNextPage(position)
            (activity as ReviewWriteActivity).hideKeyboard(viewDataBinding.fragReviewWriteInternFieldEtFiled)
        }
    }

    companion object {
        fun newInstance(position: Int): ReviewWriteInternFieldFragment {
            val fragment = ReviewWriteInternFieldFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }

}