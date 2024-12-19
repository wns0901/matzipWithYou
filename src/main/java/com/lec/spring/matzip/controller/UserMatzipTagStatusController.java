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
        Long memberId = userMatzipTagStatusService.getAuthenticatedMemberId(); // 로그인된 사용자 ID 가져오기
        model.addAttribute("memberId", memberId);
        model.addAttribute("hints", l);
        return "matzip/hintForm";
    }

    @GetMapping("/saveTag")
    public String saveTagForm() {
        return "matzip/tagForm";
    }

    @PostMapping("/saveTag")
    @ResponseBody
    public ResponseEntity<String> saveTagStatus(@RequestBody UserMatzipTagStatus request) {
        try {
            // 포인트 차감 후, 성공 여부 확인
            boolean isPointsDeducted = userMatzipTagStatusService.deductPointsForHint(
                    request.getMemberId(),
                    1 // 포인트 차감 수치 (예시로 1 사용)
            );
            System.out.println("포인트가 차감 되었습니까 ? : " + isPointsDeducted);

            // 포인트 차감이 실패했을 경우
            if (!isPointsDeducted) {
                return ResponseEntity.status(400).body("포인트 차감에 실패했습니다. 포인트가 부족할 수 있습니다.");
            }

            // 힌트 저장 처리
            userMatzipTagStatusService.hintTagSave(
                    request.getMemberId(),
                    request.getMyMatzipId(),
                    request.getTagId(),
                    1 // 포인트 차감 수치 (예시로 1 사용)
            );

            return ResponseEntity.ok("힌트가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("힌트 저장에 실패했습니다: " + e.getMessage());
        }
    }


    @GetMapping("/tags/{myMatzipId}")
    public ResponseEntity<Map<String, Object>> getTags(@PathVariable Long myMatzipId) {
        Long memberId = userMatzipTagStatusService.getAuthenticatedMemberId(); // 로그인된 회원의 ID를 가져옴

        /// 구매된태그
        List<UserMatzipTagStatus> purchasedTags = userMatzipTagStatusService.purchasedTag(memberId);

        // 미구매태그
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