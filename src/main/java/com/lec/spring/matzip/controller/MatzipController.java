package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.DTO.DetailMapDataDTO;
import com.lec.spring.matzip.domain.DTO.MatzipDTO;
import com.lec.spring.matzip.domain.DTO.SeoulMapDataDTO;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.service.MatzipService;
import com.lec.spring.matzip.service.MyMatzipService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/matzips")
public class MatzipController {

    private final MatzipService matzipService;
    private final MyMatzipService myMatzipService;

    public MatzipController(MatzipService matzipService, MyMatzipService myMatzipService) {
        this.matzipService = matzipService;
        this.myMatzipService = myMatzipService;
    }

    @ResponseBody
    @PostMapping("")
    public ResponseEntity<Map<String, String>> saveMatZip(@RequestBody MatzipDTO matzipDTO) {
        return matzipService.saveMatzip((Matzip) matzipDTO.getData(), matzipDTO.getKind());
    }

    @GetMapping("/{memberId}")
    public String seoulMapMatzip(@PathVariable("memberId") Long memberId, Model model) {
        SeoulMapDataDTO result = myMatzipService.findSeoulMapDataById(memberId);
        model.addAttribute("data", result);
        return "matzip/seoul-map";
    }

    @GetMapping("/homework/{id}")
    public String getHomework(@PathVariable Long id, Model model) {
        Matzip matzip = matzipService.getMatzipById(id, model);
        List<String> tagName = matzipService.listTagName(id);
        List<String> kindName = matzipService.listKindName(id);
        model.addAttribute("kindName", kindName);
        model.addAttribute("tagName", tagName);
        model.addAttribute("matzip", matzip);
        return "matzip/detail";
    }

    @GetMapping("/{memberId}/{gu}")
    public String getGuMapData(@PathVariable String gu, @PathVariable Long memberId, Model model) {
        DetailMapDataDTO result = myMatzipService.findGuMapDataById(memberId, gu);
        model.addAttribute("data", result);
        return "matzip/gu-detail-map";
    }

}
