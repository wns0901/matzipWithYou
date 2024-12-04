package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.service.UserMatzipTagStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matzip")
public class UserMatzipTagStatusController {

    private final UserMatzipTagStatusService userMatzipTagStatusService;

    @Autowired
    public UserMatzipTagStatusController(UserMatzipTagStatusService userMatzipTagStatusService) {
        this.userMatzipTagStatusService = userMatzipTagStatusService;
    }

    @GetMapping("/new")
    public String showTagForm() {
        return "matzip/tagForm"; // tagForm.html로 이동
    }

    @PostMapping("/save")
    @ResponseBody
    public String saveTag(UserMatzipTagStatus userMatzipTagStatus) {
        userMatzipTagStatusService.tagSave(userMatzipTagStatus);
        return "ok"; // 태그 저장 후 리스트로 리다이렉트
    }


    /*@RequestParam 없애니까 memberId 안되던거 해결됨*/
    @GetMapping("/singleList")
    public String singleListTags(Long memberId,  Long myMatzipId, Model model) {
        UserMatzipTagStatus source = userMatzipTagStatusService.findTagByIdAndMember(memberId, myMatzipId);
        model.addAttribute("source", source );
        return "matzip/singleTagList"; // tagList.html로 이동
    }


    // 회원의 태그리스트 조회
    @GetMapping("/list")
    public String listTags(Long memberId, Model model) {
        List<UserMatzipTagStatus> tags = userMatzipTagStatusService.findTagsByMemberAndMatzip(memberId);
        model.addAttribute("tags", tags);

        return "matzip/tagList"; // tagList.html로 이동
    }

    //가게에 태그리스트 조회
    @GetMapping("/matzipList")
    public String getMembersAndTags( Long myMatzipId, Model model) {
        List<UserMatzipTagStatus> result = userMatzipTagStatusService.findMemberAndTagByMatzipId(myMatzipId);
        model.addAttribute("result", result);
        return "matzip/matzipTagList";
    }

}