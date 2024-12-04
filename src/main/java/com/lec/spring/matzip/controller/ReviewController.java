package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Review;
import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.service.ReviewService;
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
        System.out.println("ReviewController() 생성");
        this.reviewService = reviewService;
    }

    @GetMapping("/list")
    public void list(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
    }

    @GetMapping("/write")
    public void write() {}

    @PostMapping("/write")
    public String writeOk(@Valid Review review
            , @RequestParam List<Tag> tag
            , @RequestParam FoodKind foodKind
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("tags", Tag.getTag());

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }

            if (tag.size() < 3 || tag.size() > 5) {
                redirectAttributes.addFlashAttribute("error_tag", "태그는 최소 3개, 최대 5개까지 선택 가능합니다.");
            }

            return "redirect:/review/write";
        }
        model.addAttribute("review", reviewService.addReview(review, tag, foodKind));
        return "review/writeOk";
    }


}
