package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.member.domain.Member;
import org.springframework.ui.Model;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews();

    Review findById(Long id);

    int addReview(ReviewDTO reviewDTO, Model model);

    List<ReviewTag> addReviewTag(List<Long> tagIds, Long reviewId);

    Boolean isHiddenMatzip(Long matzipId, Long memberId);

    int rewardReview(ReviewDTO reviewDTO, int rewardPoint);

    Review deleteReview(Long id);
}
