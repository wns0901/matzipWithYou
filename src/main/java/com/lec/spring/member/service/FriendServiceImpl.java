package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.domain.FriendRequestDTO;
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
    public int sendFriendRequest(Friend friend) {

        // 중복 요청 확인
        if (friendRepository.isAlreadyFriend(friend)) { // ?
            return 0;
        }
        return friendRepository.sendFriendRequest(friend); // DB에 저장
    }

    // 친구 요청 수락/거절
    @Override
    public int respondToRequest(Friend friend) {
        int newFriend = friendRepository.acceptFriendRequest(friend);
        if (newFriend == 0) {
            return 0;
        }
        if (newFriend == 1) {
            // 요청 수락
            friend.setIsAccept(true);
            return friendRepository.acceptFriendRequest(friend); // 업데이트된 행 수 반환
        } else {
            // 요청 거절
            return friendRepository.rejectFriendRequest(friend); // 삭제된 행 수 반환
        }
    }


    // 친구 삭제
    @Override
    public int deleteFriend(Friend friend) {
        return friendRepository.rejectFriendRequest(friend);
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
                    FriendRequestDTO dto = new FriendRequestDTO();
                    FriendDetailsDTO details = friend.getFriendDetails();

                    dto.setSenderId(friend.getSenderId());
                    dto.setReceiverId(friend.getReceiverId());
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




}