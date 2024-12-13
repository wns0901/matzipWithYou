package com.lec.spring.member.controller;

import com.lec.spring.member.domain.MyPage;
import com.lec.spring.member.domain.UpdateNickDTO;
import com.lec.spring.member.service.MyPageService;
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
    public String getMyPageInfo(
            @PathVariable Long memberId
            , Model model
    ) {
        MyPage myPageInfo = myPageService.getFullMyPageInfo(memberId);
        model.addAttribute("myPage", myPageInfo);

        return "member/myPage";
    }


    // 닉네임 변경
    @ResponseBody
    @PatchMapping("/nickname")
    public String  updateNick(
            @PathVariable Long memberId,
            @RequestBody UpdateNickDTO updateNickDTO,
            Model model
    ) {
        String newNickname = "";
        int rowsAffected = myPageService.updateNick(updateNickDTO);

        if (rowsAffected > 0) {
            model.addAttribute("message", "닉네임 변경에 성공했습니다.");
        } else {
            model.addAttribute("message", "닉네임 변경에 실패했습니다.");
        }

        // 변경된 마이페이지 정보 갱신
        MyPage updatedMyPage = myPageService.getFullMyPageInfo(memberId);
        model.addAttribute("myPage", updatedMyPage);

        return "member/myPage";
    }



}
