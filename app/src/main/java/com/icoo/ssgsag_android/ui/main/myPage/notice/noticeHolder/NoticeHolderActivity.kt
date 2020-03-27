package com.icoo.ssgsag_android.ui.main.myPage.notice.noticeHolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.TextView
import com.icoo.ssgsag_android.R
import kotlinx.android.synthetic.main.activity_notice_holder.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.LevelListDrawable
import android.os.AsyncTask
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class NoticeHolderActivity : AppCompatActivity(), ImageGetter {
    var mTv:TextView ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_holder)

        init()

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
    private fun init() {
        //툴바
        setSupportActionBar(act_notice_holder_tl_toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        //뷰
        val intent = getIntent()
        act_notice_holder_tv_title.text = intent.getStringExtra("title")
        val source = intent.getStringExtra("content")
        val spanned = Html.fromHtml(source, this, null)
        mTv = findViewById(R.id.act_notice_holder_tv_content)
        mTv!!.text = spanned
        act_notice_holder_tv_date.text = intent.getStringExtra("regDate")
    }
    override fun getDrawable(source: String): Drawable {
        val d = LevelListDrawable()
        val empty = resources.getDrawable(R.drawable.ic_task_timeout)
        d.addLevel(0, 0, empty)
        d.setBounds(0, 0, empty.intrinsicWidth, empty.intrinsicHeight)

        LoadImage().execute(source, d)

        return d
    }

    internal inner class LoadImage : AsyncTask<Any, Void, Bitmap>() {

        private var mDrawable: LevelListDrawable? = null

        override fun doInBackground(vararg params: Any): Bitmap? {
            val source = params[0] as String
            mDrawable = params[1] as LevelListDrawable
            Log.d(TAG, "doInBackground $source")
            try {
                val `is` = URL(source).openStream()
                return BitmapFactory.decodeStream(`is`)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable!!)
            Log.d(TAG, "onPostExecute bitmap " + bitmap!!)
            if (bitmap != null) {
                val d = BitmapDrawable(bitmap)
                mDrawable!!.addLevel(1, 1, d)
                mDrawable!!.setBounds(0, 0, bitmap.width, bitmap.height)
                mDrawable!!.level = 1
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                val t = mTv!!.text
                mTv!!.text = t
            }
        }
    }

    companion object {
        private val TAG = "NoticeHolderActivity"
    }
}
