package com.icoo.ssgsag_android.ui.main.calendar.calendarDetail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.item.ItemBase
import com.icoo.ssgsag_android.data.model.poster.posterDetail.comment.Comment
import com.icoo.ssgsag_android.databinding.ActivityCalendarDetailBinding
import com.icoo.ssgsag_android.databinding.ItemPosterDetailBinding
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CommentRecyclerViewAdapter.OnCommentItemClickListener
import com.icoo.ssgsag_android.util.view.NonScrollLinearLayoutManager
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Handler
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.github.mikephil.charting.data.*
import com.icoo.ssgsag_android.BuildConfig
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.photoEnlarge.PhotoExpandActivity
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagCardStackAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.igaworks.v2.core.AdBrixRm
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import kotlinx.android.synthetic.main.activity_calendar_detail.*
import kotlinx.android.synthetic.main.activity_photo_enlarge.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


class CalendarDetailActivity : BaseActivity<ActivityCalendarDetailBinding, CalendarDetailViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_calendar_detail
    override val viewModel: CalendarDetailViewModel by viewModel()
    //0: 아무행동 안함, 1: 댓글 쓰기, 2: 댓글 수정, 3: 댓글 삭제, 4: 댓글 추천
    private var commentBehaviorNum = 0
    private var commentToEdit: Comment? = null

    private val KAKAO_BASE_LINK = "https://developers.kakao.com"

    private val onCommentItemClickListener = object : OnCommentItemClickListener {
        override fun onEditClicked(commentIdx: Int, position: Int) {
            commentBehaviorNum = 2
            commentToEdit = viewModel.posterDetail.value?.commentList!![position]
            viewDataBinding.actCalDetailEtComment.run {
                setText(commentToEdit?.commentContent)
                setSelection(text.toString().length)
            }
            viewDataBinding.actCalDetailEtComment.post(Runnable {
                viewDataBinding.actCalDetailEtComment.setFocusableInTouchMode(true)
                viewDataBinding.actCalDetailEtComment.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(viewDataBinding.actCalDetailEtComment, 0)
            })
        }

        override fun onDeleteClicked(commentIdx: Int, position: Int) {
            commentBehaviorNum = 3
            viewModel.deleteComment(commentIdx, posterIdx)
        }

        override fun onLikeClicked(commentIdx: Int, like: Int) {
            if (like == 0)
                viewModel.likeComment(commentIdx, 1, posterIdx)
            else
                viewModel.likeComment(commentIdx, 0, posterIdx)
        }

        override fun onCautionClicked(commentIdx: Int) {
            viewModel.cautionComment(commentIdx)
        }

    }

    private var posterIdx: Int = 0
    private var from: String = "calendar"
    private var fromDetail : String = ""
    private var param: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.action == Intent.ACTION_VIEW){
            param = intent.data.getQueryParameter("param")
            from = "main"
        }

        viewDataBinding.vm = viewModel

        if(param == "")
            posterIdx = intent.getIntExtra("Idx", 0)
        else
            posterIdx = param.toInt()

        if(intent.getStringExtra("from") != null) {
            from = intent.getStringExtra("from")
            if(intent.getStringExtra("fromDetail") != null)
                fromDetail = intent.getStringExtra("fromDetail")
        }

        if(from == "main"){
            when(fromDetail){
                "what" ->  viewModel.getPosterDetailFromMain(posterIdx, 2)
                "all" ->  viewModel.getPosterDetailFromMain(posterIdx, 3)
                else-> viewModel.getPosterDetail(posterIdx)
            }
        }else {
            viewModel.getPosterDetail(posterIdx)

        }
        //ui
        setToolbar()
        setEnlargeClicked()
        setPosterDetailRv()
        setDetailImage()
        setBottomBar()
        setCommentRv()
        writeComment()
        bookmarkPoster()
        moveWebSite()
        directApply()


        viewModel.analytics.observe(this, androidx.lifecycle.Observer {
            setPieChart()
        })

        navigator()

    }

    private fun setToolbar() {
        setSupportActionBar(viewDataBinding.actCalDetailTb)
        supportActionBar!!.run {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back)
        }

        share()
    }


    private fun setEnlargeClicked() {
        viewDataBinding.actCalDetailCvPoster.setSafeOnClickListener {
            if(viewModel.posterDetail.value != null) {
                viewModel.navigatePhoto(
                    PhotoExpandActivity::class,
                    viewModel.posterDetail.value!!.photoUrl
                )
            }
        }
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(this@CalendarDetailActivity, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            startActivity(intent)
        })
    }

    private fun setPosterDetailRv() {
        //RecyclerView
        viewDataBinding.actCalDetailRv.apply {
            adapter =
                object : BaseRecyclerViewAdapter<ItemBase, ItemPosterDetailBinding>() {
                    override val layoutResID: Int
                        get() = R.layout.item_poster_detail
                    override val bindingVariableId: Int
                        get() = BR.posterDetail
                    override val listener: OnItemClickListener?
                        get() = null

                }
            layoutManager = NonScrollLinearLayoutManager(this@CalendarDetailActivity)
        }


    }

    private fun setDetailImage(){

       viewModel.detailImage.observe(this, Observer { value->
           if(value == "intern"){
               viewDataBinding.actCalDetailLlDetailImage.visibility = GONE
           }
       })
    }

    private fun setBottomBar(){

        if(from == "calendar"){
            viewDataBinding.actCalDetailIvBookmark.visibility = VISIBLE
            viewDataBinding.actCalDetailTvApply.visibility = VISIBLE
            viewDataBinding.actCalDetailRlStore.visibility = GONE
        }else{
            viewDataBinding.actCalDetailIvBookmark.visibility = GONE
            viewDataBinding.actCalDetailTvApply.visibility = GONE

            viewDataBinding.actCalDetailRlStore.setSafeOnClickListener {
                viewModel.managePoster(posterIdx)
            }

        }
    }


    private fun setCommentRv() {
        viewDataBinding.actCalDetailRvComment.apply {
            viewModel.posterDetail.observe(this@CalendarDetailActivity, Observer { value ->
                if (adapter != null) {
                    (this.adapter as CommentRecyclerViewAdapter).apply {
                        replaceAll(value.commentList)
                        if (commentBehaviorNum == 3) {
                            notifyDataSetChanged()
                            commentBehaviorNum = 0
                        } else {
                            notifyItemRangeChanged(0, value.commentList.size)
                        }
                    }
                    when (commentBehaviorNum) {
                        1 -> {
                            viewDataBinding.actCalDetailNsv.run {
                                post(Runnable { fullScroll(View.FOCUS_DOWN) })
                            }
                            viewDataBinding.actCalDetailEtComment.post(Runnable {
                                viewDataBinding.actCalDetailEtComment.setFocusableInTouchMode(true)
                                viewDataBinding.actCalDetailEtComment.requestFocus()
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.showSoftInput(viewDataBinding.actCalDetailEtComment, 0)
                            })
                        }
                        2 -> {
                            viewDataBinding.actCalDetailEtComment.post(Runnable {
                                viewDataBinding.actCalDetailEtComment.setFocusableInTouchMode(true)
                                viewDataBinding.actCalDetailEtComment.requestFocus()
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.showSoftInput(viewDataBinding.actCalDetailEtComment, 0)
                            })
                        }
                    }
                    commentBehaviorNum = 0
                } else {
                    adapter =
                        CommentRecyclerViewAdapter(this@CalendarDetailActivity, value.commentList).apply {
                            setOnCommentItemClickListener(onCommentItemClickListener)
                        }
                    layoutManager = NonScrollLinearLayoutManager(this@CalendarDetailActivity)
                    (this.itemAnimator as SimpleItemAnimator).run {
                        changeDuration = 0
                        supportsChangeAnimations = false
                    }
                }
            })
        }
    }

    private fun writeComment() {
        viewDataBinding.actCalDetailIvCommentWrite.setSafeOnClickListener {
            if (viewDataBinding.actCalDetailEtComment.text.toString().isNotEmpty()) {
                when (commentBehaviorNum) {
                    0 -> {
                        commentBehaviorNum = 1
                        viewModel.writeComment(viewDataBinding.actCalDetailEtComment.text.toString(), posterIdx)
                    }
                    2 -> {
                        commentToEdit?.let {
                            viewModel.editComment(
                                viewDataBinding.actCalDetailEtComment.text.toString(),
                                it.commentIdx,
                                posterIdx
                            )
                        }
                    }
                }
                viewDataBinding.actCalDetailEtComment.setText("")
            } else {
                toast("댓글을 입력해주세요.")
            }
        }
    }

    private fun bookmarkPoster() {
        viewDataBinding.actCalDetailIvBookmark.setSafeOnClickListener {
            viewModel.bookmark(posterIdx)

        }

    }

    private fun moveWebSite() {
        viewDataBinding.actCalDetailTvGoWebsite.setSafeOnClickListener {
            viewModel.webUrl.value?.let {
                viewModel.recordClickHistory(posterIdx)
                AdBrixRm.event(
                    "touchUp_MoveToWebsite",
                    AdBrixRm.AttrModel().setAttrs("posterIdx", posterIdx.toLong())
                )

                if(it != null) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse((it)))
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("error: ", e.toString());
                    }
                }
            } ?:run{
                toast("웹사이트가 존재하지 않습니다.")
            }
        }

    }

    private fun directApply() {
        viewDataBinding.actCalDetailTvApply.setSafeOnClickListener {
            toast("기능 준비중입니다:)")
        }
    }

    private fun share(){
        viewDataBinding.actCalDetailIvShare.setSafeOnClickListener {

            AdBrixRm.AttrModel().setAttrs("adv_keyword", posterIdx.toString())

            val params = FeedTemplate
                .newBuilder(
                    ContentObject.newBuilder(
                        viewModel.posterDetail.value!!.posterName,
                        viewModel.posterDetail.value!!.photoUrl,
                        LinkObject.newBuilder().build()
                    )
                        .setDescrption(viewModel.posterDetail.value!!.keyword).build()
                )
                .addButton(
                    ButtonObject(
                        "앱에서 보기", LinkObject.newBuilder()
                            .setWebUrl(KAKAO_BASE_LINK)
                            .setMobileWebUrl(KAKAO_BASE_LINK)
                            .setAndroidExecutionParams("param=${viewModel.posterDetail.value!!.posterIdx}&from=main")
                            .setIosExecutionParams("posterIdx=${viewModel.posterDetail.value!!.posterIdx}")
                            .build()
                    )
                )
                .build()

            KakaoLinkService.getInstance().sendDefault(this, params, object :
                ResponseCallback<KakaoLinkResponse>() {
                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("카카오 공유 에러", errorResult.toString())
                    toast("포스터 공유에 실패했습니다.")
                }

                override fun onSuccess(result: KakaoLinkResponse?) {
                    // adbrix
                    AdBrixRm.event(
                        "touchUp_Share", AdBrixRm.AttrModel()
                            .setAttrs("posterIdx", posterIdx.toLong())
                    )
                }
            })
        }
    }


    private fun setPieChart() {


            if (viewModel.analytics.value!!.majorCategory[0] != null) {
                viewDataBinding.actCalDetailTvAnalyticsTitleMajor.text = viewModel.analytics.value!!.majorCategory[0]
                viewDataBinding.actCalDetailTvAnalyticsTitleGradeGender.text = viewModel.analytics.value!!.grade[0] + " " + viewModel.analytics.value!!.gender[0]

                var colorTint = arrayListOf(
                    "#ff", "#b3", "#80", "#4d"
                )
                var defaultColor = "777fff"

                var majorPe = ArrayList<PieEntry>()
                var gradePe = ArrayList<PieEntry>()
                var genderPe = ArrayList<PieEntry>()

                val colors = java.util.ArrayList<Int>()

                var idx = 0
                while (idx < viewModel.analytics.value!!.majorCategory.size && viewModel.analytics.value!!.majorCategory[idx] != null) {
                    idx++
                }

                var majorCategorySize = idx
                for (i in 0..majorCategorySize - 1) {
                    majorPe.add(
                        PieEntry(
                            viewModel.analytics.value!!.majorCategoryRate[i].toFloat(),
                            viewModel.analytics.value!!.majorCategory
                        )
                    )
                    colors.add(Color.parseColor((colorTint[i]) + (defaultColor)))
                }

                viewDataBinding.actCalDetailTvMajor1.setText(viewModel.analytics.value!!.majorCategory[0])
                viewDataBinding.actCalDetailTvMajorRate1.setText(viewModel.analytics.value!!.majorCategoryRate[0].toString() + "%")


                if (majorCategorySize > 1) {
                    viewDataBinding.major2.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.majorCategory[1])
                    }
                    viewDataBinding.majorRate2.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.majorCategoryRate[1].toString() + "%")
                    }
                }
                if (majorCategorySize > 2) {
                    viewDataBinding.major3.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.majorCategory[2])
                    }
                    viewDataBinding.majorRate3.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.majorCategoryRate[2].toString() + "%")
                    }
                }
                if (majorCategorySize > 3) {
                    viewDataBinding.major4.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.majorCategory[3])
                    }
                    viewDataBinding.majorRate4.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.majorCategoryRate[3].toString() + "%")
                    }
                }

                idx = 0
                while (idx < viewModel.analytics.value!!.grade.size && viewModel.analytics.value!!.grade[idx] != null) {
                    idx++
                }

                var gradeSize = idx
                for (i in 0..gradeSize - 1) {
                    gradePe.add(
                        PieEntry(
                            viewModel.analytics.value!!.gradeRate[i].toFloat(),
                            viewModel.analytics.value!!.grade
                        )
                    )
                    colors.add(Color.parseColor((colorTint[i]) + (defaultColor)))
                }

                viewDataBinding.actCalDetailTvGrade1.setText(viewModel.analytics.value!!.grade[0])
                viewDataBinding.actCalDetailTvGradeRate1.setText(viewModel.analytics.value!!.gradeRate[0].toString() + "%")

                if (gradeSize > 1) {
                    viewDataBinding.grade2.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.grade[1])
                    }
                    viewDataBinding.gradeRate2.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.gradeRate[1].toString() + "%")
                    }
                }
                if (gradeSize > 2) {
                    viewDataBinding.grade3.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.grade[2])
                    }
                    viewDataBinding.gradeRate3.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.gradeRate[2].toString() + "%")
                    }
                }
                /**
                if (gradeSize > 3) {
                    viewDataBinding.grade4.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.grade[3])
                    }
                    viewDataBinding.gradeRate4.apply {
                        visibility = VISIBLE
                        setText(viewModel.analytics.value!!.gradeRate[3].toString() + "%")
                    }
                }**/

                for (i in 0..1) {
                    genderPe.add(PieEntry(viewModel.analytics.value!!.genderRate[i].toFloat()))
                    colors.add(Color.parseColor((colorTint[i]) + (defaultColor)))
                }

                viewDataBinding.actCalDetailTvGender1.setText(viewModel.analytics.value!!.gender[0])
                viewDataBinding.actCalDetailTvGenderrRate1.setText(viewModel.analytics.value!!.genderRate[0].toString() + "%")
                viewDataBinding.gender2.setText(viewModel.analytics.value!!.gender[1])
                viewDataBinding.genderRate2.setText(viewModel.analytics.value!!.genderRate[1].toString() + "%")

                val majorDataSet = PieDataSet(majorPe, "")
                val gradeDataSet = PieDataSet(gradePe, "")
                val genderDataSet = PieDataSet(genderPe, "")
                majorDataSet.setColors(colors)
                gradeDataSet.setColors(colors)
                genderDataSet.setColors(colors)
                majorDataSet.valueTextSize = 0f
                gradeDataSet.valueTextSize = 0f
                genderDataSet.valueTextSize = 0f


                val majorData = PieData(majorDataSet)
                val gradeData = PieData(gradeDataSet)
                val genderData = PieData(genderDataSet)

                majorChart.data = majorData
                majorChart.centerTextRadiusPercent = 0f
                majorChart.isDrawHoleEnabled = false
                majorChart.legend.isEnabled = false
                majorChart.description.isEnabled = false

                gradeChart.data = gradeData
                gradeChart.centerTextRadiusPercent = 0f
                gradeChart.isDrawHoleEnabled = false
                gradeChart.legend.isEnabled = false
                gradeChart.description.isEnabled = false

                genderChart.data = genderData
                genderChart.centerTextRadiusPercent = 0f
                genderChart.isDrawHoleEnabled = false
                genderChart.legend.isEnabled = false
                genderChart.description.isEnabled = false

                majorChart.isSelected = true
                gradeChart.isSelected = true
                genderChart.isSelected = true

            }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
