package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.service.ReviewServiceImpl;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.MemberRepository;
import com.lec.spring.member.service.MemberServiceImpl;
import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private ReviewServiceImpl reviewServiceImpl;
    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Test
    void testFindAllReview() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        List<Review> reviewList = reviewRepository.findAll();
        System.out.println("모든 객체: " + reviewList);
        assertNotNull(reviewList);
        assertFalse(reviewList.isEmpty());
    }

    @Test
    void testSaveReview() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        MemberRepository memberRepository = sqlSession.getMapper(MemberRepository.class);
        MatzipRepository matzipRepository = sqlSession.getMapper(MatzipRepository.class);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .matzipId(1L)
                .memberId(3L)
                .content("좋은 맛집입니다")
                .starRating(5)
                .regdate(LocalDateTime.now())
                .build();

        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        if (matzip == null) {
            throw new IllegalArgumentException("음식점 정보를 찾을 수 없습니다");
        }

        Model model = new ExtendedModelMap();

        int saved = reviewRepository.save(reviewDTO, model);

        assertEquals(1, saved);


        Member member = memberRepository.findById(reviewDTO.getMemberId());
        assertNotNull(member);

        boolean isHiddenMatzip = reviewRepository.checkHiddenMatzip(matzip.getId(), member.getId());

        String result = isHiddenMatzip ? "UNLOCK" : "saveOk";
        assertNotNull(result);
        assertTrue(result.equals("UNLOCK") || result.equals("saveOk"));

        int newPoint = result.equals("UNLOCK") ? 610 : 510;
        assertEquals(newPoint, member.getPoint());

    }

    @Test
    void testCheckHiddenMatzip() {
        Long matzipId = 1L;
        Long memberId = 3L;

        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        boolean isHiddenMatzip = reviewRepository.checkHiddenMatzip(matzipId, memberId);

        System.out.println("숨겨진 맛집 여부: " + isHiddenMatzip);
        assertNotNull(isHiddenMatzip);
    }

    @Test
    void testRewardReview() {
        MemberRepository memberRepository = sqlSession.getMapper(MemberRepository.class);
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        Long matzipId = 1L;

        Member member = memberRepository.findById(3L);
        assertNotNull(member.getId(), "Member ID should be generated");

        Integer memberPoint = member.getPoint();

        boolean isHiddenMatzip = reviewRepository.checkHiddenMatzip(matzipId, member.getId());
        int rewardPoint = isHiddenMatzip ? 100 : 10;

        int newPoint = memberPoint + rewardPoint;

        memberRepository.updatePoint(member.getId(), rewardPoint);

        int result = reviewServiceImpl.rewardReview(member.getId(), member.getPoint(), rewardPoint);

        assertEquals(1, result);

        assertEquals(newPoint, member.getPoint());

        sqlSession.clearCache();
    }

    @Test
    void testDeleteReview() {
    }
}