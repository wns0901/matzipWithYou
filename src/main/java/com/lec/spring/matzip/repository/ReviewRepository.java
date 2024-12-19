package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.domain.DTO.ReviewDTO;
import com.lec.spring.matzip.domain.DTO.ReviewTagDTO;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.util.List;

@Repository
public interface ReviewRepository {
    List<Review> findAll(Long memberId);

    Review findById(Long id);

    List<ReviewTag> getReviewTags (Long id);

    int save(ReviewDTO reviewDTO, Model model);

    int saveReviewTags(List<ReviewTagDTO> reviewTags);

    List<Long> checkHiddenMatzip(Long matzipId, Long memberId);

    int deleteById(Long id);
}
