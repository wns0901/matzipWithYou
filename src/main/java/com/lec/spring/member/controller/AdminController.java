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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("members", memberService.findAll());
        return "member/admin";
    }

    @GetMapping("/member/{id}")
    public String getMember(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("member", member);
        return "member/detail";
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

    @DeleteMapping("/api/{type}/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteItem(@PathVariable String type, @PathVariable Long id) {
        try {
            int result = switch (type) {
                case "members" -> memberService.deleteById(id);
                case "matzips" -> matzipService.deleteById(id);
                case "tags" -> tagService.deleteById(id);
                case "foodkinds" -> foodKindService.deleteById(id);
                default -> throw new IllegalArgumentException("Invalid type: " + type);
            };

            if (result > 0) {
                return ResponseEntity.ok("삭제 성공");
            }
            return ResponseEntity.badRequest().body("삭제 실패");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("삭제 중 오류 발생: " + e.getMessage());
        }
    }
}