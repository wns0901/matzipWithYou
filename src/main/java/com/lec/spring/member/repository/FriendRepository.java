package com.lec.spring.member.repository;

import com.lec.spring.member.domain.Friend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendDTO> {

    List<Friend> findByReceiverIdAndIsAccept(Long receiverId, Boolean isAccept);

    List<Friend> findBySenderIdOrReceiverIdAndIsAccept(Long senderId, Long receiverId, Boolean isAccept);


}
