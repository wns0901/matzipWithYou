package com.lec.spring.member.domain;

import java.io.Serializable;
import java.util.Objects;

public class FriendId implements Serializable {
    private Long senderId;
    private Long receiverId;

    public FriendId() {}

    public FriendId(Long senderId, Long receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return Objects.equals(senderId, friendId.senderId) && Objects.equals(receiverId, friendId.receiverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId);
    }
}