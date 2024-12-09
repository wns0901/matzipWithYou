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

//        boolean isHidden = false;
//        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(matzip.getId(), member.getId());
//        if (!hiddenMatzipMemberIds.isEmpty()) {
//            isHidden = true;
//        }
//        model.addAttribute("result", isHidden ? "UNLOCK" : "saveOk");
//        model.addAttribute("member", member);
//
//        int rewardPoint = isHidden ? 100 : 10;
//        rewardReview(reviewDTO, rewardPoint);

        return saved;
    }

    @Override
    public List<ReviewTag> addReviewTag(Long id, List<Long> tagIds) {
        List<Tag> tags = tagRepository.findByIds(tagIds);

        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("태그 정보를 찾을 수 없습니다.");
        }

        List<ReviewTag> reviewTags = tags.stream()
                .map(tag -> ReviewTag.builder()
                        .tagId(tag.getId())
                        .reviewId(id)
                        .regdate(LocalDateTime.now())
                        .build())
                .toList();

        reviewRepository.saveReviewTags(reviewTags);

        return reviewTags;
    }

    @Override
    public List<Member> hiddenMatzipMemberIds(ReviewDTO reviewDTO) {
        Long matzipId = reviewDTO.getMatzipId();
        List <Long> hiddenMemberIds = reviewDTO.getMemberIds();

        List<Member> hiddenMembers = new ArrayList<>();

        for(Long hiddenMember : hiddenMemberIds) {
            int countMember = reviewRepository.checkHiddenMatzip(matzipId, hiddenMember);
            if (countMember > 0) {
                hiddenMembers.add(memberRepository.findById(hiddenMember));
            }
        }

        return hiddenMembers;
    }

    @Override
    public int rewardReview(ReviewDTO reviewDTO, int rewardPoint, int rewardIntimacy) {
        Member member = memberRepository.findById(reviewDTO.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        if (matzip == null) {
            throw new IllegalArgumentException("Matzip not found");
        }

        member.setPoint(member.getPoint() + rewardPoint);
        memberRepository.updatePoint(member.getId(), member.getPoint());

        return member.getPoint();
    }

    @Override
    public Review deleteReview(Long id) {
        return reviewRepository.deleteById(id);
    }
}
