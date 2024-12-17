package com.lec.spring.matzip.controller;


import com.lec.spring.matzip.domain.DTO.FindingResultMyMatzipDTO;
import com.lec.spring.matzip.domain.DTO.UpdateMyMatzipVisibility;
import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.service.MyMatzipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/matzips/mine")
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
        FindingResultMyMatzipDTO result = myMatzipService.findByMemberId(memberId);
        model.addAttribute("result", result);

        return "/myMatzip/list";
    }

    @PostMapping("")
    public String save(@RequestBody MyMatzip myMatzip, Model model) {
        model.addAttribute("result", myMatzipService.saveMyMatzip(myMatzip));
        return "/matzip/myMatzipSaveOK";
    }

    @PatchMapping("/{myMatzipId}")
    public ResponseEntity<Map<String,String>> updateMyMatzipVisibility(@PathVariable Long myMatzipId, @RequestBody UpdateMyMatzipVisibility updateMyMatzipVisibility) {
        updateMyMatzipVisibility.setMyMatzipId(myMatzipId);
        return myMatzipService.updateMyMatzipVisibility(updateMyMatzipVisibility);
    }

    @DeleteMapping("/{myMatzipId}")
    public ResponseEntity<Map<String,String>> deleteMyMatzip(@PathVariable Long myMatzipId) {
        return myMatzipService.deleteMyMatzip(myMatzipId);
    }
}
