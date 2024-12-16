package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.service.ReviewServiceImpl;
import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.MemberRepository;
import com.lec.spring.member.service.MemberServiceImpl;
import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private ReviewServiceImpl reviewServiceImpl;

    @Test
    void testFindAllReview() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        List<Review> reviewList = reviewRepository.findAll();
        System.out.println("모든 객체: " + reviewList);
        assertNotNull(reviewList);
        assertFalse(reviewList.isEmpty());
    }

    @Test
    void testSaveReview() {
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        MemberRepository memberRepository = sqlSession.getMapper(MemberRepository.class);
        MatzipRepository matzipRepository = sqlSession.getMapper(MatzipRepository.class);
        List<Long> memberIds = List.of(2L, 3L, 4L);
        List<Long> tagIds = List.of(1L, 2L, 3L);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .matzipId(6L)
                .memberId(2L)
                .memberIds(memberIds)
                .content("좋은 맛집입니다")
                .tagIds(tagIds)
                .kindName("일식")
                .starRating(5)
                .regdate(LocalDateTime.now())
                .build();

        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        if (matzip == null) {
            throw new IllegalArgumentException("음식점 정보를 찾을 수 없습니다");
        }

        Model model = new ExtendedModelMap();

        Member member = memberRepository.findById(reviewDTO.getMemberId());
        assertNotNull(member);

        int saved = reviewRepository.save(reviewDTO, model);
        assertEquals(1, saved);

        FoodKind foodKind = reviewServiceImpl.addFoodKind(reviewDTO.getKindName());
        List<Member> hiddenMatzipMemberIds = reviewServiceImpl.hiddenMatzipMemberIds(reviewDTO);
        int rewardReviewPoint = reviewServiceImpl.rewardReviewPoint(reviewDTO, 100, 10);
        int rewardReviewIntimacy = reviewServiceImpl.rewardReviewIntimacy(reviewDTO, 100, 10);

        model.addAttribute("foodKind", foodKind);
        model.addAttribute("isHidden", !hiddenMatzipMemberIds.isEmpty()  ? "UNLOCK" : "saveOk");
        model.addAttribute("members", hiddenMatzipMemberIds);
        model.addAttribute("rewardReviewPoint", rewardReviewPoint);
        model.addAttribute("rewardReviewIntimacy", rewardReviewIntimacy);

        String result = (String) model.getAttribute("isHidden");
        assertNotNull(result);
        assertTrue(result.equals("UNLOCK") || result.equals("saveOk"));
    }

    @Test
    public void testAddFoodKind() {
        FoodKindRepository foodKindRepository = sqlSession.getMapper(FoodKindRepository.class);
        String kindName = "일식";

        FoodKind foodtag = foodKindRepository.findByKindName(kindName);

        assertEquals(kindName, foodtag.getKindName());
    }

    @Test
    public void testAddReviewTag() {
        TagRepository tagRepository = sqlSession.getMapper(TagRepository.class);
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);
        Review review = reviewRepository.findById(1L);
        List<Long> tagIds = List.of(1L, 2L, 3L);

        List<Tag> tags = tagRepository.findByIds(tagIds);
        assertNotNull(tags);


    }

    @Test
    void testCheckHiddenMatzip() {
        Long matzipId = 6L;
        Long memberId = 2L;

        MemberRepository memberRepository = sqlSession.getMapper(MemberRepository.class);
        ReviewRepository reviewRepository = sqlSession.getMapper(ReviewRepository.class);

        List<Long> hiddenFriendIds = reviewRepository.checkHiddenMatzip(matzipId, memberId);

        List<Member> hiddenMatzipMembers = new ArrayList<>();
        if (hiddenFriendIds != null && !hiddenFriendIds.isEmpty()) {
            hiddenMatzipMembers = memberRepository.findByIds(hiddenFriendIds);
        }

        System.out.println("숨겨진 맛집의 해당 친구목록: " + hiddenMatzipMembers);
    }

    @Test
    void testRewardReviewPoint() {
        MemberRepository memberRepository = sqlSession.getMapper(MemberRepository.class);

        List<Long> memberIds = List.of(2L, 3L, 4L);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .matzipId(6L)
                .memberId(2L)
                .memberIds(memberIds)
                .build();

        Member member = memberRepository.findById(reviewDTO.getMemberId());

        List<Member> hiddenMatzipMemberIds = reviewServiceImpl.hiddenMatzipMemberIds(reviewDTO);

        Integer memberPoint = member.getPoint();

        int rewardHiddenPoint = 100;
        int rewardPoint = 10;

        int resultPoint = !hiddenMatzipMemberIds.isEmpty() ? rewardHiddenPoint : rewardPoint;

        int newPoint = memberPoint + resultPoint;

        int result = reviewServiceImpl.rewardReviewPoint(reviewDTO, rewardHiddenPoint ,rewardPoint);

        assertEquals(newPoint, result);

        sqlSession.clearCache();
    }

    @Test
    void testRewardReviewIntimacy() {
        FriendRepository friendRepository = sqlSession.getMapper(FriendRepository.class);

        List<Long> memberIds = List.of(2L, 3L, 4L);

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .matzipId(6L)
                .memberId(2L)
                .memberIds(memberIds)
                .build();

        int rewardHiddenIntimacy = 100;
        int rewardIntimacy = 10;

        List<Member> hiddenMatzipMemberIds = reviewServiceImpl.hiddenMatzipMemberIds(reviewDTO);

        int resultIntimacy = !hiddenMatzipMemberIds.isEmpty() ? rewardHiddenIntimacy : rewardIntimacy;

        int newIntimacy = 0;

        List<FriendDetailsDTO> friends = friendRepository.findFriendsWithDetailsDTO(reviewDTO.getId());
        if (friends == null) {
            throw new IllegalArgumentException("Friend not found");
        }

        for(FriendDetailsDTO friend : friends) {
            friend.setIntimacy(friend.getIntimacy() + resultIntimacy);
            friendRepository.updateIntimacy(reviewDTO.getId(), friend.getIntimacy());
            newIntimacy = friend.getIntimacy();
        }

        int result = reviewServiceImpl.rewardReviewIntimacy(reviewDTO, rewardHiddenIntimacy ,rewardIntimacy);

        assertEquals(newIntimacy, result);

        sqlSession.clearCache();
    }

    @Test
    void testDeleteReview() {
    }
}