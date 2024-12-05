package com.lec.spring.member.controller;

import com.lec.spring.member.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

//    @GetMapping("/{유저아이디}")
//    public ResponseEntity<MyPageDto> getMyPageInfo(@PathVariable Long id) {
//        int friendCount = myPageService.getFriendCount(id);
//        int matzipCount = myPageService.getMatzipCount(id);
//        int point = myPageService.getPoint(id);
//
//        MyPageDto myPageDto = new MyPageDto(friendCount, matzipCount);
//        return ResponseEntity.ok(myPageDto);
//    }


}
