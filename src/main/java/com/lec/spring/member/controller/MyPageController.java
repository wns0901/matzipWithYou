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
        model.addAttribute("memberId", memberId);

        return "member/myPage";
    }


    // 닉네임 변경
    @ResponseBody
    @PatchMapping("") // 추후 /nickname 추가 필요
//    public Map<String, Object>  updateNick(
    public String   updateNick(
            @PathVariable Long memberId,
            @RequestBody UpdateNickDTO updateNickDTO,
            Model model
    ) {
        try {
            int rowsAffected = myPageService.updateNick(memberId, updateNickDTO);
            if (rowsAffected > 0) {
                model.addAttribute("message", "닉네임 변경에 성공했습니다.");
            } else {
                model.addAttribute("message", "닉네임 변경에 실패했습니다.");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        } catch (RuntimeException e) {
            model.addAttribute("error", "서버 오류: 닉네임 변경에 실패했습니다.");
        }

        // 변경된 마이페이지 정보 갱신
        MyPage updatedMyPage = myPageService.getFullMyPageInfo(memberId);
        model.addAttribute("myPage", updatedMyPage);

        return "member/myPage";
    }

//        Map<String, Object> response = new HashMap<>();
//        try {
//            int rowsAffected = myPageService.updateNick(memberId, updateNickDTO);
//
//            if (rowsAffected > 0) {
//                response.put("message", "닉네임 변경에 성공했습니다.");
//            } else {
//                response.put("message", "닉네임 변경에 실패했습니다.");
//            }
//
//            // 갱신된 마이페이지 데이터 추가
//            MyPage updatedMyPage = myPageService.getFullMyPageInfo(memberId);
//            response.put("myPage", updatedMyPage);
//
//        } catch (IllegalArgumentException e) {
//            response.put("error", e.getMessage());
//        } catch (RuntimeException e) {
//            response.put("error", "서버 오류: 닉네임 변경에 실패했습니다.");
//        }
//        return response;
//    }

}
