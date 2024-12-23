package com.lec.spring.member.service;

import com.lec.spring.member.domain.*;
import com.lec.spring.member.repository.FriendRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional

public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;

    public FriendServiceImpl(SqlSession sqlSession) {
        this.friendRepository = sqlSession.getMapper(FriendRepository.class);
    }

    // 친구 요청 보내기
    @Override
    public int sendFriendRequest(Friend friend){

        // 중복 요청 확인
        if (friendRepository.isAlreadyFriend(friend)) { // ?
            return 0;
        }
        return friendRepository.sendFriendRequest(friend); // DB에 저장
    }

    // 친구 요청 수락/거절
    @Override
    public int respondToRequest(Friend friend) {
        if (friend.getIsAccept()) {
            // 요청 수락인 경우
            return friendRepository.acceptFriendRequest(friend);
        } else {
            // 요청 거절인 경우
            return friendRepository.rejectFriendRequest(friend);
        }
    }

    // 친구 삭제
    @Override
    public int deleteFriend(Long friendId, Long memberId) {
        return friendRepository.deleteFriend(friendId, memberId);
    }

    // 친구 목록(상세정보) 가져오기
    @Override
    public List<FriendDetailsDTO> getFriendsWithDetailsDTO(Long memberId) {
        return friendRepository.findFriendsWithDetailsDTO(memberId);
        // 친밀도 순으로 정렬 후 상위 3명 추출
//        List<FriendDetailsDTO> top3Friends = friends.stream()
//                .sorted(Comparator.comparing(FriendDetailsDTO::getIntimacy).reversed()) // 친밀도 내림차순
//                .limit(3)
//                .collect(Collectors.toList());
//
//        return top3Friends;
    }





    @Override
    public List<FriendRequestDTO> getPendingRequests(Long memberId) {
        List<Friend> pendingFriends = friendRepository.findPendingRequests(memberId);

        return pendingFriends.stream()
                .map(friend -> {
                    FriendRequestDTO dto = new FriendRequestDTO();
                    FriendDetailsDTO details = friend.getFriendDetails();

                    dto.setSenderId(friend.getSenderId());
                    dto.setReceiverId(friend.getReceiverId());
                    dto.setIsAccept(friend.getIsAccept());
                    dto.setNickname(details.getNickname());
                    dto.setUsername(details.getUsername());
                    dto.setPublicCount(details.getPublicCount());
                    dto.setHiddenCount(details.getHiddenCount());
                    dto.setIntimacy(friend.getIntimacy());
                    dto.setProfileImg(details.getProfileImg() != null ?
                            "/upload/" + details.getProfileImg() :
                            "/images/default-profile.png");
                    dto.setRegdate(friend.getRegdate());


                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendSearchResponseDTO> searchPotentialFriends(String searchTerm, Long currentMemberId) {
        List<FriendSearchResponseDTO> results = friendRepository.searchPotentialFriends(searchTerm, currentMemberId);

        results.forEach(dto -> {
            dto.setProfileImg(dto.getProfileImg() != null ?
                    "/upload/" + dto.getProfileImg() :
                    "/images/default-profile.png");
        });

        return results;
    }

    public Friend getFriendRelation(Long friendId) {
        return friendRepository.findFriendRelationById(friendId);
    }


}