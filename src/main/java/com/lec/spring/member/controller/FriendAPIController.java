package com.lec.spring.member.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.member.domain.FriendSearchResponseDTO;
import com.lec.spring.member.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendAPIController {

    private FriendService friendService;

    public FriendAPIController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FriendSearchResponseDTO>> searchPotentialFriends(
            @RequestParam String term,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<FriendSearchResponseDTO> results = friendService.searchPotentialFriends(
                term,
                principalDetails.getMember().getId()
        );
        return ResponseEntity.ok(results);
    }
}

