package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.member.domain.Member;
import org.springframework.ui.Model;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews();

    Review findById(Long id);

    int addReview(ReviewDTO reviewDTO, Model model);

    List<ReviewTag> addReviewTag(Long id, List<Long> tagIds);

    List<Member> isHiddenMatzip(Long matzipId, List<Member> memberIds);

    int rewardReview(ReviewDTO reviewDTO, int rewardPoint);

    Review deleteReview(Long id);
}
