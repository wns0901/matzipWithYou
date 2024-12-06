package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.repository.MatzipRepository;
import com.lec.spring.matzip.repository.ReviewRepository;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.MemberRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MatzipRepository matzipRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;

    public ReviewServiceImpl(SqlSession sqlSession, MatzipService matzipService) {
        this.reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        this.matzipRepository = sqlSession.getMapper(MatzipRepository.class);
        this.tagRepository = sqlSession.getMapper(TagRepository.class);
        this.memberRepository = sqlSession.getMapper(MemberRepository.class);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public int addReview(ReviewDTO reviewDTO, Model model) {
        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        if (matzip == null) {
            throw new IllegalArgumentException("음식점 정보를 찾을 수 없습니다");
        }

        Review review = reviewDTO;

        int saved = reviewRepository.save(reviewDTO, model);

        Member member = memberRepository.findById(reviewDTO.getMemberId());

        boolean isHidden = isHiddenMatzip(matzip.getId(), member.getId());
        model.addAttribute("result", isHidden ? "UNLOCK" : "saveOk");

        int rewardPoint = isHidden ? 100 : 10;
        rewardReview(reviewDTO, rewardPoint);

        return saved;
    }

    @Override
    public List<ReviewTag> addReviewTag(List<Long> tagIds, Long reviewId) {

        Review review = reviewRepository.findById(reviewId);
        List<Tag> tags = tagRepository.findByIds(tagIds);

        List<ReviewTag> reviewTags = tags.stream()
            .map(tag -> ReviewTag.builder()
                    .review(review)
                    .tag(tag)
                    .regdate(LocalDateTime.now())
                    .build())
                .toList();

        reviewRepository.saveReviewTags(reviewTags);

        return reviewTags;
    }

    @Override
    public Boolean isHiddenMatzip(Long matzipId, Long memberId) {
        return reviewRepository.checkHiddenMatzip(matzipId, memberId);
    }

    @Override
    public int rewardReview(ReviewDTO reviewDTO, int rewardPoint) {
        Member member = memberRepository.findById(reviewDTO.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        if (matzip == null) {
            throw new IllegalArgumentException("Matzip not found");
        }

        boolean isHiddenMatzip = isHiddenMatzip(matzip.getId(), member.getId());
        rewardPoint = isHiddenMatzip ? 100 : 10;

        member.setPoint(member.getPoint() + rewardPoint);
        memberRepository.updatePoint(member.getId(), member.getPoint());

        return member.getPoint();
    }

    @Override
    public Review deleteReview(Long id) {
        return reviewRepository.deleteById(id);
    }
}
