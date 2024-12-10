package com.lec.spring.matzip.controller;


import com.lec.spring.matzip.service.MyMatzipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/matzip/mine")
public class MyMatzipController {

    private final MyMatzipService myMatzipService;


    @Autowired
    public MyMatzipController(MyMatzipService myMatzipService) {
        this.myMatzipService = myMatzipService;
    }


    @GetMapping("/{memberId}")
    public String list(
            @PathVariable Long memberId,
            Model model) {
        model.addAttribute("result",myMatzipService.findByMemberId(memberId));

        return "/myMatzip/list";

    }

    @PatchMapping("/{myMatzipId}/visibility")
    public ResponseEntity<Map<String,String>> updateMyMatzipVisibility(@RequestBody Long myMayzipId, @RequestBody String visibility) {

        return myMatzipService.updateMyMatzipVisibility(myMayzipId, visibility);
    }

    @DeleteMapping("/{myMatzipId}")
    public ResponseEntity<Map<String,String>> deleteMyMatzip(@PathVariable Long myMatzipId) {
        return myMatzipService.deleteMyMatzip(myMatzipId);
    }

}
