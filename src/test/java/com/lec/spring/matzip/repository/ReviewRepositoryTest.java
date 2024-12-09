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

        boolean isHidden = false;
        List<Member> isHiddenMatzip = isHiddenMatzip(matzip.getId(), member.getIds());
        if (!isHiddenMatzip.isEmpty()) {
            isHidden = true;
        }

        String result = isHidden ? "UNLOCK" : "saveOk";
        assertNotNull(result);
        assertTrue(result.equals("UNLOCK") || result.equals("saveOk"));

        int newPoint = result.equals("UNLOCK") ? 610 : 510;
        assertEquals(newPoint, member.getPoint());

    }

    @Test
    public void testAddReviewTag() {
        TagRepository tagRepository = sqlSession.getMapper(TagRepository.class);
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        Review review = reviewRepository.findById(1L);
        List<Long> tagIds = List.of(1L, 2L, 3L);

        List<Tag> tags = tagRepository.findByIds(tagIds);
        assertNotNull(tags);

        List<ReviewTag> reviewTags = tags.stream()
                .map(tag -> ReviewTag.builder()
                        .tagId(tag.getId())
                        .reviewId(review.getId())
                        .regdate(LocalDateTime.now())
                        .build())
                .toList();

        reviewRepository.saveReviewTags(reviewTags);

        List<ReviewTag> testReviewTags = reviewServiceImpl.addReviewTag(review.getId(), tagIds);

        assertNotNull(reviewTags);
    }

    @Test
    void testCheckHiddenMatzip() {
        Long matzipId = 1L;
        Long memberId = 3L;

        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        List<Member> isHiddenMatzip = reviewRepository.checkHiddenMatzip(matzipId, List<Member> memberIds);

        System.out.println("숨겨진 맛집 여부: " + isHiddenMatzip);
        assertNotNull(isHiddenMatzip);
    }

    @Test
    void testRewardReview() {
        MemberRepository memberRepository = sqlSession.getMapper(MemberRepository.class);
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        MatzipRepository matzipRepository = sqlSession.getMapper(MatzipRepository.class);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .matzipId(1L)
                .memberId(3L)
                .build();

        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        Member member = memberRepository.findById(reviewDTO.getMemberId());
        assertNotNull(member.getId(), "Member ID should be generated");

        Integer memberPoint = member.getPoint();

        boolean isHidden = false;
        List<Member> isHiddenMatzip = isHiddenMatzip(matzip.getId(), member.getIds());
        if (!isHiddenMatzip.isEmpty()) {
            isHidden = true;
        }
        int rewardPoint = isHidden ? 100 : 10;

        int newPoint = memberPoint + rewardPoint;

        int result = reviewServiceImpl.rewardReview(reviewDTO, rewardPoint);

        assertEquals(newPoint, result);

        sqlSession.clearCache();
    }

    @Test
    void testDeleteReview() {
    }
}