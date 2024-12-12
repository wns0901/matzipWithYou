package com.lec.spring.member.controller;

import com.lec.spring.member.domain.MyPage;
import com.lec.spring.member.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members/{memberId}")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }


    // 닉네임 + 친구, 위시/내맛집/내리뷰 Count + 위시/내맛집/내리뷰 Preview
    @GetMapping("")
    public ResponseEntity<MyPage> getMyPageInfo(@PathVariable Long memberId) {
        MyPage myPageInfo = myPageService.getFullMyPageInfo(memberId);
        return ResponseEntity.ok(myPageInfo);
    }


//    @GetMapping("")
//    public ResponseEntity<MyPage> getMyProfile(
//            @PathVariable Long memberId
//    ) {
//        MyPage myPage = new MyPage();
//        return ResponseEntity.ok(myPage);
//    }
//
//
//
//    // 위시, 내맛집, 내리뷰 카운팅
//    @GetMapping("")
//    public ResponseEntity<MyPage> wishlistCnt(
//            @PathVariable Long memberId
//    ) {
//        MyPage myPage = myPageService.wishlistCnt(MyPage);
//        return ResponseEntity.ok(myPage);
//    }
//    @GetMapping("")
//    public ResponseEntity<MyPage> myMatzipCnt(
//            @PathVariable Long memberId
//    ) {
//        MyPage myPage = myPageService.myMatzipCnt(MyPage);
//        return ResponseEntity.ok(myPage);
//    }
//    @GetMapping("")
//    public ResponseEntity<String> myReviewCnt(
//            @PathVariable Long memberId
//    ) {
//        MyPage myPage = myPageService.myReviewCnt(MyPage);
//        return ResponseEntity.ok(MyPage);
//    }
//
//
//    // 위시 내용
//    @GetMapping("")
//    public ResponseEntity<MyPage> getWishContents(
//            @PathVariable Long memberId
//    ) {
//
//    }
//
//    // 내맛집 내용
//    @GetMapping("")
//    public ResponseEntity<MyPage> getMyMatzipContents(
//            @PathVariable Long memberId
//    ) {
//
//    }
//
//    // 내리뷰 내용
//    @GetMapping("")
//    public ResponseEntity<MyPage> getMyReviewContents(
//            @PathVariable Long memberId
//    ) {
//
//    }


    // 닉네임 변경
    @ResponseBody
    @PatchMapping("")
    public ResponseEntity<String> updateNick(
            @PathVariable Long memberId,
            @RequestBody String newNickname
    ) {
        int rowsAffected = myPageService.updateNick(newNickname);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("닉네임 변경에 성공했습니다.");
        } else {
            return ResponseEntity.badRequest().body("닉네임 변경에 실패했습니다.");
        }
    }





}
