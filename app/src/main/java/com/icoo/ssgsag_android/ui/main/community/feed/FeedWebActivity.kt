package com.icoo.ssgsag_android.ui.main.community.feed

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
        }else{
            viewDataBinding.actFeedWebDetailIvBookmark.visibility = INVISIBLE
        }

        viewDataBinding.actFeedWebDetailWv.loadUrl(intent.getStringExtra("url"))

        setButton()



    }

    private fun setButton(){
        act_feed_web_detail_iv_back.setSafeOnClickListener {
            finish()
        }

        act_feed_web_detail_iv_bookmark.setSafeOnClickListener {
            viewModel.feed.value?.apply {
//                viewModel.bookmark(this.feedIdx, this.isSave, 0,"web")
            }
        }
    }
}