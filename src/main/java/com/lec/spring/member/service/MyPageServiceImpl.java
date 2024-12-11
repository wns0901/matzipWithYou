package com.lec.spring.member.service;

import com.lec.spring.member.domain.MyPage;
import com.lec.spring.member.repository.MyPageRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MyPageServiceImpl implements MyPageService {
    private MyPageRepository myPageRepository;

    public MyPageServiceImpl(SqlSession sqlSession) {
        this.myPageRepository = sqlSession.getMapper(MyPageRepository.class);
    }

    // 닉네임, 친구수, 포인트
    @Override
    @Transactional(readOnly = true)
    public MyPage getMyProfile(MyPage mypage) {
        return myPageRepository.myProfile(mypage);
    }

    // 위시리스트 & 내맛집 & 내리뷰 개수
    @Override
    @Transactional(readOnly = true)
    public MyPage myActsCount(MyPage mypage) {
        return myPageRepository.myActsCount(mypage);
    }



    // 위시 미리보기
    @Override
    @Transactional(readOnly = true)
    public MyPage getWishContents(MyPage mypage) {
        List<MyPage.WishItem> wishList = myPageRepository.wishContents(mypage);
        mypage.setWishList(wishList);
        return mypage;
    }





    // 닉네임 변경
    @Override
    public int updateNick(Long memberId) {
        return "";
    }




//    public int getFriendCnt(Long memberId) {
//        return friendRepository.CntBySenderIdOrReceiverId(memberId);
//    }
//
//    public int getMatzipCnt(Long memberId) {
//        return matzipRepository.;
//    }
//
//    public String updateNick(Long memberId, String newNickname) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException(""));
//        member.setNickname(newNickname);
//        memberRepository.save(member);
//    }
//
//    public void updateProfileimage(Long memberId, String imagePath) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException(""));
//        member.setProfileImagePath(imagePath);
//        memberRepository.save(member);
//    }
}
