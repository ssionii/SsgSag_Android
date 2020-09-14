package com.icoo.ssgsag_android.ui.main.allPosters.category

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseDialogFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.databinding.DialogFragmentAllCategoryFieldBinding
import com.icoo.ssgsag_android.databinding.ItemAllCategoryFieldBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollGridLayoutManager
import org.jetbrains.anko.textColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllCategoryFieldDialogFragment : BaseDialogFragment<DialogFragmentAllCategoryFieldBinding, AllCategoryViewModel>(){
    override val layoutResID: Int
        get() = R.layout.dialog_fragment_all_category_field

    override val viewModel: AllCategoryViewModel by viewModel()

    lateinit var listener: OnDialogDismissedListener

    var category = PosterCategory.CONTEST
    var interestNum = ""

    val contestFieldList = arrayListOf(Field("전체", "", false), Field("기획/아이디어", "201", false), Field("광고/마케팅", "202", false), Field("영상/콘텐츠", "206", false), Field("디자인", "205", false), Field("IT/SW", "207", false), Field("문학/시나리오", "204", false), Field("창업/스타트업", "208", false), Field("금융/경제", "215", false), Field("기타", "299", false))
    val actFieldList = arrayListOf(Field("전체", "", false),Field("대기업 서포터즈", "10000, 251", false), Field("공사/공기업 서포터즈", "50000, 251", false), Field("봉사활동", "252", false), Field("리뷰/체험단", "255", false), Field("해외봉사/탐방", "254", false), Field("기타", "299", false))
    val internFormFieldList = arrayListOf(Field("전체", "", false),Field("대기업", "10000", false), Field("공사/공기업", "50000", false), Field("중견기업", "20000", false), Field("외국계기업", "60000", false), Field("스타트업", "40000", false), Field("중소기업", "30000", false) , Field("기타", "95000", false))
    val internTaskFieldList = arrayListOf(Field("전체", "", false),Field("광고/마케팅", "110", false), Field("엔지니어링/설계", "109", false), Field("제조/생산", "103", false), Field("디자인", "112", false), Field("경영/비즈니스", "101", false), Field("개발", "104", false), Field("미디어", "107", false), Field("영업", "102", false), Field("인사/교육", "106", false), Field("고객서비스/리테일", "111", false), Field("기타", "199", false))
    val clubFieldList = arrayListOf(Field("전체", "", false),Field("문화생활", "401", false), Field("스포츠", "402", false), Field("여행", "403", false), Field("음악/예술", "404", false), Field("봉사", "405", false), Field("스터디/학회", "406", false), Field("어학", "407", false), Field("창업", "408", false), Field("친목", "409", false), Field("IT/공학", "410", false), Field("기타", "411", false))
    var fieldList = arrayListOf<Field>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        category = requireArguments().getInt("category")

        setHeader()
        setRv()
        setButton()

    }

    override fun dismiss() {
        listener.onDialogDismissed(interestNum)
        super.dismiss()
    }

    private fun setRv(){

        when(category){
            PosterCategory.CONTEST -> fieldList = contestFieldList
            PosterCategory.ACT -> fieldList = actFieldList
            PosterCategory.UNION_CLUB, PosterCategory.UNIV_CLUB -> {
                fieldList = clubFieldList
                viewDataBinding.dialogFragmentAllCategoryFieldTvLeft.text = "교내"
                viewDataBinding.dialogFragmentAllCategoryFieldTvLeft.text = "연합"
                viewDataBinding.dialogFragmentAllCategoryFieldLlHeader.visibility = VISIBLE
            }
            PosterCategory.INTERN -> {
                viewDataBinding.dialogFragmentAllCategoryFieldTvLeft.text = "기업형태"
                viewDataBinding.dialogFragmentAllCategoryFieldTvLeft.text = "관심직무"
                viewDataBinding.dialogFragmentAllCategoryFieldLlHeader.visibility = VISIBLE
                if(viewModel.isLeftHeaderClicked) {
                    fieldList = internFormFieldList
                    fieldList[viewModel.clickedFiledPositionLeft].isClicked = true
                } else{
                    fieldList = internTaskFieldList
                    fieldList[viewModel.clickedFieldPositionRight].isClicked = true
                }
            }
        }

        if(!(category == PosterCategory.INTERN && !viewModel.isLeftHeaderClicked)){
            fieldList[viewModel.clickedFiledPositionLeft].isClicked = true
        }


        viewDataBinding.dialogFragAllCategoryFieldRv.run{
            adapter = object : BaseRecyclerViewAdapter<Field, ItemAllCategoryFieldBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_all_category_field
                override val bindingVariableId: Int
                    get() = BR.field
                override val listener: OnItemClickListener?
                    get() = onItemClickListener
            }

            layoutManager = NonScrollGridLayoutManager(requireActivity(), 2)

            (adapter as BaseRecyclerViewAdapter<Field, *>).apply {
                replaceAll(fieldList)
                notifyDataSetChanged()
            }
        }

    }

    val onItemClickListener = object : BaseRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClicked(item: Any?, position: Int?) {
            if(category == PosterCategory.INTERN && !viewModel.isLeftHeaderClicked){
                viewModel.clickedFieldPositionRight = position!!
            }else{
                viewModel.clickedFiledPositionLeft = position!!
            }

            interestNum = (item as Field).number
            dismiss()
        }
    }

    private fun setHeader(){
        if(viewModel.isLeftHeaderClicked){
            selectedCardView(viewDataBinding.dialogFragAllCategoryFieldCvLeft, viewDataBinding.dialogFragmentAllCategoryFieldTvLeft)
            unselectedCardView(viewDataBinding.dialogFragAllCategoryFieldCvRight, viewDataBinding.dialogFragmentAllCategoryFieldTvRight)
        }else{
            selectedCardView(viewDataBinding.dialogFragAllCategoryFieldCvRight, viewDataBinding.dialogFragmentAllCategoryFieldTvRight)
            unselectedCardView(viewDataBinding.dialogFragAllCategoryFieldCvLeft, viewDataBinding.dialogFragmentAllCategoryFieldTvLeft)
        }
    }


    private fun setButton(){
        viewDataBinding.dialogFragAllCategoryFieldClCancel.setSafeOnClickListener {
            super.dismiss()
        }

        viewDataBinding.dialogFragAllCategoryFieldCvLeft.setSafeOnClickListener {
            viewModel.isLeftHeaderClicked = true

            selectedCardView(it as CardView, viewDataBinding.dialogFragmentAllCategoryFieldTvLeft)
            unselectedCardView(viewDataBinding.dialogFragAllCategoryFieldCvRight, viewDataBinding.dialogFragmentAllCategoryFieldTvRight)

            setRv()
        }

        viewDataBinding.dialogFragAllCategoryFieldCvRight.setSafeOnClickListener {
            viewModel.isLeftHeaderClicked = false

            selectedCardView(it as CardView, viewDataBinding.dialogFragmentAllCategoryFieldTvRight)
            unselectedCardView(viewDataBinding.dialogFragAllCategoryFieldCvLeft, viewDataBinding.dialogFragmentAllCategoryFieldTvLeft)
            setRv()
        }
    }

    private fun selectedCardView(view : CardView, textView : TextView){
        view.setCardBackgroundColor(Color.parseColor("#26656ef0"))
        textView.textColor = view.context.getColor(R.color.selectedTabColor)
        textView.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_medium)
    }

    private fun unselectedCardView(view : CardView, textView : TextView){
        view.setCardBackgroundColor(Color.parseColor("#f2f2f2"))
        textView.textColor = view.context.getColor(R.color.unselected_text_color)
        textView.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_regular)
    }

    fun setOnDialogDismissedListener(listener: OnDialogDismissedListener) {
        this.listener = listener
    }

    interface OnDialogDismissedListener {
        fun onDialogDismissed(interest : String)
    }

}

data class Field(
    val name : String,
    val number : String,
    var isClicked : Boolean
)

object PosterCategory{
    const val CONTEST = 0
    const val ACT = 1
    const val UNION_CLUB = 2
    const val INTERN = 4
    const val UNIV_CLUB = 6
    const val EDU = 7
    const val SCHOLARSHIP = 8
}