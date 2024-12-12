package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.Tag;
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
    @ResponseBody
    public String singleListTags(Long memberId, Long myMatzipId, Model model) {
        UserMatzipTagStatus source = userMatzipTagStatusService.findTagByMemberIdAndMatzipId(memberId, myMatzipId);
        System.out.println("###############source = " + source);
        model.addAttribute("tags", source );
        return "matzip/singleTagList"; // tagList.html로 이동
    }


    // 회원의 태그리스트 조회
    @GetMapping("/list")
    @ResponseBody
    public String listTags(Long memberId, Model model) {
        List<UserMatzipTagStatus> tags = userMatzipTagStatusService.findTagsAndMatzipIdByMember(memberId);
        System.out.println("###############tags = " + tags );
        model.addAttribute("tags", tags);
        return "matzip/tagList"; // tagList.html로 이동
    }

    //가게에 태그리스트 조회
    @GetMapping("/matzipList")
    @ResponseBody
    public String getMembersAndTags( Long myMatzipId,  Model model) {
        List<UserMatzipTagStatus> result = userMatzipTagStatusService.findMemberAndTagByMatzipId(myMatzipId);
        System.out.println("###############result = " + result);
        model.addAttribute("tags", result);
        return "matzip/matzipTagList";
    }

    //히든 태그 조회
    @GetMapping("/hiddenTags")
    @ResponseBody
    public List<Long> getHiddenMatzipTagIds(Long myMatzipId) {
        System.out.println("HIDDEN 맛집 ID 요청: " + myMatzipId);
        List<Long> hiddenTags = userMatzipTagStatusService.listHiddenMatzipTagIds(myMatzipId);
        System.out.println("응답할 히든 태그 ID: " + hiddenTags);
        return hiddenTags;
    }

    //kindName
    @GetMapping("/kindName")
    @ResponseBody
    public String getKindName( Long myMatzipId, Model model) {
        System.out.println("myMatzipId = " + myMatzipId);
        String kindName = userMatzipTagStatusService.listKindName(myMatzipId);
        System.out.println("###############kindName = " + kindName);
        model.addAttribute("kindName", kindName);
        return "matzip/kindName";
    }
    
    //wholeHiddenList 
    @GetMapping("/wholeHiddenList")
    public String getWholeHiddenMatzipTagIds(Model model) {
        List<UserMatzipTagStatus> result = userMatzipTagStatusService.listWholeHiddenList();
        System.out.println("###############전체 히든 맛집 = " + result);

       List<UserMatzipTagStatus> duplicate= userMatzipTagStatusService.finddeleteDuplicateMyMatzipId();
        System.out.println("#########중복 반환" + duplicate);

        List<UserMatzipTagStatus> deleteDuplicate= userMatzipTagStatusService.deleteDuplicateMyMatzipId();

        System.out.println("########deleteDuplicate" + deleteDuplicate);

        List<UserMatzipTagStatus> userMatzipTagStatuses = userMatzipTagStatusService.userMatzipTagStatus();
        System.out.println("########userMatzipTagStatuses = " + userMatzipTagStatuses);

        model.addAttribute("userMatzipTagStatuses", userMatzipTagStatuses);
        model.addAttribute("tags", result);
        model.addAttribute("duplicate", duplicate);
        model.addAttribute("deleteDuplicate", deleteDuplicate);
        return "matzip/wholeHiddenList";
    }



}