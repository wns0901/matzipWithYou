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

    List<Member> hiddenMatzipMemberIds(ReviewDTO reviewDTO);

    int rewardReview(ReviewDTO reviewDTO, int rewardPoint, int rewardIntimacy);

    Review deleteReview(Long id);
}
