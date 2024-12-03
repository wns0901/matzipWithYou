package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendId;
import com.lec.spring.member.repository.FriendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

//    private final Map<FriendId, Friend> friendDatabase = new HashMap<>();
    private final FriendRepository friendRepository;
    private final List<Friend> friendDatabase = new ArrayList<>(); // 친구목록

    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    // 친구 요청 보내기
    @Override
    public void sendFriendRequest(Long senderId, Long receiverId) {
        FriendId friendId = new FriendId(senderId, receiverId);

        // 중복 요청 방지
        boolean exists = friendDatabase.stream().anyMatch(friend -> friend.getId().equals(friendId));
        if (exists) {
            throw new IllegalArgumentException("이미 친구 요청을 보냈습니다.");
        }

        // 새로운 친구 요청 생성
        Friend friend = Friend.builder()
                .id(friendId)
                .isAccept(false)
                .regdate(LocalDateTime.now())
                .build();

        friendDatabase.add(friend);
    }

    // 친구 요청 수락/거절
    @Override
    public void respondToRequest(Long senderId, Long receiverId, boolean accept) {
        FriendId friendId = new FriendId(senderId, receiverId);
        Friend friend = friendDatabase.stream()
                .filter(f -> f.getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("친구 요청이 존재하지 않습니다."));

        if (accept) {
            friend.setAccept(true); // 요청 수락
        } else {
            friendDatabase.remove(friend); // 요청 거절 시 삭제
        }
    }

    // 친구 목록 가져오기
    @Override
    public List<Friend> getFriends(Long userId) {
        return friendDatabase.stream()
                .filter(f -> (f.getId().getSenderId().equals(userId) || f.getId().getReceiverId().equals(userId)) && f.isAccept())
                .collect(Collectors.toList());
    }

    // 대기 중인 친구 요청
    @Override
    public List<Friend> getPendingRequests(Long receiverId) {
        return friendDatabase.stream()
                .filter(f -> f.getId().getReceiverId().equals(receiverId) && !f.isAccept())
                .collect(Collectors.toList());
    }
}