package com.icoo.ssgsag_android.ui.main.community.review.photoViewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.icoo.ssgsag_android.R

class PhotoFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_photo, container, false)

        val imgUrl =  arguments?.getString("imgUrl")
        val imageView = root.findViewById<ImageView>(R.id.frag_photo_iv)

        Glide.with(this)
            .load(imgUrl)
            .error(R.drawable.img_default) //에러시 나올 이미지 적용
            .into(imageView)



        return root
    }

    companion object {
        fun newInstance(imgUrl: String): PhotoFragment {
            val fragment = PhotoFragment()
            val bundle = Bundle()
            bundle.putString("imgUrl", imgUrl)
            fragment.arguments = bundle
            return fragment
        }
    }
}