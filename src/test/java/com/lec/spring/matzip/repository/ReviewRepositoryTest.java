package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.member.domain.Member;
import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void testFindAllReview() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        List<Review> reviewList = reviewRepository.findAll();
        System.out.println("모든 객체: " + reviewList);
    }
    @Test
    void testSaveReview() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        Review review = Review.builder()
                .content("좋은 맛집입니다")
                .starRating(5)
                .regdate(LocalDateTime.now())
                .build();

        Member member = Member.builder()
                .id(1L)
                .point(100)
                .build();

        Matzip matzip = Matzip.builder()
                .id(1L)
                .kindId(1L)
                .name("더 단백")
                .build();

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .build();


    }

    @Test
    void testCheckHiddenMatzip() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        boolean isHiddenMatzip = reviewRepository.checkHiddenMatzip(1L,2L);

        System.out.println("숨겨진 맛집 여부: " + isHiddenMatzip);
    }

    @Test
    void testRewardReview() {
    }

    @Test
    void testDeleteReview() {
    }
}