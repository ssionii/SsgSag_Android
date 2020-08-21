import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.data.model.community.SsgSagNews
import com.icoo.ssgsag_android.databinding.ItemCommunitySsgsagNewsBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class SsgSagNewsViewPagerAdapter(
    private val context : Context,
    private val newsList: ArrayList<SsgSagNews>?
) : PagerAdapter() {

    private var mOnItemClickListener: OnItemClickListener? = null
    var newsWidth = 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemCommunitySsgsagNewsBinding>(
            LayoutInflater.from(context),
            R.layout.item_community_ssgsag_news, container, false
        )

        if(newsList!= null){
            val newsItem = newsList[position]

            viewDataBinding.news = newsItem
            viewDataBinding.itemCommunitySsgsagNewsCv.layoutParams.height = (newsWidth * 0.5).toInt()

            viewDataBinding.root.setSafeOnClickListener {
                mOnItemClickListener?.onItemClick(newsItem.url)
            }

        }

        viewDataBinding.root.tag = position
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        return viewDataBinding.root

    }

    override fun getCount(): Int {
        if(newsList!= null)
            return newsList.size
        else
            return 0
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return (view == (o as View))
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(url : String)
    }

}