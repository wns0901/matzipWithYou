package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.repository.FoodKindRepository;
import com.lec.spring.matzip.service.ReviewService;
import com.lec.spring.member.domain.Member;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/matzip")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews/list")
    public void list(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
    }

    @GetMapping("/reviews")
    public void save(Model model) {
        model.addAttribute("foodKinds", reviewService.getFoodKinds());
        model.addAttribute("tags", reviewService.getTags());
    }

    @GetMapping("/reviews/food-kinds")
    @ResponseBody
    public List<String> getFoodKinds() {
        return reviewService.getFoodKinds();
    }

    @GetMapping("/reviews/tags")
    @ResponseBody
    public List<ReviewTagDTO> getTags() {
        return reviewService.getTags();
    }

    @PostMapping("/reviews")
    public String saveOk(@Valid @ModelAttribute ReviewDTO reviewDTO
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }

            return "redirect:/matzip/reviews";
        }
    try {
        int saved = reviewService.addReview(reviewDTO, model);

        if (saved > 0) {
            String content = model.getAttribute("content").toString();
            List<String> foodKinds = (List<String>) model.getAttribute("foodKinds");
            String foodKind = (String) model.getAttribute("foodKind");
            List<ReviewTag> reviewTags = (List<ReviewTag>) model.getAttribute("reviewTags");
            String isHidden = (String) model.getAttribute("isHidden");
            List<Member> hiddenMatzipMemberIds = (List<Member>) model.getAttribute("members");
            Integer rewardReviewPoint = (Integer) model.getAttribute("rewardReviewPoint");
            Integer rewardReviewIntimacy = (Integer) model.getAttribute("rewardReviewIntimacy");

            redirectAttributes.addFlashAttribute("saveOk", "리뷰 작성이 완료되었습니다.");

            if (rewardReviewPoint != null) {
                redirectAttributes.addFlashAttribute("rewardReviewPoint", rewardReviewPoint);
            }

            if (rewardReviewIntimacy != null) {
                redirectAttributes.addFlashAttribute("rewardReviewIntimacy", rewardReviewIntimacy);
            }

            if (hiddenMatzipMemberIds != null && !hiddenMatzipMemberIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("isHidden", isHidden);
            }

            if (reviewTags != null && !reviewTags.isEmpty()) {
                redirectAttributes.addFlashAttribute("reviewTags", reviewTags);
            }

            if(foodKinds != null && !foodKinds.isEmpty()) {
                redirectAttributes.addFlashAttribute("foodKinds", foodKinds);
            }

            if (foodKind != null) {
                redirectAttributes.addFlashAttribute("foodKind", foodKind);
            }

            if (content != null) {
                redirectAttributes.addFlashAttribute("content", content);
            }

            return "redirect:/matzip/reviews/list";
        } else {
            redirectAttributes.addFlashAttribute("saveError", "리뷰작성에 실패했습니다.");
            return "redirect:/matzip/reviews";
        }
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("saveError", "리뷰 작성에 실패했습니다: " + e.getMessage());
        return "redirect:/matzip/reviews";
    }
}


@PostMapping("/reviews/delete")
    public String deleteOk(@RequestParam Long id
        , RedirectAttributes redirectAttributes
    ) {
    try {
        int result = reviewService.deleteReview(id);

        if (result > 0) {
            redirectAttributes.addFlashAttribute("deleteOk", "리뷰가 성공적으로 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("deleteError", "리뷰 삭제에 실패했습니다.");
        }

        return "redirect:/matzips/reviews/list";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("deleteError", "리뷰 삭제 중 오류가 발생했습니다: " + e.getMessage());
        return "redirect:/matzips/reviews/list";
    }
}
    @GetMapping("/reviews/{id}")
    public String getReviewDetails(
            @PathVariable Long id,
            Model model
    ) {
        Review review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "matzip/reviews/detail";
    }
}
