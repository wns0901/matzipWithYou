package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.repository.FriendRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public void sendFriendRequest(Long senderId, Long receiverId) {
        Friend friend = new Friend();
        friend.setSenderId(senderId);
        friend.setReceiverId(receiverId);
        friendRepository.save(friend);
    }

    public List<Friend> getPendingRequests(Long receiverId) {
        return friendRepository.findByReceiverIdAndIsAccept(receiverId, false);
    }

    public void respondToRequest(Long senderId, Long receiverId, boolean accept) {
        friendRepository.updateAcceptStatus(senderId, receiverId, accept);
    }

    public List<Friend> getFriends(Long userId) {
        return friendRepository.findBySenderIdOrReceiverIdAndIsAccept(userId, userId, true);
    }
}
