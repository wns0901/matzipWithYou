package com.lec.spring.member.controller;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FriendRelationController {
    private final FriendService friendService;

    @GetMapping("/members/{currentMemberId}/friends/relation/{targetId}")
    public Friend getFriendRelation(
            @PathVariable Long currentMemberId,
            @PathVariable Long targetId) {
        return friendService.getFriendRelation(currentMemberId, targetId);
    }
}