package com.icoo.ssgsag_android.ui.main.photoEnlarge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_photo_enlarge.*

class PhotoExpandActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_enlarge)

        setPhoto()

        act_poster_enlarge_iv_exit.setSafeOnClickListener {
            finish()
        }
    }

    private fun setPhoto(){

        var poster = intent.getStringExtra("photoUrl")

        if(poster.isNotEmpty()){
            Glide.with(this)
                .load(poster)
                .into(act_poster_enlarge_pv_img)
        }

    }
}
