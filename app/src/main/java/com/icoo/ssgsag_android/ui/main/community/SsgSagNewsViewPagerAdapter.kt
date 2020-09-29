import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.data.model.community.SsgSagNews
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.databinding.ItemCommunitySsgsagNewsBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class SsgSagNewsViewPagerAdapter(
    private val context : Context,
    private var feedList : ArrayList<Feed>,
    private val from : String
) : PagerAdapter() {

    private var items = arrayListOf<ItemCommunitySsgsagNewsBinding>()

    private var mOnItemClickListener: OnItemClickListener? = null
    var newsWidth = 0

    fun replaceAll(list : ArrayList<Feed>){
        feedList = list
        notifyDataSetChanged()
    }

    fun replaceItem(feed : Feed, position: Int){
        feedList[position] = feed

        if(feed.isSave == 0) {
            items[position].itemCommunitySsgsagNewsIvBookmark
                .setImageDrawable(context.getDrawable(R.drawable.ic_bookmark))
        }else{
            items[position].itemCommunitySsgsagNewsIvBookmark
                .setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_big_active))
        }
    }

    fun refreshItem(isSave: Int, position: Int){
        feedList[position].isSave = isSave
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemCommunitySsgsagNewsBinding>(
            LayoutInflater.from(context),
            R.layout.item_community_ssgsag_news, container, false
        )

        if(from == "main"){
            viewDataBinding.itemCommunitySsgsagNewsIvBookmark.visibility = GONE
        }

        if(feedList!= null){
            val feedItem = feedList[position]

            viewDataBinding.feed = feedItem
            viewDataBinding.itemCommunitySsgsagNewsCv.layoutParams.height = (newsWidth * 0.5).toInt()

            viewDataBinding.root.setSafeOnClickListener {
                mOnItemClickListener?.onItemClick(feedItem.feedIdx, feedItem.feedUrl, feedItem.feedName, position)
            }

            viewDataBinding.itemCommunitySsgsagNewsIvBookmark.setOnClickListener {
                mOnItemClickListener?.bookmark(feedItem, position)
            }

        }

        viewDataBinding.root.tag = position
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        items.add(viewDataBinding)
        return viewDataBinding.root

    }

    override fun getCount(): Int {
        if(feedList!= null)
            return feedList.size
        else
            return 0
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun refresh(){
        notifyDataSetChanged()
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
        fun onItemClick(idx : Int, url : String, name : String, position : Int)
        fun bookmark(feed : Feed, position : Int)
    }

}