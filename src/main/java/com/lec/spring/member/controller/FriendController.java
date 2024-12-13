package com.lec.spring.member.controller;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.service.FriendService;
import com.lec.spring.member.service.MemberServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members/{memberId}/friends")
public class FriendController {

    private final FriendService friendService;
    private final MemberServiceImpl memberServiceImpl;

    public FriendController(FriendService friendService, MemberServiceImpl memberServiceImpl) {
        this.friendService = friendService;
        this.memberServiceImpl = memberServiceImpl;
    }


    // 팝업으로 뜨는 애들은 비동기. 제이슨 보내줘야함
    // 친구 요청 보내기 / 비동기 팝업
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<String> sendFriendRequest(

            @RequestBody  Friend friend) {
        int result = friendService.sendFriendRequest(friend);
        if (result > 0) {
            return ResponseEntity.ok("친구 신청에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("친구 신청에 실패했습니다. 이미 친구거나 수락 대기중입니다.");
        }
    }

    // 친구 요청 목록 가져오기 / 비동기 팝업
    @ResponseBody
    @GetMapping("")
    public ResponseEntity<List<Friend>> getPendingRequests(@PathVariable Long memberId) {
        List<Friend> pendingRequests = friendService.getPendingRequests(memberId);
        return ResponseEntity.ok(pendingRequests);
    }

    // 요청 수락/거절 (update) / 비동기 팝업
    @ResponseBody
    @PatchMapping("")
    public ResponseEntity<Integer> respondToRequest(
            @RequestBody Friend friend,
            @PathVariable Long memberId
    ) {
        friend.setReceiverId(memberId);
        int affectedRows = friendService.respondToRequest(friend);
        return ResponseEntity.ok(affectedRows);
    }

    // 친구 삭제
    @ResponseBody
    @DeleteMapping("")
    public ResponseEntity<Integer> deleteFriend(
            @RequestBody Friend friend,
            @PathVariable Long memberId
    ){
        friend.setReceiverId(memberId);
        int affectedRows = friendService.deleteFriend(friend);
        return ResponseEntity.ok(affectedRows);
    }


    // 내 친구 목록 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<Friend>> getFriendsWithDetailsDTO(@PathVariable Long memberId) {
        List<Friend> friends = friendService.getFriendsWithDetailsDTO(memberId);
        return ResponseEntity.ok(friends);
    }


    // 친밀도
//    friendRepository.updateIntimacy(member.getId(), friend.getIntimacy());


}


// 친구요청 보내는 html 의 script 에 들어갈 JS
// // Query Parameter 방식
//async function sendFriendRequest(senderId, receiverId) {
//    const response = await fetch(/api/friend/request?senderId=${senderId}&receiverId=${receiverId}, {
//        method: 'POST',
//    });
//
//    if (response.ok) {
//        console.log('Friend request sent successfully');
//    } else {
//        console.error('Failed to send friend request');
//    }
//}