package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.repository.FoodKindRepository;
import com.lec.spring.matzip.repository.MatzipRepository;
import com.lec.spring.matzip.repository.ReviewRepository;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.FriendRepository;
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
    private final FriendRepository friendRepository;
    private final FoodKindRepository foodKindRepository;

    public ReviewServiceImpl(SqlSession sqlSession, MatzipService matzipService) {
        this.reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        this.matzipRepository = sqlSession.getMapper(MatzipRepository.class);
        this.tagRepository = sqlSession.getMapper(TagRepository.class);
        this.memberRepository = sqlSession.getMapper(MemberRepository.class);
        this.friendRepository = sqlSession.getMapper(FriendRepository.class);
        this.foodKindRepository = sqlSession.getMapper(FoodKindRepository.class);
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

        int saved = reviewRepository.save(reviewDTO, model);

        FoodKind foodKind = addFoodKind(reviewDTO.getKindName());
        List<ReviewTag> addReviewTag = addReviewTags(reviewDTO.getId(), reviewDTO.getTagIds());
        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);
        int rewardReviewPoint = rewardReviewPoint(reviewDTO, 100, 10);
        int rewardReviewIntimacy = rewardReviewIntimacy(reviewDTO, 100, 10);

        model.addAttribute("foodKind", foodKind);
        model.addAttribute("reviewTags", addReviewTag);
        model.addAttribute("isHidden", !hiddenMatzipMemberIds.isEmpty()  ? "UNLOCK" : "saveOk");
        model.addAttribute("members", hiddenMatzipMemberIds);
        model.addAttribute("rewardReviewPoint", rewardReviewPoint);
        model.addAttribute("rewardReviewIntimacy", rewardReviewIntimacy);

        return saved;
    }

    @Override
    public FoodKind addFoodKind(String kindName) {
        FoodKind foodKind = foodKindRepository.findByKindName(kindName);

        if (foodKind == null) {
            throw new IllegalArgumentException("음식 정보를 찾을 수 없습니다.");
        }

        return foodKind;
    }

    @Override
    public List<ReviewTag> addReviewTags(Long id, List<Long> tagIds) {
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
        Long memberId = reviewDTO.getMemberId();

        List<Long> hiddenFriendIds = reviewRepository.checkHiddenMatzip(matzipId, memberId);

        List<Member> hiddenMatzipMembers = new ArrayList<>();
        if (hiddenFriendIds != null && !hiddenFriendIds.isEmpty()) {
            hiddenMatzipMembers = memberRepository.findByIds(hiddenFriendIds);
        }

        return hiddenMatzipMembers;
    }

    @Override
    public int rewardReviewPoint(ReviewDTO reviewDTO, int rewardHiddenPoint, int rewardPoint) {
        Member member = memberRepository.findById(reviewDTO.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);

        int resultPoint = !hiddenMatzipMemberIds.isEmpty() ?  rewardHiddenPoint: rewardPoint;

        member.setPoint(member.getPoint() + resultPoint);
        memberRepository.updatePoint(member.getId(), member.getPoint());

        return member.getPoint();
    }

    @Override
    public int rewardReviewIntimacy(ReviewDTO reviewDTO, int rewardHiddenIntimacy, int rewardIntimacy) {
        List<Friend> friends = friendRepository.findFriendsWithDetailsDTO(reviewDTO.getId());
        if (friends == null) {
            throw new IllegalArgumentException("Friend not found");
        }

        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);

        int resultIntimacy = !hiddenMatzipMemberIds.isEmpty() ? rewardHiddenIntimacy : rewardIntimacy;
        int newIntimacy = 0;

        for(Friend friend : friends) {
            friend.setIntimacy(friend.getIntimacy() + resultIntimacy);
            friendRepository.updateIntimacy(reviewDTO.getId(), friend.getIntimacy());
            newIntimacy = friend.getIntimacy();
        }

        return newIntimacy;
    }


    @Override
    public int deleteReview(Long id) {
        int result = 0;

        Review review = reviewRepository.findById(id);
        if (review == null) {
            throw new IllegalArgumentException("삭제하려는 리뷰를 찾을 수 없습니다.");
        } else {
            result = reviewRepository.deleteById(id);
        }

        return result;
    }
}