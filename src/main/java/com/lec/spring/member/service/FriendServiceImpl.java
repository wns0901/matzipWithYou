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
    }

    @Override
    public List<FriendRequestDTO> getPendingRequests(Long memberId) {
        List<Friend> pendingFriends = friendRepository.findPendingRequests(memberId);

        return pendingFriends.stream()
                .map(friend -> {
                    // 이 부분의 쿼리를 수정해서 한번에 필요한 정보를 가져오도록 변경
                    return FriendRequestDTO.builder()
                            .senderId(friend.getSenderId())
                            .receiverId(friend.getReceiverId())
                            .isAccept(friend.getIsAccept())
                            .intimacy(friend.getIntimacy())
                            .regdate(friend.getRegdate())
                            .build();
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

    @Override
    public Friend getFriendRelation(Long currentMemberId, Long targetId) {
        return friendRepository.findFriendRelationByMemberIds(currentMemberId, targetId);
    }


}