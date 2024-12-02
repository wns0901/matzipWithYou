package com.lec.spring.member.repository;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Review;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.member.domain.Member;

import java.util.List;

public interface ReviewRepository {
    List<Review> findAll();

    int save(Review review, List<Tag> tags, FoodKind foodKind);

    boolean checkHiddenMatzip(Review review, Member member);

    int reward(Review review, Member member);

    int deleteById(Long id);

    int findById(Long id);
}
