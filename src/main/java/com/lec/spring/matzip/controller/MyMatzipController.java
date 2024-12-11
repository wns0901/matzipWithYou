package com.lec.spring.matzip.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.matzip.domain.DTO.FindingResultMyMatzipDTO;
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


    @ResponseBody
    @GetMapping("/{memberId}")
    public ResponseEntity<FindingResultMyMatzipDTO> list(
            @PathVariable Long memberId,
            Model model) {
        FindingResultMyMatzipDTO result = myMatzipService.findByMemberId(memberId);
        model.addAttribute("result", result);
        System.out.println(result);
        ObjectMapper objectMapper = new ObjectMapper(); try { String jsonResult = objectMapper.writeValueAsString(result); System.out.println(jsonResult); } catch (
                JsonProcessingException e) { e.printStackTrace(); }
        return ResponseEntity.ok(result);
//        return "/myMatzip/list";

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
