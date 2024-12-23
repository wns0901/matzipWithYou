package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.domain.DTO.ReviewDTO;
import com.lec.spring.matzip.domain.DTO.ReviewSubmitModalDTO;
import com.lec.spring.matzip.domain.DTO.ReviewTagDTO;
import com.lec.spring.member.domain.Member;
import org.springframework.ui.Model;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviews(Long memberId);

    Review findById(Long id);

    int addReview(ReviewDTO reviewDTO);

    String addContent(Long id, String content);

    List<String> getFoodKinds();

    String getKindName(Long id);

    FoodKind addFoodKind(String kindName);

    String getMatzipName(Long matzipId);

    String getMatzipAddress(Long matzipId);

    String getKakaoImgURl(Long matzipId);

    List<Tag> getTags();

    List<ReviewTag> getReviewTags(Long id);

    List<String> getReviewTagNames(Long id);

    List<ReviewTag> addReviewTags(Long reviewId, List<Long> tagIds);

    List<Member> hiddenMatzipMemberIds(ReviewDTO reviewDTO);

    int rewardReviewPoint(ReviewDTO reviewDTO, int rewardHiddenPoint, int rewardPoint);

    int rewardReviewIntimacy(ReviewDTO reviewDTO, int rewardHiddenIntimacy ,int rewardIntimacy);

    ReviewSubmitModalDTO getModalInfo(ReviewDTO reviewDTO);

    Long getAuthenticatedMemberId();

    int deleteReview(Long id);
}
