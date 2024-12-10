package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.service.ReviewService;
import com.lec.spring.member.domain.Member;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/list")
    public void list(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
    }

    @GetMapping("/write")
    public void write(Model model) {
    }

    @PostMapping("/write")
    public String writeOk(@Valid ReviewDTO reviewDTO
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }

            return "redirect:/review/write";
        }

        int saved = reviewService.addReview(reviewDTO, model);

        if (saved > 0) {
            String isHidden = (String) model.getAttribute("isHidden");
            List<ReviewTag> reviewTags = (List<ReviewTag>) model.getAttribute("reviewTags");
            List<Member> hiddenMatzipMemberIds = (List<Member>) model.getAttribute("members");
            Integer rewardReviewPoint = (Integer) model.getAttribute("rewardReviewPoint");
            Integer rewardReviewIntimacy = (Integer) model.getAttribute("rewardReviewIntimacy");

            redirectAttributes.addFlashAttribute("saveOk", "리뷰 작성이 완료되었습니다.");

            if(rewardReviewPoint != null && rewardReviewIntimacy != null) {
                redirectAttributes.addFlashAttribute("rewardReviewPoint", rewardReviewPoint);
                redirectAttributes.addFlashAttribute("rewardReviewIntimacy", rewardReviewIntimacy);
            }

            if(!hiddenMatzipMemberIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("isHidden", isHidden);
            }

            if(!reviewTags.isEmpty()) {
                redirectAttributes.addFlashAttribute("reviewTags", reviewTags);
            }

            redirectAttributes.addFlashAttribute("saveError", "리뷰작성에 실패했습니다.");
            return "redirect:/review/writeOk";
        }

        model.addAttribute("review", reviewService.addReview(reviewDTO, model));
        return "redirect:/review/write";
    }

    @PostMapping("/delete/{id}")
    public String deleteOk(ReviewDTO reviewDTO, Model model) {
        model.addAttribute("")
    }

}
