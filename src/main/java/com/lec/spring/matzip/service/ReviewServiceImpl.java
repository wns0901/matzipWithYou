package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Review;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.member.domain.Member;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    @Override
    public List<Review> getAllReviews() {
        return List.of();
    }

    @Override
    public int addReview(Review review, List<Tag> tags, FoodKind foodKind) {
        return 0;
    }

    @Override
    public boolean isHiddenMatzip(Review review, Member member) {
        return false;
    }

    @Override
    public int rewardReview(Review review, Member member) {
        return 0;
    }

    @Override
    public int deleteReview(Long id) {
        return 0;
    }
}
