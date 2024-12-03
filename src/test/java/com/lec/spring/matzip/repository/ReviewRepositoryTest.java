package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Review;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.member.domain.Member;
import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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

        Review review = new Review();
        review.setContent("좋은 맛집입니다");
        review.setStarRating(5);

        Member member = new Member();
        member.setId(1L);
        review.setMember(member);

        FoodKind foodKind = new FoodKind();
        foodKind.setId(1L);

        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tags.add(tag1);

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tags.add(tag2);

        Tag tag3 = new Tag();
        tag3.setId(3L);
        tags.add(tag3);

        int result = reviewRepository.save(review, tags, foodKind);
        System.out.println("저장된 리뷰 ID: " + review.getId());
        System.out.println("결과: " + result);
    }

    @Test
    void testCheckHiddenMatzip() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        Review review = new Review();
        Member member = new Member();

        member.setId(1L);
        review.setMember(member);
        review.getMatzip().setId(1L);
    }

    @Test
    void testRewardReview() {
    }

    @Test
    void testDeleteReview() {
    }
}