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
@RequestMapping("/matzip")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/review/list")
    public void list(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
    }

    @GetMapping("/review")
    public void save(Model model) {
    }

    @PostMapping("/review")
    public String saveOk(@Valid ReviewDTO reviewDTO
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }

            return "redirect:/matzips/review";
        }

        int saved = reviewService.addReview(reviewDTO, model);

        if (saved > 0) {
            String content = model.getAttribute("content").toString();
            String foodKind = (String) model.getAttribute("foodKind");
            List<ReviewTag> reviewTags = (List<ReviewTag>) model.getAttribute("reviewTags");
            String isHidden = (String) model.getAttribute("isHidden");
            List<Member> hiddenMatzipMemberIds = (List<Member>) model.getAttribute("members");
            Integer rewardReviewPoint = (Integer) model.getAttribute("rewardReviewPoint");
            Integer rewardReviewIntimacy = (Integer) model.getAttribute("rewardReviewIntimacy");

            redirectAttributes.addFlashAttribute("saveOk", "리뷰 작성이 완료되었습니다.");

            if (rewardReviewPoint != null && rewardReviewIntimacy != null) {
                redirectAttributes.addFlashAttribute("rewardReviewPoint", rewardReviewPoint);
                redirectAttributes.addFlashAttribute("rewardReviewIntimacy", rewardReviewIntimacy);
            }

            if (hiddenMatzipMemberIds != null && !hiddenMatzipMemberIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("isHidden", isHidden);
            }

            if (reviewTags != null && !reviewTags.isEmpty()) {
                redirectAttributes.addFlashAttribute("reviewTags", reviewTags);
            }

            if (foodKind != null) {
                redirectAttributes.addFlashAttribute("foodKind", foodKind);
            }

            if (content != null) {
                redirectAttributes.addFlashAttribute("content", content);
            }

            return "redirect:/matzips/review/list";
        } else {
            redirectAttributes.addFlashAttribute("saveError", "리뷰작성에 실패했습니다.");
            return "redirect:/matzips/review";
        }
    }

    @PostMapping("/review/delete")
    public String deleteOk(Long id, Model model) {
        model.addAttribute("result", reviewService.deleteReview(id));
        return "/matzips/review/deleteOk";
    }
}
