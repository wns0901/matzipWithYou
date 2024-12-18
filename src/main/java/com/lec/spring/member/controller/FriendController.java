package com.lec.spring.member.controller;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members/{memberId}/friends")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    // 친구 요청 보내기 (비동기)
    @ResponseBody
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody Friend friend) {
        int result = friendService.sendFriendRequest(friend);
        if (result > 0) {
            return ResponseEntity.ok("친구 신청에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("친구 신청에 실패했습니다. 이미 친구거나 수락 대기중입니다.");
        }
    }

    // 대기중인 친구 요청 목록 가져오기 (비동기)
    @ResponseBody
    @GetMapping("/pending")
    public ResponseEntity<List<Friend>> getPendingRequests(@PathVariable Long memberId) {
        List<Friend> pendingRequests = friendService.getPendingRequests(memberId);
        return ResponseEntity.ok(pendingRequests);
    }

    // 친구 요청 수락 (비동기)
    @ResponseBody
    @PatchMapping("/accept")
    public ResponseEntity<Integer> acceptFriendRequest(
            @RequestBody Friend friend,
            @PathVariable Long memberId
    ) {
        friend.setReceiverId(memberId);
        int affectedRows = friendService.acceptFriendRequest(friend);
        return ResponseEntity.ok(affectedRows);
    }

    // 친구 요청 거절 (비동기)
    @ResponseBody
    @PatchMapping("/reject")
    public ResponseEntity<Integer> rejectFriendRequest(
            @RequestBody Friend friend,
            @PathVariable Long memberId
    ) {
        friend.setReceiverId(memberId);
        int affectedRows = friendService.rejectFriendRequest(friend);
        return ResponseEntity.ok(affectedRows);
    }

    // 친구 삭제 (비동기)
    @ResponseBody
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Integer> deleteFriend(@PathVariable Long friendId) {
        int affectedRows = friendService.deleteFriend(friendId);
        return ResponseEntity.ok(affectedRows);
    }

    // 내 전체 친구 목록 가져오기 (비동기)
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<List<FriendDetailsDTO>> getFriendsWithDetailsDTO(@PathVariable Long memberId) {
        List<FriendDetailsDTO> friends = friendService.getFriendsWithDetailsDTO(memberId);
        return ResponseEntity.ok(friends);
    }

    // 친구 목록 페이지 보기
    @GetMapping("/view")
    public String getFriendsList(
            @PathVariable Long memberId,
            Model model
    ) {
        List<FriendDetailsDTO> friends = friendService.getFriendsWithDetailsDTO(memberId);
        model.addAttribute("friends", friends);
        model.addAttribute("memberId", memberId);
        return "member/friend/list";
    }
}

