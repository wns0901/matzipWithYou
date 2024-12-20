package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.DTO.ReviewDTO;
import com.lec.spring.matzip.domain.DTO.ReviewSubmitModalDTO;
import com.lec.spring.matzip.domain.Review;
import com.lec.spring.matzip.domain.ReviewTag;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

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
    public ResponseEntity<List<Map<String, Object>>> getReviewsWithMatzipInfo(@PathVariable Long memberId) {
        try {
            List<Review> reviews = reviewService.getAllReviews(memberId);
            List<Map<String, Object>> reviewsWithMatzip = new ArrayList<>();

            for (Review review : reviews) {
                if (review.getMatzipId() != null) {
                    reviewsWithMatzip.add(createReviewInfo(review));
                }
            }

            return ResponseEntity.ok(reviewsWithMatzip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String, Object> createReviewInfo(Review review) {
        Map<String, Object> reviewInfo = new HashMap<>();
        Long matzipId = review.getMatzipId();

        reviewInfo.put("id", review.getId());
        reviewInfo.put("content", review.getContent());
        reviewInfo.put("regdate", review.getRegdate());
        reviewInfo.put("starRating", review.getStarRating());
        reviewInfo.put("foodKind", reviewService.getKindName(review.getId()));
        reviewInfo.put("matzipName", reviewService.getMatzipName(matzipId));
        reviewInfo.put("matzipAddress", reviewService.getMatzipAddress(matzipId));
        reviewInfo.put("kakaoImgUrl", reviewService.getKakaoImgURl(matzipId));

        return reviewInfo;
    }

    @GetMapping("/api/reviews/{id}/detail")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getReviewDetails(@PathVariable Long id) {
        try {
            Review review = reviewService.findById(id);
            if (review == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> detailInfo = createDetailInfo(review);
            return ResponseEntity.ok(detailInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String, Object> createDetailInfo(Review review) {
        Map<String, Object> detailInfo = new HashMap<>();
        Long id = review.getId();

        detailInfo.put("id", id);
        detailInfo.put("content", review.getContent());
        detailInfo.put("regdate", review.getRegdate());
        detailInfo.put("starRating", review.getStarRating());
        detailInfo.put("matzipId", review.getMatzipId());
        detailInfo.put("memberId", review.getMemberId());

        String kindName = reviewService.getKindName(id);
        if (kindName != null) {
            detailInfo.put("kindName", kindName);
        }

        List<ReviewTag> reviewTags = reviewService.getReviewTags(id);
        if (reviewTags != null && !reviewTags.isEmpty()) {
            detailInfo.put("reviewTags", reviewTags);
        }

        detailInfo.put("reviewTagName", reviewService.getReviewTagNames(id));

        return detailInfo;
    }

    @GetMapping("/reviews/")
    public Long getAuthenticatedMemberId(Model model) {
        Long memberId = reviewService.getAuthenticatedMemberId();
        model.addAttribute("memberId", memberId);
        return memberId;
    }

    @GetMapping("/reviews/{memberId}/{matzipId}")
    public String save(@PathVariable Long matzipId
            , @PathVariable Long memberId
            , Model model) {
        model.addAttribute("memberId", memberId);
        model.addAttribute("matzipId", matzipId);
        model.addAttribute("foodKinds", reviewService.getFoodKinds());
        model.addAttribute("tags", reviewService.getTags());
        return "matzip/reviews";
    }

    @GetMapping("/reviews/food-kinds")
    @ResponseBody
    public ResponseEntity<List<String>> getFoodKinds() {
        try {
            return ResponseEntity.ok(reviewService.getFoodKinds());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reviews/tags")
    @ResponseBody
    public ResponseEntity<List<Tag>> getTags() {
        try {
            return ResponseEntity.ok(reviewService.getTags());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/reviews/{memberId}/{matzipId}/modal")
    @ResponseBody
    public ResponseEntity<?> getReviewModal(
            @PathVariable Long memberId,
            @PathVariable Long matzipId
    ) {
        try {
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .memberId(memberId)
                    .matzipId(matzipId)
                    .build();

            ReviewSubmitModalDTO modalData = reviewService.getModalInfo(reviewDTO);
            return ResponseEntity.ok(modalData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/reviews/{memberId}/{matzipId}")
    @ResponseBody
    public ResponseEntity<?> saveOk(
            @Valid @RequestBody ReviewDTO reviewDTO,
            @PathVariable Long memberId,
            @PathVariable Long matzipId,
            Model model
    ) {
        try {
            reviewDTO.setMemberId(memberId);
            reviewDTO.setMatzipId(matzipId);

            int saved = reviewService.addReview(reviewDTO, model);
            if (saved > 0) {
                addSuccessAttributes(model, new RedirectAttributesModelMap());
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "리뷰가 저장되었습니다."
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "리뷰작성에 실패했습니다."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "알 수 없는 오류가 발생했습니다.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", errorMessage));
        }
    }

    private void addSuccessAttributes(Model model, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("saveOk", "리뷰 작성이 완료되었습니다.");

        Object[] attributes = {
                "content", "foodKinds", "foodKind", "reviewTags",
                "isHidden", "members", "rewardReviewPoint", "rewardReviewIntimacy"
        };

        for (Object attr : attributes) {
            Object value = model.getAttribute(attr.toString());
            if (value != null) {
                redirectAttributes.addFlashAttribute(attr.toString(), value);
            }
        }
    }

    @DeleteMapping("/reviews/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteOk(@PathVariable Long id) {
        try {
            int result = reviewService.deleteReview(id);
            if (result > 0) {
                return ResponseEntity.ok().body(Map.of("message", "리뷰가 성공적으로 삭제되었습니다."));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "리뷰 삭제에 실패했습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "리뷰 삭제 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
