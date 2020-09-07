package com.icoo.ssgsag_android.util.dataBinding

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter


@BindingAdapter("llVisibilityByString")
fun setLinearLayoutVisibility(layout: LinearLayout, string: String?){
    if(string == "")
        layout.visibility = View.GONE
    else
        layout.visibility = View.VISIBLE
}

// R: 리버스 라는 뜻
@BindingAdapter("clVisibilityByStringR")
fun setConstraintLayoutVisibilityR(layout: ConstraintLayout, string: String?){
    if(string != "")
        layout.visibility = View.GONE
    else
        layout.visibility = View.VISIBLE
}

@BindingAdapter("tvVisibilityByInt")
fun setTextViewVisibility(view: TextView, num: Int){
    if(num == 1)
        view.visibility = View.GONE
    else
        view.visibility = View.VISIBLE
}

@BindingAdapter("tvVisibilityByString")
fun setTextviewVisivility(view: TextView, str: String?){
    if(str == null || str == "")
        view.visibility = View.GONE
    else
        view.visibility = View.VISIBLE
}

@BindingAdapter("tvVisibilityFromEmptyView")
fun setTextViewVisibilityFromEmptyView(view: TextView, num: Int){

    if(num == 0)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}


@BindingAdapter("ivVisibilityByInt")
fun setImageViewVisibility(view: ImageView, num: Int){
    if(num == 0)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}

@BindingAdapter("clVisibilityByBool")
fun setConstraintLayoutVisibility(layout: ConstraintLayout, bool: Boolean){
    if(bool)
        layout.visibility = View.VISIBLE
    else
        layout.visibility = View.GONE
}

@BindingAdapter("llVisibilityByInt")
fun LinearLayout.setVisibilityByInt(num: Int){
    when(num){
        1 -> this.visibility = View.VISIBLE
        else -> this.visibility = View.GONE
    }
}

@BindingAdapter("llVisibilityByIntR")
fun LinearLayout.setVisibilityByIntR(num: Int){
    when(num){
        0 -> this.visibility = View.VISIBLE
        else -> this.visibility = View.GONE
    }
}

@BindingAdapter("cvVisibilityByIntR")
fun CardView.setVisibilityByIntR(num: Int){
    when(num){
        0 -> this.visibility = View.VISIBLE
        else -> this.visibility = View.GONE
    }
}

@BindingAdapter("cvVisibilityByInt")
fun CardView.setVisibilityByInt(num: Int){
    when(num){
        1 -> this.visibility = View.VISIBLE
        else -> this.visibility = View.GONE
    }
}

@BindingAdapter("rlVisibilityByInt")
fun RelativeLayout.setVisibilityByInt(num: Int){
    when(num){
        0 -> this.visibility = View.GONE
        1 -> this.visibility = View.VISIBLE

    }
}


@BindingAdapter("rlVisibilityByIntR")
fun RelativeLayout.setVisibilityByIntR(num: Int){
    when(num){
        0 -> this.visibility = View.VISIBLE
        1 -> this.visibility = View.GONE
    }
}

@BindingAdapter("cvVisibilityByString")
fun CardView.setVisibilityByInt(str: String?){
    if(str == null || str == ""){
        this.visibility = View.GONE
    }else{
        this.visibility = View.VISIBLE
    }
}
