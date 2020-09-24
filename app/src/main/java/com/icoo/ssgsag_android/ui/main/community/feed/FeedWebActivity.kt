package com.icoo.ssgsag_android.ui.main.community.feed

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.webkit.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityFeedWebDetailBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_feed_web_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedWebActivity : BaseActivity<ActivityFeedWebDetailBinding, FeedViewModel>() {


    override val layoutResID: Int
        get() = R.layout.activity_feed_web_detail
    override val viewModel: FeedViewModel by viewModel()

    var isSave = MutableLiveData<Int>()

    var position = 0
    var idx = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.activity = this

        viewDataBinding.actFeedWebDetailWv.apply{
            settings.javaScriptEnabled = true

            webChromeClient = WebChromeClient()

            webViewClient = object : WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    act_feed_web_detail_progress_bar.visibility = VISIBLE
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)

                    Log.e("web view ", error.toString())
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    act_feed_web_detail_progress_bar.visibility = GONE
                    super.onPageFinished(view, url)
                }
            }



        }

        viewDataBinding.actFeedWebDetailTvName.text = intent.getStringExtra("title")

        if(intent.getStringExtra("from") == "feed"){
            isSave.value = intent.getIntExtra("isSave", 0)
            position = intent.getIntExtra("position", 0)
        }else{
            viewDataBinding.actFeedWebDetailIvBookmark.visibility = INVISIBLE
        }

        viewDataBinding.actFeedWebDetailWv.loadUrl(intent.getStringExtra("url"))

        setButton()
        setObserver()

    }

    private fun setObserver(){
        viewModel.feedBookmarkStatus.observe(this, Observer {
            if(it == 200){
                if( isSave.value == 0) isSave.value = 1
                else isSave.value = 0

                val result = Intent().apply {
                    putExtra("isSave", isSave.value!!)
                    putExtra("position", position)
                }
                setResult(Activity.RESULT_OK, result)
            }
        })
    }

    private fun setButton(){
        viewDataBinding.actFeedWebDetailIvBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actFeedWebDetailIvBookmark.setSafeOnClickListener {
            viewModel.bookmarkFromWeb(intent.getIntExtra("idx", 0), isSave.value!!)
        }
    }
}