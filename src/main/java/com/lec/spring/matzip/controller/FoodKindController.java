package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.service.FoodKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foodkinds")
public class FoodKindController {

    private final FoodKindService foodKindService;

    @Autowired
    public FoodKindController(FoodKindService foodKindService) {
        this.foodKindService = foodKindService;
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(@RequestBody FoodKind foodKind) {
        try {
            foodKindService.save(foodKind);
            return "저장 성공";
        } catch (Exception e) {
            return "저장 실패";
        }
    }
}