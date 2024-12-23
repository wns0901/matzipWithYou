package com.lec.spring.matzip.service;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.domain.DTO.ReviewDTO;
import com.lec.spring.matzip.domain.DTO.ReviewSubmitModalDTO;
import com.lec.spring.matzip.domain.DTO.ReviewTagDTO;
import com.lec.spring.matzip.repository.FoodKindRepository;
import com.lec.spring.matzip.repository.MatzipRepository;
import com.lec.spring.matzip.repository.ReviewRepository;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.MemberRepository;
import com.lec.spring.member.service.FriendService;
import com.lec.spring.member.service.FriendServiceImpl;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Review> getAllReviews(Long memberId) {
        List<Review> reviews = reviewRepository.findAll(memberId);
        return reviews != null ? reviews : new ArrayList<>();
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public int addReview(ReviewDTO reviewDTO) {
        Matzip matzip = matzipRepository.findById(reviewDTO.getMatzipId());
        if (matzip == null) {
            throw new IllegalArgumentException("음식점 정보를 찾을 수 없습니다");
        }
        reviewDTO.setRegdate(LocalDateTime.now());

        int saved = reviewRepository.save(reviewDTO);
        addReviewTags(reviewDTO.getId(), reviewDTO.getTagIds());

        return saved;
    }

    @Override
    public Long getAuthenticatedMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            Long memberId = principalDetails.getMember().getId();
            System.out.println("##########memberId:" + memberId);
            return memberId;
        }

        throw new IllegalStateException("로그인된 사용자 정보가 없습니다.");
    }

    @Override
    public List<String> getFoodKinds() {
        return foodKindRepository.findAll().stream()
                .map(FoodKind::getKindName)
                .collect(Collectors.toList());
    }

    @Override
    public String getKindName(Long id) {
        Review review = reviewRepository.findById(id);
        Matzip matzip = matzipRepository.findById(review.getMatzipId());
        FoodKind foodKind = foodKindRepository.findByKindId(matzip.getKindId());
        return foodKind.getKindName();
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
    public String getMatzipName(Long matzipId) {
        Matzip matzip = matzipRepository.findById(matzipId);
        return matzip.getName();
    }

    @Override
    public String getMatzipAddress(Long matzipId) {
        Matzip matzip = matzipRepository.findById(matzipId);
        return matzip.getAddress();
    }

    @Override
    public String getKakaoImgURl(Long matzipId) {
        Matzip matzip = matzipRepository.findById(matzipId);
        return matzip.getImgUrl();
    }

    @Override
    public String addContent(Long id, String content) {
        Review review = reviewRepository.findById(id);
        if (review != null) {
            review.setContent(content);
        } else {
            return "Review not found";
        }
        return content;
    }

    @Override
    public List<Tag> getTags() {
       return tagRepository.findAll().stream()
               .map(tag -> new Tag(tag.getId(), tag.getTagName(), tag.getRegdate()))
               .collect(Collectors.toList());
    }

    @Override
    public List<ReviewTag> getReviewTags(Long id) {
        return reviewRepository.getReviewTags(id).stream()
                .map(reviewTag -> new ReviewTag(reviewTag.getRegdate(), reviewTag.getTagId(), reviewTag.getReviewId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getReviewTagNames(Long id) {
        List<ReviewTag> reviewTags = reviewRepository.getReviewTags(id)
                .stream()
                .map(reviewTag -> new ReviewTag(reviewTag.getRegdate(), reviewTag.getTagId(), reviewTag.getReviewId()))
                .collect(Collectors.toList());

        List<String> reviewTagNames = new ArrayList<>();

        for (ReviewTag reviewTag : reviewTags) {
            Tag tag = tagRepository.findById(reviewTag.getTagId());

            reviewTagNames.add(tag.getTagName());
        }
        return reviewTagNames;
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
                .collect(Collectors.toList());

        reviewRepository.saveReviewTags(reviewTags);

        return reviewTags;
    }

    @Override
    public List<Member> hiddenMatzipMemberIds(ReviewDTO reviewDTO) {
        Long matzipId = reviewDTO.getMatzipId();
        Long memberId = reviewDTO.getMemberId();

        List<Long> hiddenFriendIds = reviewRepository.checkHiddenMatzip(matzipId, memberId);

        if (hiddenFriendIds == null || hiddenFriendIds.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }

        List<Member> hiddenMatzipMembers = memberRepository.findByIds(hiddenFriendIds);
        return hiddenMatzipMembers != null ? hiddenMatzipMembers : new ArrayList<>();
    }

    @Override
    public int rewardReviewPoint(ReviewDTO reviewDTO, int rewardHiddenPoint, int rewardPoint) {
        Member member = memberRepository.findById(reviewDTO.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);
        int resultPoint = !hiddenMatzipMemberIds.isEmpty() ?  rewardHiddenPoint : rewardPoint;

        int currentPoint = member.getPoint();
        int newPoint = currentPoint + resultPoint;

        member.setPoint(newPoint);
        memberRepository.updatePoint(member.getId(), member.getPoint());

        return resultPoint;
    }

    @Override
    public int rewardReviewIntimacy(ReviewDTO reviewDTO, int rewardHiddenIntimacy, int rewardIntimacy) {
        List<FriendDetailsDTO> friends = friendRepository.findFriendsWithDetailsDTO(reviewDTO.getMemberId());
        if (friends == null || friends.isEmpty()) {
            return 0;
        }

        List<Member> hiddenMatzipMemberIds = hiddenMatzipMemberIds(reviewDTO);

        int resultIntimacy = !hiddenMatzipMemberIds.isEmpty() ? rewardHiddenIntimacy : rewardIntimacy;

        for(FriendDetailsDTO friend : friends) {
            friend.setIntimacy(friend.getIntimacy() + resultIntimacy);
            friendRepository.updateIntimacy(reviewDTO.getMemberId(), friend.getFriendId(),friend.getIntimacy());
        }

        return resultIntimacy;
    }

    @Override
    public ReviewSubmitModalDTO getModalInfo(ReviewDTO reviewDTO) {
        List<Member> hiddenMatzipMembers = hiddenMatzipMemberIds(reviewDTO);

        int pointIncrease = rewardReviewPoint(reviewDTO, 500, 100);
        int intimacyIncrease = rewardReviewIntimacy(reviewDTO, 100, 0);

        List<FriendDetailsDTO> friendDetails = friendRepository.findFriendsWithDetailsDTO(reviewDTO.getMemberId());

        if (hiddenMatzipMembers == null && hiddenMatzipMembers.isEmpty()) {
            return ReviewSubmitModalDTO.builder()
                    .hiddenFriends(new ArrayList<>())
                    .topFriendName(null)
                    .friendCount(0)
                    .intimacyIncrease(intimacyIncrease)
                    .rewardPoints(pointIncrease)
                    .build();
        } else {
            List<FriendDetailsDTO> hiddenFriendProfiles = friendDetails.stream()
                    .filter(friend -> hiddenMatzipMembers.stream()
                            .anyMatch(member -> member.getId().equals(friend.getFriendId())))
                    .map(friend -> FriendDetailsDTO.builder()
                            .friendId(friend.getFriendId())
                            .profileImg(friend.getProfileImg())
                            .nickname(friend.getNickname())
                            .username(friend.getUsername())
                            .intimacy(friend.getIntimacy())
                            .publicCount(friend.getPublicCount())
                            .hiddenCount(friend.getHiddenCount())
                            .build())
                    .distinct()
                    .collect(Collectors.toList());

            FriendDetailsDTO topFriend = hiddenFriendProfiles.stream()
                    .max(Comparator.comparingInt(FriendDetailsDTO::getIntimacy))
                    .orElse(null);

            return ReviewSubmitModalDTO.builder()
                    .hiddenFriends(hiddenFriendProfiles)
                    .topFriendName(topFriend != null ? topFriend.getNickname() : null)
                    .friendCount(hiddenFriendProfiles.isEmpty() ? 0 : hiddenFriendProfiles.size() - 1)
                    .intimacyIncrease(intimacyIncrease)
                    .rewardPoints(pointIncrease)
                    .build();
        }
    }


    @Override
    public int deleteReview(Long id) {
        Review review = reviewRepository.findById(id);
        if (review == null) {
            throw new IllegalArgumentException("삭제하려는 리뷰를 찾을 수 없습니다.");
        }

        reviewRepository.deleteReviewTags(id);
        return reviewRepository.deleteById(id);
    }
}