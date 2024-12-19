package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.*;
import com.lec.spring.matzip.domain.DTO.ReviewDTO;
import com.lec.spring.matzip.domain.DTO.ReviewTagDTO;
import com.lec.spring.matzip.service.MatzipService;
import com.lec.spring.matzip.service.ReviewService;
import com.lec.spring.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/matzip")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviewList/{memberId}")
    public String reviewListPage(@PathVariable Long memberId, Model model) {
        model.addAttribute("memberId", memberId);
        return "matzip/reviewList";
    }

    @GetMapping("/api/reviews/{memberId}")
    @ResponseBody
    public List<Map<String, Object>> getReviewsWithMatzipInfo(@PathVariable Long memberId) {
        List<Review> reviews = reviewService.getAllReviews(memberId);
        List<Map<String, Object>> reviewsWithMatzip = new ArrayList<>();

        for (Review review : reviews) {
            try {
                Map<String, Object> reviewInfo = new HashMap<>();
                Long matzipId = review.getMatzipId();

                if (matzipId != null) {
                    reviewInfo.put("id", review.getId());
                    reviewInfo.put("content", review.getContent());
                    reviewInfo.put("regdate", review.getRegdate());
                    reviewInfo.put("starRating", review.getStarRating());

                    reviewInfo.put("foodKind", reviewService.getKindName(review.getId()));
                    reviewInfo.put("matzipName", reviewService.getMatzipName(matzipId));
                    reviewInfo.put("matzipAddress", reviewService.getMatzipAddress(matzipId));
                    reviewInfo.put("kakaoImgUrl", reviewService.getKakaoImgURl(matzipId));

                    reviewsWithMatzip.add(reviewInfo);
                }
            } catch (Exception e) {
                System.err.println("Error processing review ID: " + review.getId());
                e.printStackTrace();
            }
        }

        return reviewsWithMatzip;
    }

    @GetMapping("/api/reviews/{id}/detail")
    @ResponseBody
    public Map<String, Object> getReviewDetails(@PathVariable Long id) {
        Map<String, Object> detailInfo = new HashMap<>();

        try {
            Review review = reviewService.findById(id);
            if (review == null) {
                throw new IllegalArgumentException("Review not found for id: " + id);
            }

            String kindName = reviewService.getKindName(id);
            List<ReviewTag> reviewTags = reviewService.getReviewTags(id);
            List<String> reviewTagName = reviewService.getReviewTagNames(id);

            detailInfo.put("id", id);
            detailInfo.put("content", review.getContent());
            detailInfo.put("regdate", review.getRegdate());
            detailInfo.put("starRating", review.getStarRating());
            detailInfo.put("matzipId", review.getMatzipId());
            detailInfo.put("memberId", review.getMemberId());

            if (kindName != null) {
                detailInfo.put("kindName", kindName);
            }
            if (reviewTags != null && !reviewTags.isEmpty()) {
                detailInfo.put("reviewTags", reviewTags);
            }

            detailInfo.put("reviewTagName", reviewTagName);

            System.out.println("Returning detail info for review ID: " + id);
            System.out.println("Detail info: " + detailInfo);

        } catch (Exception e) {
            System.err.println("Error getting review details for id " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return detailInfo;
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
    public List<Tag> getTags() {
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


@PostMapping("/reviews/delete/{id}")
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

}
