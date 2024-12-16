package com.lec.spring.member.controller;

import com.lec.spring.member.domain.MyPage;
import com.lec.spring.member.domain.UpdateNickDTO;
import com.lec.spring.member.service.MyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    @PatchMapping("") // 추후 /nickname 추가 필요
    public Map<String, Object>  updateNick(
            @PathVariable Long memberId,
            @RequestBody UpdateNickDTO updateNickDTO,
            Model model
    ) {

        Map<String, Object> response = new HashMap<>();
        try {
            int rowsAffected = myPageService.updateNick(memberId, updateNickDTO);

            if (rowsAffected > 0) {
                response.put("message", "닉네임 변경에 성공했습니다.");
            } else {
                response.put("message", "닉네임 변경에 실패했습니다.");
            }

            MyPage updatedMyPage = myPageService.getFullMyPageInfo(memberId);
            response.put("myPage", updatedMyPage);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
        } catch (RuntimeException e) {
            response.put("error", "서버 오류: 닉네임 변경에 실패했습니다.");
        }
        return response;
    }

}
