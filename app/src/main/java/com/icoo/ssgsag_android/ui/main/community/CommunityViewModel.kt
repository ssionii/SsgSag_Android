package com.icoo.ssgsag_android.ui.main.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.*
import com.icoo.ssgsag_android.data.model.poster.allPoster.AdPosterCollection

class CommunityViewModel : BaseViewModel(){


    val ssgSagNewsDummy = arrayListOf(
        SsgSagNews(0, "https://img.huffingtonpost.com/asset/5db8ec22200000823350729a.jpeg?cache=3KPGTkF35i&ops=crop_0_53_1782_1254,scalefit_630_noupscale", "원호 신곡 발표! 글이 이렇게 길어진다 왈라라라ㅏ라라ㅏㅏ라ㅏ라ㅏㅏ호롤릴리랄라라라 신난다아아ㅏㅇㅇ", ""),
        SsgSagNews(1, "https://img1.daumcdn.net/thumb/S560x400/?scode=1boon&fname=https://t1.daumcdn.net/liveboard/benter/3f799561fe184bec87f14d39fa000448.jpeg", "캡틴 코리아 원호", "")
    )

    val collectionDummy = CommunityMainCollection(
        arrayListOf(RecruitTeamMain(0, "디자인 공모전 나가실 디자인과 구해요!", "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg", 1, "서울", "공모전"),
            RecruitTeamMain(1, "신도림 모각공 구해용", "https://file.mk.co.kr/meet/neds/2018/08/image_readtop_2018_534333_15351842733435488.jpg", 1, "서울", "모여서 각자")),
        arrayListOf(BoardMain(0, "취업/진로", "1시간 전", "제목입니당", "글 본문내용입니다. 아유 힘들다 힘들어 돈벌어 돈~", "https://file.mk.co.kr/meet/neds/2018/08/image_readtop_2018_534333_15351842733435488.jpg"),
        BoardMain(1, "기타", "1시간 전", "제목입니당2", "아유 힘들다 힘들어 돈벌어 돈~", "https://file.mk.co.kr/meet/neds/2018/08/image_readtop_2018_534333_15351842733435488.jpg")),
        arrayListOf(BoardMain(0, "자유게시판", "1시간 전", "자게란 이런것인가,,", "글 본문내용입니다. 아유 힘들다 힘들어 돈벌어 돈~", "https://file.mk.co.kr/meet/neds/2018/08/image_readtop_2018_534333_15351842733435488.jpg"),
            BoardMain(1, "기타", "1시간 전", "제목입니당2", "아유 힘들다 힘들어 돈벌어 돈~", "https://file.mk.co.kr/meet/neds/2018/08/image_readtop_2018_534333_15351842733435488.jpg")),
        arrayListOf(ReviewMain(0, "연합동아리", "SOPT", "솝트 짱~", 4.3f, "개발, 디자인, 기획 파트로 나누어져 있어서 협업 경험하는데 큰 도움 됨. 특히 방학에 앱잼이라는 3주짜리 해커톤을 하는데 너무 맘에 들었음 ㅎㅎ IT쪽에 충분히 관심있고 개발 욕심있다면 짱 추천","ssionii", "2019년 활동"),
            ReviewMain(0, "연합동아리", "SOPT", "솝트,, 호로롤리릴", 4.3f, "개발, 디자인, 기획 파트로 나누어져 있어서 협업 경험하는데 큰 도움 됨. 특히 방학에 앱잼이라는 3주짜리 해커톤을 하는데 너무 맘에 들었음 ㅎㅎ IT쪽에 충분히 관심있고 개발 욕심있다면 짱 추천","ssionii", "2019년 활동")))

    private var _ssgSagNewsList = MutableLiveData<ArrayList<SsgSagNews>>()
    val ssgSagNewsList : LiveData<ArrayList<SsgSagNews>> = _ssgSagNewsList

    private var _collectionItem = MutableLiveData<CommunityMainCollection>()
    val collectionItem : LiveData<CommunityMainCollection> = _collectionItem

    init {
        getSsgSagDummyList()
        getCollectionDummy()
    }

    fun getSsgSagDummyList(){
        _ssgSagNewsList.postValue(ssgSagNewsDummy)
    }

    fun getCollectionDummy(){
        _collectionItem.postValue(collectionDummy)
    }
}