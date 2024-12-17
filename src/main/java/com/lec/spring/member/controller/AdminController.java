package com.lec.spring.member.controller;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.service.FoodKindService;
import com.lec.spring.matzip.service.MatzipService;
import com.lec.spring.matzip.service.TagService;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;
    private final MatzipService matzipService;
    private final TagService tagService;
    private final FoodKindService foodKindService;

    @Autowired
    public AdminController(
            MemberService memberService,
            MatzipService matzipService,
            TagService tagService,
            FoodKindService foodKindService) {
        this.memberService = memberService;
        this.matzipService = matzipService;
        this.tagService = tagService;
        this.foodKindService = foodKindService;
    }

    @GetMapping("")
    public String adminPage(Model model) {
        // 기본적으로 member list 표시
        model.addAttribute("members", memberService.findAll());
        return "member/admin";
    }

    @GetMapping("/api/members")
    @ResponseBody
    public List<Member> getMembers() {
        return memberService.findAll();
    }

    @GetMapping("/api/matzips")
    @ResponseBody
    public List<Matzip> getMatzips() {
        return matzipService.getAllMatzips();
    }

    @GetMapping("/api/tags")
    @ResponseBody
    public List<Tag> getTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/api/foodkinds")
    @ResponseBody
    public List<FoodKind> getFoodKinds() {
        return foodKindService.getAllFoodKinds();
    }
}
