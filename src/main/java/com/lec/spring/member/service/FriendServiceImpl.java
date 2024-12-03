package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.repository.FriendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final List<Friend> friendDatabase = new ArrayList<>(); // 친구목록

    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    // 친구 요청 보내기
    @Override
    public void sendFriendRequest(Long senderId, Long receiverId) {
        Friend friend = new Friend();
        friend.setSenderId(senderId);
        friend.setReceiverId(receiverId);
        friend.setAccept(false);
        friendRepository.save(friend);
    }

    // 친구 요청 수락/거절
    @Override
    public void respondToRequest(Long senderId, Long receiverId, boolean accept) {
        Optional<Friend> optionalFriend = friendDatabase.stream()
                .filter(friend -> friend.getSenderId().equals(senderId) && friend.getReceiverId().equals(receiverId))
                .findFirst();

        if (optionalFriend.isPresent()) {
            Friend friend = optionalFriend.get();
            if (accept) {
                friend.setAccept(true);
            } else {
                friendDatabase.remove(friend); // 요청 거절 시 삭제
            }
        } else {
            throw new IllegalArgumentException("친구 요청이 존재하지 않습니다.");
        }
    }

    // 친구 목록 가져오기
    @Override
    public List<Friend> getFriends(Long userId) {
        List<Friend> friends = new ArrayList<>();
        for (Friend friend : friendDatabase) {
            if ((friend.getSenderId().equals(userId) || friend.getReceiverId().equals(userId)) && friend.isAccept()) {
                friends.add(friend);
            }
        }
        return friends;
    }

    @Override
    public List<Friend> getPendingRequests(Long receiverId) {
        return List.of();
    }
}