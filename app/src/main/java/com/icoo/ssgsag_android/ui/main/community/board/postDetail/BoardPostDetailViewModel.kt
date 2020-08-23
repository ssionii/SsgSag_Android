package com.icoo.ssgsag_android.ui.main.community.board.postDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail

class BoardPostDetailViewModel(

) : BaseViewModel(){

    val dummyDetail = BoardPostDetail(1, "공모전/대외활동", 1, "고민이 있읍니다,,,,", "[청년창업네트워크 프리즘 x 한양대학교x 에스큐빅엔젤스]\n" +
            "액셀러레이팅에다가 최대 투자 1억까지? \n" +
            "유니콘을 위한 최고의 선택\n" +
            "\" 2020 한양대학교 Value up Launch Pad - 투자형 엑셀러레이팅 (최대 1억원 까지)\"\n" +
            "\n" +
            " 한양대학교 창업지원단과 S-cubic 엔젤스가 함께 하는 당신의 스타트업을 로켓으로 만들 최고의 엑셀러레이팅 프로그램!\n" +
            "테이블 매니저, 하얀마인드 등 로켓처럼 성장하려고 하는 초기 스타트업을 초대합니다.\n" +
            "\n" +
            "* 참여대상: 최대 20억 밸류의 시드 투자 유치를 원하는 창업 3년 이내의 초기 창업기업 또는 팀 \n" +
            "* 진행내용: 3개월간 마일스톤 설정, 2주마다 6회 스프린트 점검 \n" +
            "* 신청 마감: 7월 16일(목) 24:00 \n" +
            " * 참여혜택: 선발팀중 1팀이상 최소3천만원 ~ 최대1억원투자 \n" +
            "* 신청링크: https://event-us.kr/prismnetwork/event/19507\n" +
            "\n" +
            "※문의: 한양대학교 창업지원단 김미연 매니저 \n" +
            " 02-2220-285122", 3, 32, "3일 전", "https://i.pinimg.com/564x/af/7a/aa/af7aaae1985221ec5155da6c42ed1985.jpg",1, "싀오니", "http://img.segye.com/content/image/2019/10/31/20191031513063.jpg", 20)


    private var _postDetail = MutableLiveData<BoardPostDetail>()
    val postDetail : LiveData<BoardPostDetail> = _postDetail

    init {
        getPostDetail()
    }

    fun getPostDetail(){
        _postDetail.value = dummyDetail
    }

}