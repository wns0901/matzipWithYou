package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Review;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.repository.ReviewRepository;
import com.lec.spring.member.domain.Member;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(SqlSession sqlSession) {
        this.reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        System.out.println("ReviewService() 생성");
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public int addReview(Review review, List<Tag> tags, FoodKind foodKind) {
        return reviewRepository.save(review, tags, foodKind);
    }

    @Override
    public boolean isHiddenMatzip(MyMatzip myMatzip, Friend friend) {
        return reviewRepository.checkHiddenMatzip(myMatzip, friend);
    }

    @Override
    public int rewardReview(Review review, Member member) {
        return reviewRepository.rewardHidden(review, member);
    }

    @Override
    public int deleteReview(Long id) {
        return reviewRepository.deleteById(id);
    }
}
