package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import com.lec.spring.matzip.service.UserMatzipTagStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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



    //wholeHiddenListt
    @GetMapping("/wholeHiddenListt/{myMatzipId}")
    public String getWholeHiddenMatzipTagIds(@PathVariable Long myMatzipId, Model model) {
        List<UserMatzipTagStatus> result = userMatzipTagStatusService.listWholeHiddenListByMyMatzipId(myMatzipId);
        System.out.println("###############전체 히든 맛집 = " + result.size());

        List<UserMatzipTagStatus> duplicate= userMatzipTagStatusService.findDuplicateMyMatzipId(myMatzipId);
        System.out.println("#########중복 태그 반환 : " + duplicate.size());

        List<UserMatzipTagStatus> deleteDuplicate= userMatzipTagStatusService.deleteDuplicateByMyMatzipId(myMatzipId);

        System.out.println("########삭제된 결과 : " + deleteDuplicate.size());

        List<UserMatzipTagStatus> userMatzipTagStatuses = userMatzipTagStatusService.userMatzipTagStatus();
        System.out.println("########userMatzipTagStatuses = " + userMatzipTagStatuses.size());

        model.addAttribute("userMatzipTagStatuses", userMatzipTagStatuses);
        model.addAttribute("tags", result);
        model.addAttribute("duplicate", duplicate);
        model.addAttribute("deleteDuplicate", deleteDuplicate);
        return "matzip/wholeHiddenListt";
    }


    @GetMapping("/hint/{myMatzipId}")
    public String showHintForm(@PathVariable Long myMatzipId, Model model) {
        List<UserMatzipTagStatus> result = userMatzipTagStatusService.hintByMyMatzipId(myMatzipId);
        System.out.println("#########랜덤 돌리기 전 : "+ result);
        // 리스트 섞기
        result = userMatzipTagStatusService.shuffleTagNames(result);
        System.out.println("######랜덤으로 돌린 후: " + result);
        model.addAttribute("hints", result);
        return "matzip/hintForm";
    }

    @PostMapping("/saveTag")
    public ResponseEntity<String> saveTagStatus(@RequestBody UserMatzipTagStatus request) {
        try {
            userMatzipTagStatusService.hintTagSave(
                    request.getMemberId(),
                    request.getMyMatzipId(),
                    request.getTagId());
            return ResponseEntity.ok("힌트가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("힌트 저장에 실패했습니다: " + e.getMessage());
        }}




}