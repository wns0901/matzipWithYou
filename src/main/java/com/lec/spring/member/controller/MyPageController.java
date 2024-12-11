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

    @RequestMapping("/")
    public void MyPage(Model model) {}

    // 내 정보
    @GetMapping("")
    public ResponseEntity<MyPage> getMyPageInfo(
            @PathVariable Long memberId
    ) {
        MyPage mypage = new MyPage();
        return ResponseEntity.ok(mypage);
    };

    // 위시, 내맛집, 내리뷰 카운팅
    @GetMapping("")
    public ResponseEntity<MyPage> myActsCount(
    ) {

    };



    // 위시, 내맛집, 내리뷰 내용
    @GetMapping("")
    public ResponseEntity<MyPage> getMyPage(
    ) {

    };

    // 관리 페이지로 이동




    // 닉네임 변경
    @ResponseBody
    @PatchMapping("")
    public ResponseEntity<MyPage> updateNick(

    ){

    }


}
