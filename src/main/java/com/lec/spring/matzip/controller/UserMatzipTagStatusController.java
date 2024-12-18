package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import com.lec.spring.matzip.service.UserMatzipTagStatusService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
        // 리스트 섞기
        List<UserMatzipTagStatus> l = userMatzipTagStatusService.shuffleTagNames(myMatzipId);
        System.out.println("######랜덤으로 돌린 후: " + l.size());

        model.addAttribute("hints", l);
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

    @GetMapping("/tags/{myMatzipId}")
    public ResponseEntity<Map<String, Object>> getTags(@PathVariable Long myMatzipId) {
        Long memberId = userMatzipTagStatusService.getAuthenticatedMemberId(); // 로그인된 회원의 ID를 가져옴

        List<UserMatzipTagStatus> purchasedTags = userMatzipTagStatusService.purchasedTag(memberId);

        List<UserMatzipTagStatus> unpurchasedTags = userMatzipTagStatusService.unpurchasedTag(memberId);

        List<Map<String, Object>> purchasedTagsList = purchasedTags.stream()
                .filter(tag -> tag.getMyMatzipId().equals(myMatzipId))
                .map(tag -> {
                    Map<String, Object> tagMap = new HashMap<>();
                    tagMap.put("tagId", tag.getTagId());
                    tagMap.put("tagName", tag.getTagName());
                    return tagMap;
                })
                .collect(Collectors.toList());


        List<Map<String, Object>> unpurchasedTagsList = unpurchasedTags.stream()
                .filter(tag -> tag.getMyMatzipId().equals(myMatzipId))
                .map(tag -> {
                    Map<String, Object> tagMap = new HashMap<>();
                    tagMap.put("tagId", tag.getTagId());
                    tagMap.put("tagName", tag.getTagName());
                    return tagMap;
                })
                .collect(Collectors.toList());

        // 응답 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("myMatzipId", myMatzipId);
        response.put("purchased", purchasedTagsList);
        response.put("unpurchased", unpurchasedTagsList);

        return ResponseEntity.ok(response);
    }







}